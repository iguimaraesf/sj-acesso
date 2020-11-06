package com.ivantex.saidasjuntas.acesso.servico.dados;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ivantex.saidasjuntas.acesso.dto.UsuarioDTO;
import com.ivantex.saidasjuntas.acesso.excecao.AbstractSaidasException;
import com.ivantex.saidasjuntas.acesso.excecao.EmailJaExisteException;
import com.ivantex.saidasjuntas.acesso.excecao.TokenInvalidoException;
import com.ivantex.saidasjuntas.acesso.modelos.TokenConfirmacao;
import com.ivantex.saidasjuntas.acesso.modelos.Usuario;
import com.ivantex.saidasjuntas.acesso.repositorio.TokenConfirmacaoRepository;
import com.ivantex.saidasjuntas.acesso.repositorio.UsuarioRepository;
import com.ivantex.saidasjuntas.acesso.servico.infra.EnvioEmailService;
import com.ivantex.saidasjuntas.config.SenhaConfig;
import com.ivantex.saidasjuntas.fixture.TagSaida;
import com.ivantex.saidasjuntas.fixture.UsuarioFixture;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
	private UsuarioDTO dto;
	private Usuario usuarioGravado;
	
	@Mock
	private UsuarioRepository repository;
	@Mock
	private EnvioEmailService envio;
	@Mock
	private TokenConfirmacaoRepository tokenRep;
	@Mock
	private SenhaConfig senhaConfig;

	@InjectMocks
	private UsuarioService service;
	
	@BeforeEach
	void setUp(TestInfo info) {
		dto = UsuarioFixture.criarUsuarioDTOJoao();
		if (TagSaida.temTag(info, TagSaida.USUARIO_EXISTE)) {
			Mockito.when(repository.existsByEmail(dto.getEmail())).thenReturn(true);
		}
		if (TagSaida.temTag(info, TagSaida.USUARIO_NAO_EXISTE)) {
			Mockito.when(repository.existsByEmail(dto.getEmail())).thenReturn(false);
		}
		if (TagSaida.temTag(info, TagSaida.USUARIO_ENCONTRADO)) {
			Mockito.when(repository.findByEmail(dto.getEmail())).thenReturn(Optional.of(UsuarioFixture.criarUsuario(dto)));
		}
		if (TagSaida.temTag(info, TagSaida.USUARIO_NAO_ENCONTRADO)) {
			Mockito.when(repository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
		}
		if (TagSaida.temTag(info, TagSaida.GRAVA_USUARIO)) {
			usuarioGravado = UsuarioFixture.criarUsuario(dto);
			Mockito.when(senhaConfig.encoder()).thenReturn(new BCryptPasswordEncoder());
			Mockito.when(tokenRep.existsByUsuario(Mockito.any())).thenReturn(true);
			Mockito.when(repository.save(Mockito.any())).thenReturn(usuarioGravado);
		} else {
			usuarioGravado = null;
		}
		if (TagSaida.temTag(info, TagSaida.TOKEN_EXISTE)) {
			Mockito.when(tokenRep.existsByUsuario(Mockito.any())).thenReturn(true);
		}
		if (TagSaida.temTag(info, TagSaida.GRAVA_TOKEN)) {
			Mockito.when(tokenRep.save(Mockito.any())).thenReturn(new TokenConfirmacao("id", usuarioGravado, "token"));
		}
	}
	
	@Tags({
		@Tag(TagSaida.GRAVA_USUARIO),
		@Tag(TagSaida.TOKEN_EXISTE),
		@Tag(TagSaida.USUARIO_NAO_EXISTE),
		@Tag(TagSaida.GRAVA_TOKEN),
	})
	@Test
	void registroGravaNovoUsuario() throws AbstractSaidasException {
		final Usuario res = service.novoUsuario(dto);
		assertThat(res).isEqualTo(usuarioGravado);
		Mockito.verify(repository).existsByEmail(Mockito.anyString());
		Mockito.verify(repository).save(Mockito.any());
		Mockito.verify(tokenRep).existsByUsuario(Mockito.any());
		Mockito.verify(tokenRep).deleteByUsuario(Mockito.any());
		Mockito.verify(tokenRep).save(Mockito.any());
		Mockito.verify(envio).sendMail(Mockito.any());
	}
	
	@Tags({
		@Tag(TagSaida.USUARIO_EXISTE)
	})
	@Test
	void registroUsuarioJaExiste() throws AbstractSaidasException {
		assertThrows(EmailJaExisteException.class, () -> {
			service.novoUsuario(dto);
		});
		Mockito.verify(repository).existsByEmail(Mockito.anyString());
	}

	@Tags({
		@Tag(TagSaida.USUARIO_ENCONTRADO),
		@Tag(TagSaida.GRAVA_TOKEN),
		@Tag(TagSaida.TOKEN_EXISTE),
	})
	@Test
	void reenviarLinkExisteAnterior() throws AbstractSaidasException {
		service.reenviarToken(dto);
		Mockito.verify(repository).findByEmail(dto.getEmail());
		Mockito.verify(tokenRep).existsByUsuario(Mockito.any());
		Mockito.verify(tokenRep).deleteByUsuario(Mockito.any());
		Mockito.verify(tokenRep).save(Mockito.any());
		Mockito.verify(envio).sendMail(Mockito.any());
	}
	
	@Tags({
		@Tag(TagSaida.USUARIO_ENCONTRADO),
	})
	@Test
	void reenviarLinkNaoExisteAnterior() throws AbstractSaidasException {
		assertThrows(TokenInvalidoException.class, () -> {
			service.reenviarToken(dto);
		});
		Mockito.verify(repository).findByEmail(dto.getEmail());
		Mockito.verify(tokenRep).existsByUsuario(Mockito.any());
		Mockito.verify(tokenRep, Mockito.never()).deleteByUsuario(Mockito.any());
	}

}
