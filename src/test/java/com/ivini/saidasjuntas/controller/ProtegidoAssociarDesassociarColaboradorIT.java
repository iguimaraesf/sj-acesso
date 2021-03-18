package com.ivini.saidasjuntas.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ivini.saidasjuntas.acesso.dto.AssociacaoDTO;
import com.ivini.saidasjuntas.acesso.repositorio.CargoRepository;
import com.ivini.saidasjuntas.acesso.repositorio.UsuarioRepository;
import com.ivini.saidasjuntas.config.SenhaConfig;
import com.ivini.saidasjuntas.fixture.CargoFixture;
import com.ivini.saidasjuntas.fixture.CredenciaisFixture;
import com.ivini.saidasjuntas.fixture.JsonFixture;
import com.ivini.saidasjuntas.fixture.UsuarioFixture;
import com.ivini.saidasjuntas.tag.TesteConstSaida;

@AutoConfigureMockMvc
class ProtegidoAssociarDesassociarColaboradorIT extends BaseController {
	private static final String URL_ASSOCIAR_COLABORADOR = "/api/v1/prot/usuario/associar/colaborador";
	private static final String URL_ASSOCIAR_SEGURANCA = "/api/v1/prot/usuario/associar/seguranca";
	private static final String URL_ASSOCIAR_FUNCIONARIO = "/api/v1/prot/usuario/associar/funcionario";
	private static final String URL_DESASSOCIAR_COLABORADOR = "/api/v1/prot/usuario/desassociar/colaborador";
	private static final String URL_DESASSOCIAR_SEGURANCA = "/api/v1/prot/usuario/desassociar/seguranca";
	private static final String URL_DESASSOCIAR_FUNCIONARIO = "/api/v1/prot/usuario/desassociar/funcionario";

	private AssociacaoDTO associacao;
	private String json;
	@MockBean
	private UsuarioRepository usuarioRep;
	@MockBean
	private CargoRepository cargoRep;
	@Autowired
	private SenhaConfig senhaConfig;

	@BeforeEach
	void setUp(TestInfo info) throws JsonProcessingException {
		UsuarioFixture.configurarRepositorioUsuarioEToken(info, senhaConfig, null, usuarioRep);
		CargoFixture.configurarRepositorio(info, cargoRep);
		associacao = CredenciaisFixture.criarAssociacao(info);
		json = JsonFixture.toJson(associacao);
	}
	
	@AfterEach
	void tearDown(TestInfo info) {
		UsuarioFixture.verificarRepositorioRegistrar(info, usuarioRep, null);
		CargoFixture.verificarRepositorio(info, cargoRep);
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_ID),
		@Tag(TesteConstSaida.BD_USUARIO_GERENTE_DIFERENTE),
	})
	@Test
	void associarSegurancaNaoEncontrado() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.patch(URL_ASSOCIAR_SEGURANCA)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder("naoEncontrado")))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(
				"O usuário " + CredenciaisFixture.CODIGO_USUARIO_INEXISTENTE + " não foi encontrado")));
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_ID),
		@Tag(TesteConstSaida.BD_USUARIO_GERENTE_DIFERENTE),
	})
	@Test
	void desassociarSegurancaNaoEncontrado() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.patch(URL_DESASSOCIAR_SEGURANCA)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder("naoEncontrado")))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(
				"O usuário " + CredenciaisFixture.CODIGO_USUARIO_INEXISTENTE + " não foi encontrado")));
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID),
		@Tag(TesteConstSaida.BD_USUARIO_GERENTE_NULO),
		@Tag(TesteConstSaida.BD_CARGO_TORNAR_COMERCIANTE),
		@Tag(TesteConstSaida.BD_CARGO_MEUS_CLIENTES),
	})
	@Test
	void desassociarColaboradorComGerenteNulo() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.patch(URL_DESASSOCIAR_COLABORADOR)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isOk())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_SUCESSO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(RESULTADO_SUCESSO)));
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID),
		@Tag(TesteConstSaida.BD_USUARIO_GERENTE_IGUAL),
		@Tag(TesteConstSaida.BD_CARGO_TORNAR_COMERCIANTE),
		@Tag(TesteConstSaida.BD_CARGO_MEUS_CLIENTES),
	})
	@Test
	void associarColaboradorComGerenteIgual() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.patch(URL_ASSOCIAR_COLABORADOR)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isOk())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_SUCESSO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(RESULTADO_SUCESSO)));
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID),
		@Tag(TesteConstSaida.BD_USUARIO_EMPREGADOR_IGUAL),
		@Tag(TesteConstSaida.BD_CARGO_VALIDAR_CUPOM),
	})
	@Test
	void associarFuncionarioComEmpregadorIgual() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.patch(URL_ASSOCIAR_FUNCIONARIO)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isOk())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_SUCESSO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(RESULTADO_SUCESSO)));
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID),
		@Tag(TesteConstSaida.BD_USUARIO_EMPREGADOR_IGUAL),
		@Tag(TesteConstSaida.BD_CARGO_VALIDAR_CUPOM),
	})
	@Test
	void desassociarFuncionarioComEmpregadorIgual() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.patch(URL_DESASSOCIAR_FUNCIONARIO)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isOk())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_SUCESSO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(RESULTADO_SUCESSO)));
	}
}
