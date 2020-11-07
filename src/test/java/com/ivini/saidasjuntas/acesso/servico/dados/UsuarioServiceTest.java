package com.ivini.saidasjuntas.acesso.servico.dados;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
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

import com.ivini.saidasjuntas.acesso.dto.UsuarioDTO;
import com.ivini.saidasjuntas.acesso.excecao.AbstractSaidasException;
import com.ivini.saidasjuntas.acesso.excecao.EmailJaExisteException;
import com.ivini.saidasjuntas.acesso.excecao.TokenInvalidoException;
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
import com.ivini.saidasjuntas.fixture.TagSaida;
import com.ivini.saidasjuntas.fixture.UsuarioFixture;

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
	@Mock
	private CargoService cargoService;

	@InjectMocks
	private UsuarioService service;
	
	@BeforeEach
	void setUp(TestInfo info) {
		dto = UsuarioFixture.criarUsuarioDTOJoao();
		if (TagSaida.temTag(info, TagSaida.USUARIO_EXISTE_POR_EMAIL)) {
			Mockito.when(repository.existsByEmail(dto.getEmail())).thenReturn(true);
		}
		if (TagSaida.temTag(info, TagSaida.USUARIO_NAO_EXISTE_POR_EMAIL)) {
			Mockito.when(repository.existsByEmail(dto.getEmail())).thenReturn(false);
		}
		if (TagSaida.temTag(info, TagSaida.USUARIO_ENCONTRADO_POR_EMAIL)) {
			Mockito.when(repository.findByEmail(dto.getEmail())).thenReturn(Optional.of(UsuarioFixture.criarUsuario(info, dto, encoder)));
		}
		if (TagSaida.temTag(info, TagSaida.USUARIO_NAO_ENCONTRADO_POR_EMAIL)) {
			Mockito.when(repository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
		}

		usuarioGravado = null;
		if (TagSaida.temTag(info, TagSaida.USUARIO_GRAVA, TagSaida.USUARIO_ENCONTRADO_POR_ID, TagSaida.USUARIO_NAO_ENCONTRADO_POR_ID)) {
			usuarioGravado = UsuarioFixture.criarUsuario(info, dto, encoder);
			if (TagSaida.temTag(info, TagSaida.USUARIO_GRAVA)) {
				Mockito.when(repository.save(Mockito.any())).thenReturn(usuarioGravado);
			}
			if (TagSaida.temTag(info, TagSaida.USUARIO_ENCONTRADO_POR_ID)) {
				Mockito.when(repository.findById(usuarioGravado.getUsuarioId())).thenReturn(Optional.of(usuarioGravado));
			} else if (TagSaida.temTag(info, TagSaida.USUARIO_NAO_ENCONTRADO_POR_ID)) {
				Mockito.when(repository.findById(usuarioGravado.getUsuarioId())).thenReturn(Optional.empty());
			}
		}
		
		if (TagSaida.temTag(info, TagSaida.SERVICO_CARGO_PADRAO_NENHUM)) {
			Mockito.when(cargoService.cargosUsuarioPadrao()).thenReturn(Collections.emptyList());
		}

		if (TagSaida.temTag(info, TagSaida.CODIFICA_SENHA)) {
			Mockito.when(senhaConfig.encoder()).thenReturn(encoder);
		}

		if (TagSaida.temTag(info, TagSaida.TOKEN_EXISTE_POR_USUARIO)) {
			Mockito.when(tokenRep.existsByUsuario(Mockito.any())).thenReturn(true);
		}
		if (TagSaida.temTag(info, TagSaida.TOKEN_NAO_EXISTE_POR_USUARIO)) {
			Mockito.when(tokenRep.existsByUsuario(Mockito.any())).thenReturn(false);
		}
		if (TagSaida.temTag(info, TagSaida.TOKEN_GRAVA)) {
			Mockito.when(tokenRep.save(Mockito.any())).thenReturn(new TokenConfirmacao("id", usuarioGravado, "token"));
		}

		tokenGravado = null;
		if (TagSaida.temTag(info, TagSaida.TOKEN_ENCONTRADO_POR_TOKEN)) {
			tokenGravado = new TokenConfirmacao("000", usuarioGravado, "123");
			Mockito.when(tokenRep.findByToken(Mockito.anyString())).thenReturn(Optional.of(tokenGravado));
		}
		if (TagSaida.temTag(info, TagSaida.TOKEN_NAO_ENCONTRADO_POR_TOKEN)) {
			Mockito.when(tokenRep.findByToken(Mockito.anyString())).thenReturn(Optional.empty());
		}
	}
	
	@Tags({
		@Tag(TagSaida.USUARIO_GRAVA),
		@Tag(TagSaida.SERVICO_CARGO_PADRAO_NENHUM),
		@Tag(TagSaida.CODIFICA_SENHA),
		@Tag(TagSaida.TOKEN_EXISTE_POR_USUARIO),
		@Tag(TagSaida.USUARIO_NAO_EXISTE_POR_EMAIL),
		@Tag(TagSaida.TOKEN_GRAVA),
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
		@Tag(TagSaida.USUARIO_EXISTE_POR_EMAIL)
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
		@Tag(TagSaida.TOKEN_GRAVA),
		@Tag(TagSaida.TOKEN_EXISTE_POR_USUARIO),
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
		@Tag(TagSaida.USUARIO_NAO_ENCONTRADO_POR_EMAIL),
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
		@Tag(TagSaida.TOKEN_ENCONTRADO_POR_TOKEN),
	})
	@Test
	void confirmarUsuarioQuandoTokenExiste() throws AbstractSaidasException {
		service.confirmarUsuario("123");
		Mockito.verify(tokenRep).findByToken("123");
		Mockito.verify(tokenRep).delete(tokenGravado);
	}

	@Tags({
		@Tag(TagSaida.TOKEN_NAO_ENCONTRADO_POR_TOKEN),
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
		@Tag(TagSaida.TOKEN_NAO_EXISTE_POR_USUARIO),
	})
	@Test
	void loginOk() throws AbstractSaidasException {
		Usuario usuario = service.loginUsuario(dto.getEmail(), dto.getSenha());
		Mockito.verify(repository).findByEmail(usuario.getEmail());
		Mockito.verify(tokenRep).existsByUsuario(usuario);
		Mockito.verify(senhaConfig).encoder();
	}
	
	@Tags({
		@Tag(TagSaida.USUARIO_NAO_ENCONTRADO_POR_EMAIL),
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
		@Tag(TagSaida.TOKEN_EXISTE_POR_USUARIO),
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
		@Tag(TagSaida.TOKEN_NAO_EXISTE_POR_USUARIO),
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
		@Tag(TagSaida.TOKEN_NAO_EXISTE_POR_USUARIO),
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
		@Tag(TagSaida.TOKEN_NAO_EXISTE_POR_USUARIO),
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
		@Tag(TagSaida.USUARIO_GRAVA),
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
		@Tag(TagSaida.USUARIO_GRAVA),
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
		@Tag(TagSaida.USUARIO_GRAVA),
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
