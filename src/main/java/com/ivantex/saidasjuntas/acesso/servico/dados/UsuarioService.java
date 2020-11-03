package com.ivantex.saidasjuntas.acesso.servico.dados;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.ivantex.saidasjuntas.acesso.excecao.AbstractSaidasException;
import com.ivantex.saidasjuntas.acesso.excecao.EmailJaExisteException;
import com.ivantex.saidasjuntas.acesso.excecao.TokenJaValidadoException;
import com.ivantex.saidasjuntas.acesso.excecao.UsuarioInativoException;
import com.ivantex.saidasjuntas.acesso.excecao.UsuarioNaoConfirmadoException;
import com.ivantex.saidasjuntas.acesso.excecao.UsuarioNaoEncontrado;
import com.ivantex.saidasjuntas.acesso.excecao.UsuarioSuspensoException;
import com.ivantex.saidasjuntas.acesso.modelos.TokenConfirmacao;
import com.ivantex.saidasjuntas.acesso.modelos.Usuario;
import com.ivantex.saidasjuntas.acesso.repositorio.TokenConfirmacaoRepository;
import com.ivantex.saidasjuntas.acesso.repositorio.UsuarioRepository;
import com.ivantex.saidasjuntas.acesso.servico.infra.EnvioEmailService;

@Service
public class UsuarioService {
	private final UsuarioRepository usuarioRep;
	private final EnvioEmailService envio;
	private final TokenConfirmacaoRepository tokenRep;

	@Autowired
	public UsuarioService(UsuarioRepository repository, TokenConfirmacaoRepository tokenRep, EnvioEmailService envio) {
		this.usuarioRep = repository;
		this.tokenRep = tokenRep;
		this.envio = envio;
	}
	
	public Usuario novoUsuario(Usuario usuario) throws AbstractSaidasException {
		if (usuarioRep.existsByEmail(usuario.getEmail())) {
			throw new EmailJaExisteException(usuario.getEmail());
		}
		Usuario res = usuarioRep.save(usuario);
		SimpleMailMessage msg = envio.criarMensagem(usuario.getEmail(), "Confirme sua conta", 
				String.format("Clique <a href='http://localhost:8080/sj-acesso/conf?token=%s'>aqui</a> para confirmar a sua conta.", res.getUsuarioId()));
		tokenRep.save(new TokenConfirmacao(null, res, UUID.randomUUID().toString()));
		envio.sendMail(msg);
		return res;
	}
	
	public Usuario lerUsuario(String emailOuDocumento) throws AbstractSaidasException {
		Usuario usuario = usuarioRep.findByEmail(emailOuDocumento)
				.orElseThrow(() -> new UsuarioNaoEncontrado(emailOuDocumento));

		if (tokenRep.existsByUsuario(usuario)) {
			throw new UsuarioNaoConfirmadoException(emailOuDocumento);
		}
		if (usuario.getDataInativacao() != null) {
			throw new UsuarioInativoException(emailOuDocumento);
		}
		if (usuario.getDataFimSuspensao() != null && ChronoUnit.DAYS.between(LocalDate.now(), usuario.getDataFimSuspensao()) > 0) {
			throw new UsuarioSuspensoException(emailOuDocumento, usuario.getDataFimSuspensao());
		}
		return usuario;
	}
	
	public void confirmarUsuario(String codigo) throws AbstractSaidasException {
		TokenConfirmacao token = tokenRep.findByToken(codigo);
		if (token == null) {
			throw new TokenJaValidadoException(codigo);
		}
		tokenRep.delete(token);
	}

	public void inativar(String id) throws AbstractSaidasException {
		Usuario usuario = usuarioRep.findById(id)
				.orElseThrow(() -> new UsuarioNaoEncontrado(id));
		usuario.setDataInativacao(LocalDate.now());
		usuarioRep.save(usuario);
	}

	public void reativar(String id) throws AbstractSaidasException {
		Usuario usuario = usuarioRep.findById(id)
				.orElseThrow(() -> new UsuarioNaoEncontrado(id));
		usuario.setDataInativacao(null);
		usuarioRep.save(usuario);
	}
	
	public void suspender(String id) throws AbstractSaidasException {
		Usuario usuario = usuarioRep.findById(id)
				.orElseThrow(() -> new UsuarioNaoEncontrado(id));
		usuario.setDataFimSuspensao(LocalDate.now().plusDays(15));
		usuarioRep.save(usuario);
	}

}
