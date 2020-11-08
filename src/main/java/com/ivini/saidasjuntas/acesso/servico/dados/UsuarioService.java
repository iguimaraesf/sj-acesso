package com.ivini.saidasjuntas.acesso.servico.dados;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.ivini.saidasjuntas.acesso.dto.UsuarioDTO;
import com.ivini.saidasjuntas.acesso.excecao.AbstractSaidasException;
import com.ivini.saidasjuntas.acesso.excecao.EmailJaExisteException;
import com.ivini.saidasjuntas.acesso.excecao.TokenNaoEncontradoException;
import com.ivini.saidasjuntas.acesso.excecao.UsuarioInativoException;
import com.ivini.saidasjuntas.acesso.excecao.UsuarioNaoConfirmadoException;
import com.ivini.saidasjuntas.acesso.excecao.UsuarioNaoEncontradoException;
import com.ivini.saidasjuntas.acesso.excecao.UsuarioSuspensoException;
import com.ivini.saidasjuntas.acesso.modelos.TokenConfirmacao;
import com.ivini.saidasjuntas.acesso.modelos.Usuario;
import com.ivini.saidasjuntas.acesso.repositorio.TokenConfirmacaoRepository;
import com.ivini.saidasjuntas.acesso.repositorio.UsuarioRepository;
import com.ivini.saidasjuntas.acesso.servico.infra.EnvioEmailService;
import com.ivini.saidasjuntas.config.SenhaConfig;

@Service
public class UsuarioService {
	private final UsuarioRepository usuarioRep;
	private final EnvioEmailService envio;
	private final TokenConfirmacaoRepository tokenRep;
	private final SenhaConfig senhaConfig;
	private final CargoService cargoService;

	@Autowired
	public UsuarioService(UsuarioRepository repository, TokenConfirmacaoRepository tokenRep, EnvioEmailService envio,
			SenhaConfig senhaConfig, CargoService cargoService) {
		this.usuarioRep = repository;
		this.tokenRep = tokenRep;
		this.envio = envio;
		this.senhaConfig = senhaConfig;
		this.cargoService = cargoService;
	}
	
	@Transactional
	public Usuario registrarNovoUsuario(UsuarioDTO param) throws AbstractSaidasException {
		if (usuarioRep.existsByEmail(param.getEmail())) {
			throw new EmailJaExisteException(param.getEmail());
		}
		Usuario usuario = new Usuario();
		usuario.setNome(param.getNome());
		usuario.setEmail(param.getEmail());
		usuario.setSenha(senhaConfig.encoder().encode(param.getSenha()));
		usuario.setCargos(cargoService.cargosUsuarioPadrao());
		Usuario res = usuarioRep.save(usuario);

		reenviarPrivate(usuario, param.getSenha());

		return res;
	}

	@Transactional
	public void reenviarToken(String email, String senha) throws AbstractSaidasException {
		Usuario usuario = buscarPorEmail(email);
		reenviarPrivate(usuario, senha);
	}

	public void confirmarUsuario(String codigo) throws AbstractSaidasException {
		TokenConfirmacao token = tokenRep.findByToken(codigo).orElseThrow(() -> new TokenNaoEncontradoException(codigo));
		tokenRep.delete(token);
	}

	public Usuario loginUsuario(String email, String senha) throws AbstractSaidasException {
		Usuario usuario = buscarPorEmail(email);

		if (tokenRep.existsByUsuario(usuario)) {
			throw new UsuarioNaoConfirmadoException(email);
		}
		if (usuario.getDataInativacao() != null) {
			throw new UsuarioInativoException(email);
		}
		if (usuario.getDataFimSuspensao() != null && ChronoUnit.DAYS.between(LocalDate.now(), usuario.getDataFimSuspensao()) >= 0) {
			throw new UsuarioSuspensoException(email, usuario.getDataFimSuspensao());
		}
		conferirSenha(email, senha, usuario);
		return usuario;
	}

	public void inativar(String usuarioId) throws AbstractSaidasException {
		Usuario usuario = buscarPorId(usuarioId);
		usuario.setDataInativacao(LocalDate.now());
		usuarioRep.save(usuario);
	}

	public void reativar(String usuarioId) throws AbstractSaidasException {
		Usuario usuario = buscarPorId(usuarioId);
		usuario.setDataInativacao(null);
		usuarioRep.save(usuario);
	}
	
	public void suspender(String usuarioId) throws AbstractSaidasException {
		Usuario usuario = buscarPorId(usuarioId);
		usuario.setDataFimSuspensao(LocalDate.now().plusDays(15));
		usuarioRep.save(usuario);
	}

	public List<String> temQualDessesAcessos(String usuarioId, List<String> acessos) throws AbstractSaidasException {
		List<String> confirmados = new ArrayList<>();
		if (acessos == null) {
			return confirmados;
		}
		Usuario usuario = buscarPorId(usuarioId);
		if (usuario.getCargos() == null) {
			return confirmados;
		}
		usuario.getCargos().forEach(cargo -> {
			if (cargo.getPrivilegios() != null) {
				cargo.getPrivilegios().forEach(func -> {
					if (acessos.contains(func.getNome())) {
						confirmados.add(func.getNome());
					}
				});
			}
		});
		return confirmados;
	}

	private Usuario buscarPorId(String usuarioId) throws UsuarioNaoEncontradoException {
		return usuarioRep.findById(usuarioId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
	}

	private Usuario buscarPorEmail(String email) throws UsuarioNaoEncontradoException {
		return usuarioRep.findByEmail(email)
				.orElseThrow(() -> new UsuarioNaoEncontradoException(email));
	}

	private void reenviarPrivate(Usuario usuario, String senha) throws TokenNaoEncontradoException, UsuarioNaoEncontradoException {
		if (!tokenRep.existsByUsuario(usuario)) {
			throw new TokenNaoEncontradoException(usuario.getEmail());
		}
		conferirSenha(usuario.getEmail(), senha, usuario);
		tokenRep.deleteByUsuario(usuario);

		final TokenConfirmacao novoToken = tokenRep.save(new TokenConfirmacao(null, usuario, UUID.randomUUID().toString()));
		SimpleMailMessage msg = envio.criarMensagem(usuario.getEmail(), "Confirme sua conta", 
				String.format("Clique <a href='http://localhost:8080/sj-acesso/conf?token=%s'>aqui</a> para confirmar a sua conta.", novoToken.getTokenId()));
		envio.sendMail(msg);
	}

	private void conferirSenha(String email, String senha, Usuario usuario) throws UsuarioNaoEncontradoException {
		if (!senhaConfig.encoder().matches(senha, usuario.getSenha())) {
			throw new UsuarioNaoEncontradoException(email);
		}
	}

}
