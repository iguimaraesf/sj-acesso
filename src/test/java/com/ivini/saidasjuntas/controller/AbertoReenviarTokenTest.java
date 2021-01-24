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
import com.ivini.saidasjuntas.acesso.dto.CredenciaisDTO;
import com.ivini.saidasjuntas.acesso.excecao.tipos.EnvioEmailException;
import com.ivini.saidasjuntas.acesso.repositorio.TokenConfirmacaoRepository;
import com.ivini.saidasjuntas.acesso.repositorio.UsuarioRepository;
import com.ivini.saidasjuntas.acesso.servico.infra.EnvioEmailService;
import com.ivini.saidasjuntas.config.SenhaConfig;
import com.ivini.saidasjuntas.fixture.CredenciaisFixture;
import com.ivini.saidasjuntas.fixture.EnvioEmailFixture;
import com.ivini.saidasjuntas.fixture.JsonFixture;
import com.ivini.saidasjuntas.fixture.TokenConfirmacaoFixture;
import com.ivini.saidasjuntas.fixture.UsuarioFixture;
import com.ivini.saidasjuntas.tag.TesteConstSaida;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AbertoReenviarTokenTest extends BaseController {
	private static final String URL_REENVIAR = "/api/v1/aberto/reenviar";
	private String jsonParam;

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private SenhaConfig senhaConfig;
	@MockBean
	private TokenConfirmacaoRepository tokenRep;
	@MockBean
	private UsuarioRepository usuarioRep;
	@MockBean
	private EnvioEmailService envio;

	@BeforeEach
	void setUp(TestInfo info) throws JsonProcessingException, EnvioEmailException {
		TokenConfirmacaoFixture.configurarRepositorio(info, tokenRep, senhaConfig);
		CredenciaisDTO cred = CredenciaisFixture.criar(info);
		EnvioEmailFixture.configurarRepositorioCredenciais(info, envio, cred);
		UsuarioFixture.configurarRepositorioCredenciais(info, usuarioRep, senhaConfig, cred);
		jsonParam = JsonFixture.toJson(cred);
	}

	@AfterEach
	void tearDown(TestInfo info) throws EnvioEmailException {
		TokenConfirmacaoFixture.verificarRepositorio(info, tokenRep);
		EnvioEmailFixture.verificarRepositorio(info, envio);
		UsuarioFixture.verificarRepositorio(info, usuarioRep, tokenRep);
	}

	@Tags({
		@Tag(TesteConstSaida.BD_TOKEN_ERRO_FATAL),
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_EMAIL),
	})
	@Test
	void reenviarTokenErroBuscaUsuario() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(URL_REENVIAR)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(jsonParam))
		.andExpect(status().isInternalServerError())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_ERRO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(
				"Teste dando erro inesperado ao buscar por ID de usuário")));
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_EMAIL),
	})
	@Test
	void reenviarTokenUsuarioNaoEncontrado() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(URL_REENVIAR)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(jsonParam))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_NAO_ENCONTRADO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(RESULTADO_EMAIL_NAO_ENCONTRADO)));
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_EMAIL),
		@Tag(TesteConstSaida.BD_TOKEN_ENCONTRADO_POR_TOKEN),
	})
	@Test
	void reenviarTokenQuandoTokenJaExiste() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(URL_REENVIAR)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(jsonParam))
		.andExpect(status().isOk())
		//.andDo(MockMvcResultHandlers.print())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_SUCESSO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(RESULTADO_SUCESSO)));
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_EMAIL),
		@Tag(TesteConstSaida.BD_TOKEN_NAO_ENCONTRADO_POR_TOKEN),
	})
	@Test
	void reenviarTokenQuandoTokenNaoExisteMais() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(URL_REENVIAR)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(jsonParam))
		.andExpect(status().isOk())
		//.andDo(MockMvcResultHandlers.print())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_SUCESSO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(RESULTADO_SUCESSO)));
	}

}
