package com.ivini.saidasjuntas.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

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
import com.ivini.saidasjuntas.acesso.repositorio.UsuarioRepository;
import com.ivini.saidasjuntas.acesso.util.DataSistemaHelper;
import com.ivini.saidasjuntas.acesso.util.UsuarioHelper;
import com.ivini.saidasjuntas.config.SenhaConfig;
import com.ivini.saidasjuntas.fixture.UsuarioFixture;
import com.ivini.saidasjuntas.tag.TesteConstSaida;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AdminInativarReativarSuspenderTest extends BaseController {
	private static final String NAO_ENCONTRADO = "O usuário 123abc não foi encontrado";
	private static final String URL_INATIVAR = "/api/v1/admin/inativar/123abc";
	private static final String URL_REATIVAR = "/api/v1/admin/reativar/123abc";
	private static final String URL_SUSPENDER = "/api/v1/admin/suspender/123abc";

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private SenhaConfig senhaConfig;
	@MockBean
	private UsuarioRepository usuarioRep;

	@BeforeEach
	void setUp(TestInfo info) throws JsonProcessingException {
		UsuarioFixture.configurarRepositorioUsuarioEToken(info, senhaConfig, null, usuarioRep);
	}
	
	@AfterEach
	void tearDown(TestInfo info) {
		UsuarioFixture.verificarRepositorio(info, usuarioRep, null);
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_ID)
	})
	@Test
	void inativarChaveInexistente() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.patch(URL_INATIVAR)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_NAO_ENCONTRADO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(
				NAO_ENCONTRADO)));

	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_ID)
	})
	@Test
	void reativarChaveInexistente() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.patch(URL_REATIVAR)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_NAO_ENCONTRADO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(
				NAO_ENCONTRADO)));

	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_ID)
	})
	@Test
	void suspenderChaveInexistente() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.patch(URL_SUSPENDER)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_NAO_ENCONTRADO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(
				NAO_ENCONTRADO)));

	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID)
	})
	@Test
	void inativarChaveExistente() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.patch(URL_INATIVAR)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andExpect(jsonPath(RETORNO_COMPLEMENTO_OBJETO, Matchers.equalTo(DataSistemaHelper.formatarData(LocalDate.now()))))
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_SUCESSO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(
				RESULTADO_SUCESSO)));

	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID)
	})
	@Test
	void reativarChaveExistente() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.patch(URL_REATIVAR)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andExpect(jsonPath(RETORNO_COMPLEMENTO_OBJETO, Matchers.equalTo("")))
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_SUCESSO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(
				RESULTADO_SUCESSO)));

	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID)
	})
	@Test
	void suspenderChaveExistente() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.patch(URL_SUSPENDER)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andExpect(jsonPath(RETORNO_COMPLEMENTO_OBJETO, Matchers.equalTo(DataSistemaHelper.formatarData(LocalDate.now().plusDays(UsuarioHelper.DIAS_SUSPENSAO)))))
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_SUCESSO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(
				RESULTADO_SUCESSO)));

	}
}
