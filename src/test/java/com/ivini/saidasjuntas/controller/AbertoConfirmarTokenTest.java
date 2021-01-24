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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ivini.saidasjuntas.acesso.excecao.tipos.EnvioEmailException;
import com.ivini.saidasjuntas.acesso.repositorio.TokenConfirmacaoRepository;
import com.ivini.saidasjuntas.config.SenhaConfig;
import com.ivini.saidasjuntas.fixture.TokenConfirmacaoFixture;
import com.ivini.saidasjuntas.tag.TesteConstSaida;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AbertoConfirmarTokenTest extends BaseController {
	private static final String URL_CONFIRMAR = "/api/v1/aberto/confirmar/123";

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private SenhaConfig senhaConfig;
	@MockBean
	private TokenConfirmacaoRepository tokenRep;

	@BeforeEach
	void setUp(TestInfo info) throws JsonProcessingException, EnvioEmailException {
		TokenConfirmacaoFixture.configurarRepositorio(info, tokenRep, senhaConfig);
	}

	@AfterEach
	void tearDown(TestInfo info) throws EnvioEmailException {
		TokenConfirmacaoFixture.verificarRepositorio(info, tokenRep);
	}

	@Tags({
		@Tag(TesteConstSaida.BD_TOKEN_NAO_ENCONTRADO_POR_TOKEN)
	})
	@Test
	void confirmarTokenMasEleNaoExiste() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(URL_CONFIRMAR)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_NAO_ENCONTRADO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(
				"O token 123 já foi validado")));
	}
	
	@Tags({
		@Tag(TesteConstSaida.BD_TOKEN_ENCONTRADO_POR_TOKEN)
	})
	@Test
	void confirmarTokenLiberaUsuario() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(URL_CONFIRMAR)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_SUCESSO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(
				RESULTADO_SUCESSO)));
	}

	@Tags({
		@Tag(TesteConstSaida.BD_TOKEN_ERRO_FATAL)
	})
	@Test
	void confirmarTokenErroFatal() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(URL_CONFIRMAR)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isInternalServerError())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_ERRO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(
				"Teste dando erro inesperado ao procurar por token")));
	}

}
