package com.ivantex.saidasjuntas.acesso.servico.dados;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.ivantex.saidasjuntas.acesso.dto.UsuarioDTO;
import com.ivantex.saidasjuntas.acesso.excecao.AbstractSaidasException;
import com.ivantex.saidasjuntas.acesso.excecao.EmailJaExisteException;
import com.ivantex.saidasjuntas.acesso.excecao.TokenInvalidoException;
import com.ivantex.saidasjuntas.acesso.excecao.UsuarioInativoException;
import com.ivantex.saidasjuntas.acesso.excecao.UsuarioNaoConfirmadoException;
import com.ivantex.saidasjuntas.acesso.excecao.UsuarioNaoEncontradoException;
import com.ivantex.saidasjuntas.acesso.excecao.UsuarioSuspensoException;
import com.ivantex.saidasjuntas.acesso.modelos.TokenConfirmacao;
import com.ivantex.saidasjuntas.acesso.modelos.Usuario;
import com.ivantex.saidasjuntas.acesso.repositorio.TokenConfirmacaoRepository;
import com.ivantex.saidasjuntas.acesso.repositorio.UsuarioRepository;
import com.ivantex.saidasjuntas.acesso.servico.infra.EnvioEmailService;
import com.ivantex.saidasjuntas.config.SenhaConfig;

@Service
public class UsuarioService {
	private final UsuarioRepository usuarioRep;
	private final EnvioEmailService envio;
	private final TokenConfirmacaoRepository tokenRep;
	private final SenhaConfig senhaConfig;

	@Autowired
	public UsuarioService(UsuarioRepository repository, TokenConfirmacaoRepository tokenRep, EnvioEmailService envio, SenhaConfig senhaConfig) {
		this.usuarioRep = repository;
		this.tokenRep = tokenRep;
		this.envio = envio;
		this.senhaConfig = senhaConfig;
	}
	
	@Transactional
	public Usuario novoUsuario(UsuarioDTO param) throws AbstractSaidasException {
		if (usuarioRep.existsByEmail(param.getEmail())) {
			throw new EmailJaExisteException(param.getEmail());
		}
		Usuario usuario = new Usuario();
		usuario.setNome(param.getNome());
		usuario.setEmail(param.getEmail());
		usuario.setSenha(senhaConfig.encoder().encode(param.getSenha()));
		// TODO usuario.setCargos(Arrays.asList(new Cargo(1, "Usuário final")));
		Usuario res = usuarioRep.save(usuario);

		reenviarPrivate(usuario);

		return res;
	}

	@Transactional
	public void reenviarToken(UsuarioDTO param) throws AbstractSaidasException {
		Usuario usuario = usuarioRep.findByEmail(param.getEmail())
				.orElseThrow(() -> new UsuarioNaoEncontradoException(param.getEmail()));
		reenviarPrivate(usuario);
	}

	public void confirmarUsuario(String codigo) throws AbstractSaidasException {
		TokenConfirmacao token = tokenRep.findByToken(codigo).orElseThrow(() -> new TokenInvalidoException(codigo));
		tokenRep.delete(token);
	}

	public Usuario loginUsuario(String email, String senha) throws AbstractSaidasException {
		Usuario usuario = usuarioRep.findByEmail(email)
				.orElseThrow(() -> new UsuarioNaoEncontradoException(email));

		if (tokenRep.existsByUsuario(usuario)) {
			throw new UsuarioNaoConfirmadoException(email);
		}
		if (usuario.getDataInativacao() != null) {
			throw new UsuarioInativoException(email);
		}
		if (usuario.getDataFimSuspensao() != null && ChronoUnit.DAYS.between(LocalDate.now(), usuario.getDataFimSuspensao()) >= 0) {
			throw new UsuarioSuspensoException(email, usuario.getDataFimSuspensao());
		}
		if (!senhaConfig.encoder().matches(senha, usuario.getSenha())) {
			throw new UsuarioNaoEncontradoException(email);
		}
		return usuario;
	}

	public void inativar(String usuarioId) throws AbstractSaidasException {
		Usuario usuario = usuarioRep.findById(usuarioId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
		usuario.setDataInativacao(LocalDate.now());
		usuarioRep.save(usuario);
	}

	public void reativar(String usuarioId) throws AbstractSaidasException {
		Usuario usuario = usuarioRep.findById(usuarioId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
		usuario.setDataInativacao(null);
		usuarioRep.save(usuario);
	}
	
	public void suspender(String usuarioId) throws AbstractSaidasException {
		Usuario usuario = usuarioRep.findById(usuarioId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
		usuario.setDataFimSuspensao(LocalDate.now().plusDays(15));
		usuarioRep.save(usuario);
	}

	private void reenviarPrivate(Usuario usuario) throws TokenInvalidoException {
		if (!tokenRep.existsByUsuario(usuario)) {
			throw new TokenInvalidoException(usuario.getEmail());
		}
		tokenRep.deleteByUsuario(usuario);
		
		final TokenConfirmacao novoToken = tokenRep.save(new TokenConfirmacao(null, usuario, UUID.randomUUID().toString()));
		SimpleMailMessage msg = envio.criarMensagem(usuario.getEmail(), "Confirme sua conta", 
				String.format("Clique <a href='http://localhost:8080/sj-acesso/conf?token=%s'>aqui</a> para confirmar a sua conta.", novoToken.getTokenId()));
		envio.sendMail(msg);
	}

}
