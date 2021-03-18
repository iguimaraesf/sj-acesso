package com.ivini.saidasjuntas.config;

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
import com.ivini.saidasjuntas.acesso.dto.CredenciaisDTO;
import com.ivini.saidasjuntas.acesso.excecao.tipos.EnvioEmailException;
import com.ivini.saidasjuntas.acesso.repositorio.TokenConfirmacaoRepository;
import com.ivini.saidasjuntas.acesso.repositorio.UsuarioRepository;
import com.ivini.saidasjuntas.controller.BaseController;
import com.ivini.saidasjuntas.fixture.CredenciaisFixture;
import com.ivini.saidasjuntas.fixture.JsonFixture;
import com.ivini.saidasjuntas.fixture.UsuarioFixture;
import com.ivini.saidasjuntas.tag.TesteConstSaida;

@AutoConfigureMockMvc
class CentralExcecoesHandlerIT extends BaseController {
	private static final String URL_CONFIRMAR = "/api/v1/aberto/confirmar/123";
	private static final String URL_LOGIN = "/api/v1/aberto/entrar";
	private static final String URL_ASSOCIAR_COLABORADOR = "/api/v1/prot/usuario/associar/colaborador";
	private String jsonParam;
	private String jsonAssociacao;

	@Autowired
	private SenhaConfig senhaConfig;
	@MockBean
	private TokenConfirmacaoRepository tokenRep;
	@MockBean
	private UsuarioRepository usuarioRep;

	@BeforeEach
	void setUp(TestInfo info) throws JsonProcessingException, EnvioEmailException {
//		TokenConfirmacaoFixture.configurarRepositorio(info, tokenRep, senhaConfig);
		UsuarioFixture.configurarRepositorioUsuarioEToken(info, senhaConfig, tokenRep, usuarioRep);
		CredenciaisDTO param = CredenciaisFixture.criar(info);
		jsonParam = JsonFixture.toJson(param);
		AssociacaoDTO associacao = CredenciaisFixture.criarAssociacao(info);
		jsonAssociacao = JsonFixture.toJson(associacao);
	}

	@AfterEach
	void tearDown(TestInfo info) throws EnvioEmailException {
		//TokenConfirmacaoFixture.verificarRepositorio(info, tokenRep);
		UsuarioFixture.verificarRepositorioRegistrar(info, usuarioRep, tokenRep);
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
		@Tag(TesteConstSaida.BD_TOKEN_ERRO_FATAL),
		@Tag(TesteConstSaida.BD_TOKEN_ENCONTRADO_POR_USUARIO),
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
		@Tag(TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID),
		@Tag(TesteConstSaida.BD_USUARIO_GERENTE_DIFERENTE),
	})
	@Test
	void excecaoOutroResponsavel() throws Exception {
		//  + UsuarioFixture.ID_1111_2222_MARIA + "/" + UsuarioFixture.ID_1111_2224_ANTONIO + "/meu colaborador";
		mockMvc.perform(MockMvcRequestBuilders.patch(URL_ASSOCIAR_COLABORADOR)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(jsonAssociacao))
		.andExpect(status().isForbidden())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder("acesso")))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(
				"Esta operação não é permitida para o usuário " + UsuarioFixture.ID_XXX)));
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_ID),
	})
	@Test
	void excecaoDadosIncompletos() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.patch(URL_ASSOCIAR_COLABORADOR)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(jsonAssociacao))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder("idReferencia")))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(
				"O código da referência não foi informado")));
	}
}
