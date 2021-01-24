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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ivini.saidasjuntas.acesso.dto.CredenciaisDTO;
import com.ivini.saidasjuntas.acesso.repositorio.TokenConfirmacaoRepository;
import com.ivini.saidasjuntas.acesso.repositorio.UsuarioRepository;
import com.ivini.saidasjuntas.config.SenhaConfig;
import com.ivini.saidasjuntas.fixture.CredenciaisFixture;
import com.ivini.saidasjuntas.fixture.JsonFixture;
import com.ivini.saidasjuntas.fixture.UsuarioFixture;
import com.ivini.saidasjuntas.tag.TesteConstSaida;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AbertoLoginTest extends BaseController {

	private static final String URL_LOGIN = "/api/v1/aberto/entrar";
	private String jsonParam;

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private SenhaConfig senhaConfig;
	@MockBean
	private UsuarioRepository usuarioRep;
	@MockBean
	private TokenConfirmacaoRepository tokenRep;

	@BeforeEach
	void setUp(TestInfo info) throws JsonProcessingException {
		CredenciaisDTO param = CredenciaisFixture.criar(info);
		UsuarioFixture.configurarRepositorioUsuarioEToken(info, senhaConfig, tokenRep, usuarioRep);
		jsonParam = JsonFixture.toJson(param);
	}

	@AfterEach
	void tearDown(TestInfo info) {
		UsuarioFixture.verificarRepositorio(info, usuarioRep, tokenRep);
	}

	@Tags({
		@Tag(TesteConstSaida.BD_TOKEN_ERRO_FATAL),
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_EMAIL),
	})
	@Test
	void loginErroFatal() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(URL_LOGIN)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(jsonParam))
		//.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isInternalServerError())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_ERRO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(
				"Teste de erro lendo o token")));

	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_EMAIL),
	})
	@Test
	void loginUsuarioNaoEncontrado() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(URL_LOGIN)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(jsonParam))
		//.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isNotFound())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_NAO_ENCONTRADO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(RESULTADO_EMAIL_NAO_ENCONTRADO)));
	}
	
	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_EMAIL),
		@Tag(TesteConstSaida.BD_TOKEN_ENCONTRADO_POR_USUARIO),
	})
	@Test
	void loginUsuarioNaoValidouToken() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(URL_LOGIN)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(jsonParam))
		//.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isUnavailableForLegalReasons())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_CONTRATO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(RESULTADO_EMAIL_NAO_CONFIRMADO)));
	}
	
	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_EMAIL),
		@Tag(TesteConstSaida.BD_TOKEN_NAO_ENCONTRADO_POR_USUARIO),
		@Tag(TesteConstSaida.BD_USUARIO_INATIVO),
	})
	@Test
	void loginUsuarioInativo() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(URL_LOGIN)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(jsonParam))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isUnavailableForLegalReasons())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_CONTRATO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(RESULTADO_EMAIL_INATIVO)));
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_EMAIL),
		@Tag(TesteConstSaida.BD_TOKEN_NAO_ENCONTRADO_POR_USUARIO),
		@Tag(TesteConstSaida.BD_USUARIO_SUSPENSO_ATE_HOJE),
	})
	@Test
	void loginUsuarioSuspensoAteHoje() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(URL_LOGIN)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(jsonParam))
		//.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isUnavailableForLegalReasons())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_CONTRATO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(RESULTADO_EMAIL_SUSPENSO)));
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_EMAIL),
		@Tag(TesteConstSaida.BD_TOKEN_NAO_ENCONTRADO_POR_USUARIO),
		@Tag(TesteConstSaida.BD_USUARIO_SUSPENSO_ATE_ONTEM),
		@Tag(TesteConstSaida.BD_USUARIO_SENHA_ERRADA),
	})
	@Test
	void loginUsuarioPassouSuspensaoSenhaInvalida() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(URL_LOGIN)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(jsonParam))
		//.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isNotFound())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_NAO_ENCONTRADO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(RESULTADO_EMAIL_NAO_ENCONTRADO)));
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_EMAIL),
		@Tag(TesteConstSaida.BD_TOKEN_NAO_ENCONTRADO_POR_USUARIO),
	})
	@Test
	void loginUsuarioSenhaCerta() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(URL_LOGIN)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(jsonParam))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isOk())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_SUCESSO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(RESULTADO_SUCESSO)));
	}
}
