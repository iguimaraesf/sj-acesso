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
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
import com.ivantex.saidasjuntas.fixture.TagSaida;
import com.ivantex.saidasjuntas.fixture.UsuarioFixture;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
	private UsuarioDTO dto;
	private Usuario usuarioGravado;
	private TokenConfirmacao tokenGravado;
	private PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
	
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
		if (TagSaida.temTag(info, TagSaida.USUARIO_ENCONTRADO_POR_EMAIL)) {
			Mockito.when(repository.findByEmail(dto.getEmail())).thenReturn(Optional.of(UsuarioFixture.criarUsuario(info, dto, encoder)));
		}
		if (TagSaida.temTag(info, TagSaida.USUARIO_NAO_ENCONTRADO)) {
			Mockito.when(repository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
		}

		usuarioGravado = null;
		if (TagSaida.temTag(info, TagSaida.GRAVA_USUARIO, TagSaida.USUARIO_ENCONTRADO_POR_ID, TagSaida.USUARIO_NAO_ENCONTRADO_POR_ID)) {
			usuarioGravado = UsuarioFixture.criarUsuario(info, dto, encoder);
			if (TagSaida.temTag(info, TagSaida.GRAVA_USUARIO)) {
				Mockito.when(repository.save(Mockito.any())).thenReturn(usuarioGravado);
			}
			if (TagSaida.temTag(info, TagSaida.USUARIO_ENCONTRADO_POR_ID)) {
				Mockito.when(repository.findById(usuarioGravado.getUsuarioId())).thenReturn(Optional.of(usuarioGravado));
			} else if (TagSaida.temTag(info, TagSaida.USUARIO_NAO_ENCONTRADO_POR_ID)) {
				Mockito.when(repository.findById(usuarioGravado.getUsuarioId())).thenReturn(Optional.empty());
			}
		}

		if (TagSaida.temTag(info, TagSaida.CODIFICA_SENHA)) {
			Mockito.when(senhaConfig.encoder()).thenReturn(encoder);
		}

		if (TagSaida.temTag(info, TagSaida.PROCURA_TOKEN_POR_USUARIO_EXISTE)) {
			Mockito.when(tokenRep.existsByUsuario(Mockito.any())).thenReturn(true);
		}
		if (TagSaida.temTag(info, TagSaida.PROCURA_TOKEN_POR_USUARIO_NAO_EXISTE)) {
			Mockito.when(tokenRep.existsByUsuario(Mockito.any())).thenReturn(false);
		}
		if (TagSaida.temTag(info, TagSaida.GRAVA_TOKEN)) {
			Mockito.when(tokenRep.save(Mockito.any())).thenReturn(new TokenConfirmacao("id", usuarioGravado, "token"));
		}

		tokenGravado = null;
		if (TagSaida.temTag(info, TagSaida.CONFIRMAR_TOKEN_EXISTE)) {
			tokenGravado = new TokenConfirmacao("000", usuarioGravado, "123");
			Mockito.when(tokenRep.findByToken(Mockito.anyString())).thenReturn(Optional.of(tokenGravado));
		}
		if (TagSaida.temTag(info, TagSaida.CONFIRMAR_TOKEN_NAO_EXISTE)) {
			Mockito.when(tokenRep.findByToken(Mockito.anyString())).thenReturn(Optional.empty());
		}
	}
	
	@Tags({
		@Tag(TagSaida.GRAVA_USUARIO),
		@Tag(TagSaida.CODIFICA_SENHA),
		@Tag(TagSaida.PROCURA_TOKEN_POR_USUARIO_EXISTE),
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
		@Tag(TagSaida.USUARIO_ENCONTRADO_POR_EMAIL),
		@Tag(TagSaida.GRAVA_TOKEN),
		@Tag(TagSaida.PROCURA_TOKEN_POR_USUARIO_EXISTE),
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
		@Tag(TagSaida.USUARIO_ENCONTRADO_POR_EMAIL),
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
	
	@Tags({
		@Tag(TagSaida.USUARIO_NAO_ENCONTRADO),
	})
	@Test
	void reenviarLinkUsuarioNaoExiste() throws AbstractSaidasException {
		assertThrows(UsuarioNaoEncontradoException.class, () -> {
			service.reenviarToken(dto);
		});
		Mockito.verify(repository).findByEmail(dto.getEmail());
		Mockito.verify(tokenRep, Mockito.never()).existsByUsuario(Mockito.any());
		Mockito.verify(tokenRep, Mockito.never()).deleteByUsuario(Mockito.any());
	}
	
	@Tags({
		@Tag(TagSaida.CONFIRMAR_TOKEN_EXISTE),
	})
	@Test
	void confirmarUsuarioQuandoTokenExiste() throws AbstractSaidasException {
		service.confirmarUsuario("123");
		Mockito.verify(tokenRep).findByToken("123");
		Mockito.verify(tokenRep).delete(tokenGravado);
	}

	@Tags({
		@Tag(TagSaida.CONFIRMAR_TOKEN_NAO_EXISTE),
	})
	@Test
	void confirmarUsuarioQuandoTokenNaoExiste() throws AbstractSaidasException {
		assertThrows(TokenInvalidoException.class, () -> {
			service.confirmarUsuario("123");
		});
		Mockito.verify(tokenRep).findByToken("123");
		Mockito.verify(tokenRep, Mockito.never()).delete(tokenGravado);
	}

	@Tags({
		@Tag(TagSaida.USUARIO_ENCONTRADO_POR_EMAIL),
		@Tag(TagSaida.CODIFICA_SENHA),
		@Tag(TagSaida.PROCURA_TOKEN_POR_USUARIO_NAO_EXISTE),
	})
	@Test
	void loginOk() throws AbstractSaidasException {
		Usuario usuario = service.loginUsuario(dto.getEmail(), dto.getSenha());
		Mockito.verify(repository).findByEmail(usuario.getEmail());
		Mockito.verify(tokenRep).existsByUsuario(usuario);
		Mockito.verify(senhaConfig).encoder();
	}
	
	@Tags({
		@Tag(TagSaida.USUARIO_NAO_ENCONTRADO),
	})
	@Test
	void loginUsuarioNaoCadastrado() throws AbstractSaidasException {
		assertThrows(UsuarioNaoEncontradoException.class, () -> {
			service.loginUsuario(dto.getEmail(), dto.getSenha());
		});
		Mockito.verify(repository).findByEmail(dto.getEmail());
		Mockito.verify(tokenRep, Mockito.never()).existsByUsuario(Mockito.any());
		Mockito.verify(senhaConfig, Mockito.never()).encoder();
	}

	@Tags({
		@Tag(TagSaida.USUARIO_ENCONTRADO_POR_EMAIL),
		@Tag(TagSaida.PROCURA_TOKEN_POR_USUARIO_EXISTE),
	})
	@Test
	void loginUsuarioNaoConfirmou() throws AbstractSaidasException {
		assertThrows(UsuarioNaoConfirmadoException.class, () -> {
			service.loginUsuario(dto.getEmail(), dto.getSenha());
		});
		Mockito.verify(repository).findByEmail(dto.getEmail());
		Mockito.verify(tokenRep).existsByUsuario(Mockito.any());
		Mockito.verify(senhaConfig, Mockito.never()).encoder();
	}

	@Tags({
		@Tag(TagSaida.USUARIO_ENCONTRADO_POR_EMAIL),
		@Tag(TagSaida.USUARIO_INATIVO),
		@Tag(TagSaida.PROCURA_TOKEN_POR_USUARIO_NAO_EXISTE),
	})
	@Test
	void loginUsuarioInativo() throws AbstractSaidasException {
		assertThrows(UsuarioInativoException.class, () -> {
			service.loginUsuario(dto.getEmail(), dto.getSenha());
		});
		Mockito.verify(repository).findByEmail(dto.getEmail());
		Mockito.verify(tokenRep).existsByUsuario(Mockito.any());
		Mockito.verify(senhaConfig, Mockito.never()).encoder();
	}

	@Tags({
		@Tag(TagSaida.USUARIO_ENCONTRADO_POR_EMAIL),
		@Tag(TagSaida.USUARIO_ESTA_SUSPENSO),
		@Tag(TagSaida.PROCURA_TOKEN_POR_USUARIO_NAO_EXISTE),
	})
	@Test
	void loginUsuarioEstaSuspenso() throws AbstractSaidasException {
		assertThrows(UsuarioSuspensoException.class, () -> {
			service.loginUsuario(dto.getEmail(), dto.getSenha());
		});
		Mockito.verify(repository).findByEmail(dto.getEmail());
		Mockito.verify(tokenRep).existsByUsuario(Mockito.any());
		Mockito.verify(senhaConfig, Mockito.never()).encoder();
	}

	@Tags({
		@Tag(TagSaida.USUARIO_ENCONTRADO_POR_EMAIL),
		@Tag(TagSaida.USUARIO_SENHA_ERRADA),
		@Tag(TagSaida.USUARIO_ESTEVE_SUSPENSO),
		@Tag(TagSaida.CODIFICA_SENHA),
		@Tag(TagSaida.PROCURA_TOKEN_POR_USUARIO_NAO_EXISTE),
	})
	@Test
	void loginUsuarioPassouSuspensaoSenhaErrada() throws AbstractSaidasException {
		assertThrows(UsuarioNaoEncontradoException.class, () -> {
			service.loginUsuario(dto.getEmail(), dto.getSenha());
		});
		Mockito.verify(repository).findByEmail(dto.getEmail());
		Mockito.verify(tokenRep).existsByUsuario(Mockito.any());
		Mockito.verify(senhaConfig).encoder();
	}

	@Tags({
		@Tag(TagSaida.USUARIO_ENCONTRADO_POR_ID),
		@Tag(TagSaida.GRAVA_USUARIO),
	})
	@Test
	void inativarUsuarioEncontrado() throws AbstractSaidasException {
		service.inativar(usuarioGravado.getUsuarioId());
		Mockito.verify(repository).findById(usuarioGravado.getUsuarioId());
		Mockito.verify(repository).save(usuarioGravado);
	}

	@Tags({
		@Tag(TagSaida.USUARIO_NAO_ENCONTRADO_POR_ID),
	})
	@Test
	void inativarUsuarioNaoEncontrado() throws AbstractSaidasException {
		assertThrows(UsuarioNaoEncontradoException.class, () -> {
			service.inativar(usuarioGravado.getUsuarioId());
		});
		Mockito.verify(repository).findById(usuarioGravado.getUsuarioId());
		Mockito.verify(repository, Mockito.never()).save(usuarioGravado);
	}

	@Tags({
		@Tag(TagSaida.USUARIO_ENCONTRADO_POR_ID),
		@Tag(TagSaida.GRAVA_USUARIO),
	})
	@Test
	void reativarUsuarioEncontrado() throws AbstractSaidasException {
		service.reativar(usuarioGravado.getUsuarioId());
		Mockito.verify(repository).findById(usuarioGravado.getUsuarioId());
		Mockito.verify(repository).save(usuarioGravado);
	}

	@Tags({
		@Tag(TagSaida.USUARIO_NAO_ENCONTRADO_POR_ID),
	})
	@Test
	void reativarUsuarioNaoEncontrado() throws AbstractSaidasException {
		assertThrows(UsuarioNaoEncontradoException.class, () -> {
			service.reativar(usuarioGravado.getUsuarioId());
		});
		Mockito.verify(repository).findById(usuarioGravado.getUsuarioId());
		Mockito.verify(repository, Mockito.never()).save(usuarioGravado);
	}

	@Tags({
		@Tag(TagSaida.USUARIO_ENCONTRADO_POR_ID),
		@Tag(TagSaida.GRAVA_USUARIO),
	})
	@Test
	void suspenderUsuarioEncontrado() throws AbstractSaidasException {
		service.suspender(usuarioGravado.getUsuarioId());
		Mockito.verify(repository).findById(usuarioGravado.getUsuarioId());
		Mockito.verify(repository).save(usuarioGravado);
	}

	@Tags({
		@Tag(TagSaida.USUARIO_NAO_ENCONTRADO_POR_ID),
	})
	@Test
	void suspenderUsuarioNaoEncontrado() throws AbstractSaidasException {
		assertThrows(UsuarioNaoEncontradoException.class, () -> {
			service.suspender(usuarioGravado.getUsuarioId());
		});
		Mockito.verify(repository).findById(usuarioGravado.getUsuarioId());
		Mockito.verify(repository, Mockito.never()).save(usuarioGravado);
	}
}
