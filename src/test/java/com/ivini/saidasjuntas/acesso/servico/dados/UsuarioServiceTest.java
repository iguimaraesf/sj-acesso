package com.ivini.saidasjuntas.acesso.servico.dados;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

import com.ivini.saidasjuntas.acesso.dto.CadastroUsuarioDTO;
import com.ivini.saidasjuntas.acesso.excecao.tipos.AbstractSaidasException;
import com.ivini.saidasjuntas.acesso.excecao.tipos.UsuarioNaoEncontradoException;
import com.ivini.saidasjuntas.acesso.modelo.TokenConfirmacao;
import com.ivini.saidasjuntas.acesso.modelo.Usuario;
import com.ivini.saidasjuntas.acesso.repositorio.TokenConfirmacaoRepository;
import com.ivini.saidasjuntas.acesso.repositorio.UsuarioRepository;
import com.ivini.saidasjuntas.acesso.servico.infra.EnvioEmailService;
import com.ivini.saidasjuntas.config.SenhaConfig;
import com.ivini.saidasjuntas.fixture.TokenConfirmacaoFixture;
import com.ivini.saidasjuntas.fixture.UsuarioDTOFixture;
import com.ivini.saidasjuntas.fixture.UsuarioFixture;
import com.ivini.saidasjuntas.tag.TagSaida;
import com.ivini.saidasjuntas.tag.TesteConstSaida;

