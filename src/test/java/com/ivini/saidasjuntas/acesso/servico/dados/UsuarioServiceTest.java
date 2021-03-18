package com.ivini.saidasjuntas.acesso.servico.dados;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.ivini.saidasjuntas.acesso.dto.AssociacaoDTO;
import com.ivini.saidasjuntas.acesso.excecao.tipos.AbstractSaidasException;
import com.ivini.saidasjuntas.acesso.excecao.tipos.OutroResponsavelException;
import com.ivini.saidasjuntas.acesso.excecao.tipos.TokenNaoEncontradoException;
import com.ivini.saidasjuntas.acesso.excecao.tipos.UsuarioNaoEncontradoException;
import com.ivini.saidasjuntas.acesso.modelo.Usuario;
import com.ivini.saidasjuntas.acesso.repositorio.CargoRepository;
import com.ivini.saidasjuntas.acesso.repositorio.FuncionalidadeRepository;
import com.ivini.saidasjuntas.acesso.repositorio.TokenConfirmacaoRepository;
import com.ivini.saidasjuntas.acesso.repositorio.UsuarioRepository;
import com.ivini.saidasjuntas.acesso.servico.infra.EnvioEmailService;
import com.ivini.saidasjuntas.acesso.util.DataSistemaHelper;
import com.ivini.saidasjuntas.acesso.util.UsuarioHelper;
import com.ivini.saidasjuntas.config.SenhaConfig;
import com.ivini.saidasjuntas.fixture.CargoFixture;
import com.ivini.saidasjuntas.fixture.CredenciaisFixture;
import com.ivini.saidasjuntas.fixture.UsuarioFixture;
import com.ivini.saidasjuntas.tag.TesteConstSaida;

@ActiveProfiles("test")
@SpringBootTest
class UsuarioServiceTest {

	private Usuario usuarioGravado;
	private AssociacaoDTO param;
	
	@Mock
	private UsuarioRepository repository;
	@Mock
	private EnvioEmailService envio;
	@Mock
	private TokenConfirmacaoRepository tokenRep;
	@Mock
	private SenhaConfig senhaConfig;
	@Mock
	private CargoRepository cargoRep;
	@Mock
	private FuncionalidadeRepository funcionalidadeRep;

	@InjectMocks
	private UsuarioService service;
	
	@BeforeEach
	void setUp(TestInfo info) {
		usuarioGravado = UsuarioFixture.configurarRepositorioUsuarioEToken(info, senhaConfig, tokenRep, repository);
		param = CredenciaisFixture.criarAssociacao(info);
		CargoFixture.configurarRepositorio(info, cargoRep);
		// TokenConfirmacaoFixture.configurarRepositorio(info, tokenRep, senhaConfig);
		//FuncionalidadeFixture.configurarRepositorio(info, funcionalidadeRep);
	}

