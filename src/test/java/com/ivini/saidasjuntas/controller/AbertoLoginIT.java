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
import com.ivini.saidasjuntas.acesso.dto.CredenciaisDTO;
import com.ivini.saidasjuntas.acesso.repositorio.TokenConfirmacaoRepository;
import com.ivini.saidasjuntas.acesso.repositorio.UsuarioRepository;
import com.ivini.saidasjuntas.config.SenhaConfig;
import com.ivini.saidasjuntas.fixture.CredenciaisFixture;
import com.ivini.saidasjuntas.fixture.JsonFixture;
import com.ivini.saidasjuntas.fixture.UsuarioFixture;
import com.ivini.saidasjuntas.tag.TesteConstSaida;

@AutoConfigureMockMvc
class AbertoLoginIT extends BaseController {

	private static final String URL_LOGIN = "/api/v1/aberto/entrar";
	private String jsonParam;

	@Autowired
	private SenhaConfig senhaConfig;
	@MockBean
	private UsuarioRepository usuarioRep;
	@MockBean
	private TokenConfirmacaoRepository tokenRep;

	@BeforeEach
	void setUp(TestInfo info) throws JsonProcessingException {
		UsuarioFixture.configurarRepositorioUsuarioEToken(info, senhaConfig, tokenRep, usuarioRep);
		CredenciaisDTO param = CredenciaisFixture.criar(info);
		jsonParam = JsonFixture.toJson(param);
	}

	@AfterEach
	void tearDown(TestInfo info) {
		UsuarioFixture.verificarRepositorioRegistrar(info, usuarioRep, tokenRep);
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