@Deprecated
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
	private CadastroUsuarioDTO dto;
	private Usuario usuarioGravado;
	private TokenConfirmacao tokenGravado;
	//private PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
	
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
		dto = UsuarioDTOFixture.criarUsuarioDTOJoao();
		if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_EXISTE_POR_EMAIL)) {
			Mockito.when(repository.existsByEmail(dto.getEmail())).thenReturn(true);
		}
		if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_NAO_EXISTE_POR_EMAIL)) {
			Mockito.when(repository.existsByEmail(dto.getEmail())).thenReturn(false);
		}
		if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_EMAIL)) {
			Mockito.when(repository.findByEmail(dto.getEmail())).thenReturn(Optional.of(UsuarioFixture.criarUsuario(info, dto/*, encoder*/)));
		}
		if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_EMAIL)) {
			Mockito.when(repository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
		}

		usuarioGravado = null;
		if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_GRAVA, TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID, TesteConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_ID)) {
			usuarioGravado = UsuarioFixture.criarUsuario(info, dto/*, encoder*/);
			if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_GRAVA)) {
				Mockito.when(repository.save(Mockito.any())).thenReturn(usuarioGravado);
			}
			if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID)) {
				Mockito.when(repository.findById(usuarioGravado.getIdUsuario())).thenReturn(Optional.of(usuarioGravado));
			} else if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_ID)) {
				Mockito.when(repository.findById(usuarioGravado.getIdUsuario())).thenReturn(Optional.empty());
			}
		}
		
		if (TagSaida.temTag(info, TesteConstSaida.SVC_CARGO_PADRAO_NENHUM)) {
			Mockito.when(cargoService.cargosUsuarioPadrao()).thenReturn(Collections.emptyList());
		}

		if (TagSaida.temTag(info, TesteConstSaida.INFRA_CODIFICA_SENHA)) {
			//Mockito.when(senhaConfig.encoder()).thenReturn(encoder);
		}

		if (TagSaida.temTag(info, TesteConstSaida.BD_TOKEN_ENCONTRADO_POR_USUARIO)) {
			Mockito.when(tokenRep.findByUsuarioIdUsuario(Mockito.any())).thenReturn(TokenConfirmacaoFixture.criarRetornandoAlgo());
		}
		if (TagSaida.temTag(info, TesteConstSaida.BD_TOKEN_NAO_ENCONTRADO_POR_USUARIO)) {
			Mockito.when(tokenRep.findByUsuarioIdUsuario(Mockito.any())).thenReturn(TokenConfirmacaoFixture.criarRetornandoVazio());
		}
		if (TagSaida.temTag(info, TesteConstSaida.BD_TOKEN_GRAVA)) {
			Mockito.when(tokenRep.save(Mockito.any())).thenReturn(new TokenConfirmacao("001", usuarioGravado, "token"));
		}

		tokenGravado = null;
		if (TagSaida.temTag(info, TesteConstSaida.BD_TOKEN_ENCONTRADO_POR_TOKEN)) {
			tokenGravado = new TokenConfirmacao("000", usuarioGravado, "123");
			Mockito.when(tokenRep.findByTokenGerado(Mockito.anyString())).thenReturn(Optional.of(tokenGravado));
		}
		if (TagSaida.temTag(info, TesteConstSaida.BD_TOKEN_NAO_ENCONTRADO_POR_TOKEN)) {
			Mockito.when(tokenRep.findByTokenGerado(Mockito.anyString())).thenReturn(Optional.empty());
		}
	}
	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID),
		@Tag(TesteConstSaida.BD_USUARIO_GRAVA),
	})
	@Test
	void inativarUsuarioEncontrado() throws AbstractSaidasException {
		service.inativar(usuarioGravado.getIdUsuario());
		Mockito.verify(repository).findById(usuarioGravado.getIdUsuario());
		Mockito.verify(repository).save(usuarioGravado);
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_ID),
	})
	@Test
	void inativarUsuarioNaoEncontrado() throws AbstractSaidasException {
		assertThrows(UsuarioNaoEncontradoException.class, () -> {
			service.inativar(usuarioGravado.getIdUsuario());
		});
		Mockito.verify(repository).findById(usuarioGravado.getIdUsuario());
		Mockito.verify(repository, Mockito.never()).save(usuarioGravado);
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID),
		@Tag(TesteConstSaida.BD_USUARIO_GRAVA),
	})
	@Test
	void reativarUsuarioEncontrado() throws AbstractSaidasException {
		service.reativar(usuarioGravado.getIdUsuario());
		Mockito.verify(repository).findById(usuarioGravado.getIdUsuario());
		Mockito.verify(repository).save(usuarioGravado);
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_ID),
	})
	@Test
	void reativarUsuarioNaoEncontrado() throws AbstractSaidasException {
		assertThrows(UsuarioNaoEncontradoException.class, () -> {
			service.reativar(usuarioGravado.getIdUsuario());
		});
		Mockito.verify(repository).findById(usuarioGravado.getIdUsuario());
		Mockito.verify(repository, Mockito.never()).save(usuarioGravado);
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID),
		@Tag(TesteConstSaida.BD_USUARIO_GRAVA),
	})
	@Test
	void suspenderUsuarioEncontrado() throws AbstractSaidasException {
		service.suspender(usuarioGravado.getIdUsuario());
		Mockito.verify(repository).findById(usuarioGravado.getIdUsuario());
		Mockito.verify(repository).save(usuarioGravado);
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_ID),
	})
	@Test
	void suspenderUsuarioNaoEncontrado() throws AbstractSaidasException {
		assertThrows(UsuarioNaoEncontradoException.class, () -> {
			service.suspender(usuarioGravado.getIdUsuario());
		});
		Mockito.verify(repository).findById(usuarioGravado.getIdUsuario());
		Mockito.verify(repository, Mockito.never()).save(usuarioGravado);
	}
	
	@Test
	void acessoNull() throws AbstractSaidasException {
		List<String> acessos = service.temQualDessesAcessos("", null);
		assertThat(acessos).isEmpty();
		Mockito.verify(repository, Mockito.never()).findById("");
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID),
	})
	@Test
	void acessoCargoNull() throws AbstractSaidasException {
		List<String> acessos = service.temQualDessesAcessos(usuarioGravado.getIdUsuario(), Arrays.asList(ConstFuncionalidade.KEY_FUNC_PARTICIPAR_EVENTO));
		assertThat(acessos).isEmpty();
		assertThat(usuarioGravado.getCargos()).isNull();
		Mockito.verify(repository).findById(usuarioGravado.getIdUsuario());
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID),
		@Tag(TesteConstSaida.BD_CARGO_PADRAO),
	})
	@Test
	void acessoCargoPadraoFuncionalidadeNull() throws AbstractSaidasException {
		List<String> acessos = service.temQualDessesAcessos(usuarioGravado.getIdUsuario(), Arrays.asList(ConstFuncionalidade.KEY_FUNC_PARTICIPAR_EVENTO));
		assertThat(acessos).isEmpty();
		assertThat(usuarioGravado.getCargos().iterator().next().getPrivilegios()).isEmpty();
		Mockito.verify(repository).findById(usuarioGravado.getIdUsuario());
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID),
		@Tag(TesteConstSaida.BD_CARGO_PADRAO),
		@Tag(TesteConstSaida.BD_FUNCIONALIDADE_EDITAR_DOCUMENTOS),
	})
	@Test
	void acessoCargoPadraoSemPrivilegio() throws AbstractSaidasException {
		List<String> acessos = service.temQualDessesAcessos(usuarioGravado.getIdUsuario(), Arrays.asList(ConstFuncionalidade.KEY_FUNC_PARTICIPAR_EVENTO));
		assertThat(acessos).isEmpty();
		assertThat(usuarioGravado.getCargos().iterator().next().getPrivilegios()).isNotNull();
		Mockito.verify(repository).findById(usuarioGravado.getIdUsuario());
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID),
		@Tag(TesteConstSaida.BD_CARGO_PADRAO),
		@Tag(TesteConstSaida.BD_FUNCIONALIDADE_PARTICIPAR_EVENTO),
	})
	@Test
	void acessoCargoPadraoParticiparEvento() throws AbstractSaidasException {
		List<String> acessos = service.temQualDessesAcessos(usuarioGravado.getIdUsuario(), Arrays.asList(ConstFuncionalidade.KEY_FUNC_PARTICIPAR_EVENTO));
		assertThat(acessos)
			.hasSize(1)
			.contains(ConstFuncionalidade.KEY_FUNC_PARTICIPAR_EVENTO);
		Mockito.verify(repository).findById(usuarioGravado.getIdUsuario());
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID),
		@Tag(TesteConstSaida.BD_CARGO_PADRAO),
		@Tag(TesteConstSaida.BD_FUNCIONALIDADE_AVALIAR_EVENTO),
		@Tag(TesteConstSaida.BD_FUNCIONALIDADE_AVALIAR_PARTICIPANTE),
	})
	@Test
	void acessoCargoPadraoAvaliarUmaNaoTem() throws AbstractSaidasException {
		List<String> acessos = service.temQualDessesAcessos(usuarioGravado.getIdUsuario(), 
				Arrays.asList(ConstFuncionalidade.KEY_FUNC_AVALIAR_EVENTO,
						ConstFuncionalidade.KEY_FUNC_AVALIAR_PARTICIPANTE,
						ConstFuncionalidade.BD_FUNC_BLOQUEAR_PARTICIPANTE));
		assertThat(acessos)
			.hasSize(2)
			.contains(ConstFuncionalidade.KEY_FUNC_AVALIAR_EVENTO)
			.contains(ConstFuncionalidade.KEY_FUNC_AVALIAR_PARTICIPANTE);
		Mockito.verify(repository).findById(usuarioGravado.getIdUsuario());
	}
}