	@AfterEach
	void tearDown(TestInfo info) {
		UsuarioFixture.verificarRepositorioRegistrar(info, repository, tokenRep);
		CargoFixture.verificarRepositorio(info, cargoRep);
		// TokenConfirmacaoFixture.verificarRepositorio(info, tokenRep);
		//FuncionalidadeFixture.verificarRepositorio(info, funcionalidadeRep);
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID),
		@Tag(TesteConstSaida.BD_USUARIO_GRAVA),
		@Tag(TesteConstSaida.BD_TOKEN_NAO_ENCONTRADO_POR_USUARIO),
	})
	@Test
	void inativarUsuarioEncontrado() throws AbstractSaidasException {
		String res = service.inativar(usuarioGravado.getIdUsuario());
		assertThat(res).isEqualTo(DataSistemaHelper.formatarData(LocalDate.now()));
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_ID),
	})
	@Test
	void inativarUsuarioNaoEncontrado() throws AbstractSaidasException {
		assertThrows(UsuarioNaoEncontradoException.class, () -> {
			service.inativar(CredenciaisFixture.CODIGO_USUARIO_INEXISTENTE);
		});
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID),
		@Tag(TesteConstSaida.BD_USUARIO_GRAVA),
		@Tag(TesteConstSaida.BD_TOKEN_NAO_ENCONTRADO_POR_USUARIO),
	})
	@Test
	void reativarUsuarioEncontrado() throws AbstractSaidasException {
		assertThat(service.reativar(usuarioGravado.getIdUsuario())).isEmpty();
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_ID),
	})
	@Test
	void reativarUsuarioNaoEncontrado() throws AbstractSaidasException {
		assertThrows(UsuarioNaoEncontradoException.class, () -> {
			service.reativar(CredenciaisFixture.CODIGO_USUARIO_INEXISTENTE);
		});
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID),
		@Tag(TesteConstSaida.BD_USUARIO_GRAVA),
		@Tag(TesteConstSaida.BD_TOKEN_NAO_ENCONTRADO_POR_USUARIO),
	})
	@Test
	void suspenderUsuarioEncontrado() throws AbstractSaidasException {
		String res = service.suspender(usuarioGravado.getIdUsuario());
		assertThat(res).isEqualTo(DataSistemaHelper.formatarData(LocalDate.now().plusDays(UsuarioHelper.DIAS_SUSPENSAO)));
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_ID),
	})
	@Test
	void suspenderUsuarioNaoEncontrado() throws AbstractSaidasException {
		assertThrows(UsuarioNaoEncontradoException.class, () -> {
			service.suspender(CredenciaisFixture.CODIGO_USUARIO_INEXISTENTE);
		});
	}
	
	@Test
	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID),
		@Tag(TesteConstSaida.BD_TOKEN_NAO_ENCONTRADO_POR_USUARIO),
	})
	void acessoNull() throws AbstractSaidasException {
		List<String> acessos = service.temQualDessesAcessos("", null);
		assertThat(acessos).isEmpty();
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID),
		@Tag(TesteConstSaida.BD_USUARIO_CARGO_NULO),
		@Tag(TesteConstSaida.BD_TOKEN_NAO_ENCONTRADO_POR_USUARIO),
	})
	@Test
	void acessoCargoNull() throws AbstractSaidasException {
		List<String> acessos = service.temQualDessesAcessos(usuarioGravado.getIdUsuario(),
				Arrays.asList("teste:qualquer"));
		assertThat(acessos).isEmpty();
		assertThat(usuarioGravado.getCargos()).isNull();
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_ID),
	})
	@Test
	void associarUsuarioNaoEncontrado() throws Exception {
		assertThrows(UsuarioNaoEncontradoException.class, () -> service.associarColaborador(param));
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_ID),
	})
	@Test
	void desassociarUsuarioNaoEncontrado() throws Exception {
		assertThrows(UsuarioNaoEncontradoException.class, () -> service.desassociarColaborador(param));
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID),
		@Tag(TesteConstSaida.BD_USUARIO_GERENTE_DIFERENTE),
	})
	@Test
	void associarColaboradorComGerenteDiferente() throws Exception {
		assertThrows(OutroResponsavelException.class, () -> service.associarColaborador(param));
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID),
		@Tag(TesteConstSaida.BD_USUARIO_GERENTE_NULO),
		@Tag(TesteConstSaida.BD_CARGO_TORNAR_COMERCIANTE),
		@Tag(TesteConstSaida.BD_CARGO_MEUS_CLIENTES),
	})
	@Test
	void associarColaboradorComGerenteNulo() throws Exception {
		String res = service.associarColaborador(param);
		assertThat(res).isEqualTo(usuarioGravado.getIdUsuario());
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID),
		@Tag(TesteConstSaida.BD_USUARIO_GERENTE_IGUAL),
		@Tag(TesteConstSaida.BD_CARGO_TORNAR_COMERCIANTE),
		@Tag(TesteConstSaida.BD_CARGO_MEUS_CLIENTES),
	})
	@Test
	void associarColaboradorComGerenteIgual() throws Exception {
		String res = service.associarColaborador(param);
		assertThat(res).isEqualTo(usuarioGravado.getIdUsuario());
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID),
		@Tag(TesteConstSaida.BD_USUARIO_GERENTE_DIFERENTE),
	})
	@Test
	void desassociarColaboradorComGerenteDiferente() throws Exception {
		assertThrows(OutroResponsavelException.class, () -> service.desassociarColaborador(param));
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID),
		@Tag(TesteConstSaida.BD_USUARIO_GERENTE_NULO),
		@Tag(TesteConstSaida.BD_CARGO_TORNAR_COMERCIANTE),
		@Tag(TesteConstSaida.BD_CARGO_MEUS_CLIENTES),
	})
	@Test
	void desassociarColaboradorComGerenteNulo() throws Exception {
		String res = service.desassociarColaborador(param);
		assertThat(res).isEqualTo(usuarioGravado.getIdUsuario());
	}
	
	@Tags({
		@Tag(TesteConstSaida.BD_TOKEN_NAO_ENCONTRADO_POR_TOKEN)
	})
	@Test
	void confirmarTokenMasEleNaoExiste() throws Exception {
		assertThrows(TokenNaoEncontradoException.class, () -> service.confirmarUsuario("123"));
	}
	
	@Tags({
		@Tag(TesteConstSaida.BD_TOKEN_ENCONTRADO_POR_TOKEN)
	})
	@Test
	void confirmarTokenLiberaUsuario() throws Exception {
		assertThat(service.confirmarUsuario("123")).isNotNull();
	}

	// TODO COLOCAR OS TESTES UNITÁRIO AQUI.
}
