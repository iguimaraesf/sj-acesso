package com.ivini.saidasjuntas.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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
import com.ivini.saidasjuntas.acesso.dto.DetalhesUsuarioDTO;
import com.ivini.saidasjuntas.acesso.repositorio.TokenConfirmacaoRepository;
import com.ivini.saidasjuntas.acesso.repositorio.UsuarioRepository;
import com.ivini.saidasjuntas.config.SenhaConfig;
import com.ivini.saidasjuntas.fixture.UsuarioDTOFixture;
import com.ivini.saidasjuntas.fixture.UsuarioFixture;
import com.ivini.saidasjuntas.tag.TesteConstSaida;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AdminListarTest extends BaseController {
	private final static String URL_LISTAR_TUDO = "/api/v1/admin/listar";
	private final static String URL_LISTAR_PAG1 = "/api/v1/admin/listar/1/2";
	private final static String URL_LISTAR_PAG2 = "/api/v1/admin/listar/2/2";
	private static final String RETORNO_COMPLEMENTO_NOME0 = "$.complemento.content[0].nome";
	private static final String RETORNO_COMPLEMENTO_NOME1 = "$.complemento.content[1].nome";
	private static final String RETORNO_COMPLEMENTO_NOME2 = "$.complemento.content[2].nome";
	private static final String RETORNO_COMPLEMENTO_LISTA = "$.complemento.content";
	private List<DetalhesUsuarioDTO> lista;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private SenhaConfig senhaConfig;
	@MockBean
	private TokenConfirmacaoRepository tokenRep;
	@MockBean
	private UsuarioRepository usuarioRep;

	@BeforeEach
	void setUp(TestInfo info) throws JsonProcessingException {
		UsuarioFixture.configurarRepositorioUsuarioEToken(info, senhaConfig, tokenRep, usuarioRep);
		UsuarioFixture.configurarRepositorioUsuarioListar(info, tokenRep, usuarioRep, senhaConfig);
		lista = UsuarioDTOFixture.listaComOs3Registros(info, senhaConfig);
	}
	
	@AfterEach
	void tearDown(TestInfo info) {
		UsuarioFixture.verificarRepositorio(info, usuarioRep, tokenRep);
	}

	@Tags({
		@Tag(TesteConstSaida.BD_TOKEN_ENCONTRADO_POR_USUARIO),
		@Tag(TesteConstSaida.BD_USUARIO_LISTA_ERRO),
	})
	@Test
	void listarQuandoDaErro() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(URL_LISTAR_TUDO)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isInternalServerError())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_ERRO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(
				"Erro de teste ao listar")));
	}
	
	@Tags({
		@Tag(TesteConstSaida.BD_TOKEN_ENCONTRADO_POR_USUARIO),
		@Tag(TesteConstSaida.BD_USUARIO_LISTA_VAZIA),
	})
	@Test
	void listarQuandoEstaVazio() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(URL_LISTAR_TUDO)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isOk())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_SUCESSO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(
				RESULTADO_SUCESSO)))
		.andExpect(jsonPath(RETORNO_COMPLEMENTO_LISTA, Matchers.empty()));
	}
	
	@Tags({
		@Tag(TesteConstSaida.BD_TOKEN_ENCONTRADO_POR_USUARIO),
		@Tag(TesteConstSaida.BD_USUARIO_LISTA_PRIMEIRA_PAGINA),
	})
	@Test
	void listarPagina1Com2RegistrosPorPagina() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(URL_LISTAR_PAG1)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isOk())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_SUCESSO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(
				RESULTADO_SUCESSO)))
		.andExpect(jsonPath(RETORNO_COMPLEMENTO_NOME0, Matchers.equalTo(lista.get(0).getNome())))
		.andExpect(jsonPath(RETORNO_COMPLEMENTO_NOME1, Matchers.equalTo(lista.get(1).getNome())));
	}

	@Tags({
		@Tag(TesteConstSaida.BD_TOKEN_ENCONTRADO_POR_USUARIO),
		@Tag(TesteConstSaida.BD_USUARIO_LISTA_SEGUNDA_PAGINA),
	})
	@Test
	void listarPagina2Com2RegistrosPorPagina() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(URL_LISTAR_PAG2)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isOk())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_SUCESSO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(
				RESULTADO_SUCESSO)))
		.andExpect(jsonPath(RETORNO_COMPLEMENTO_NOME0, Matchers.equalTo(lista.get(2).getNome())));
	}

	@Tags({
		@Tag(TesteConstSaida.BD_TOKEN_ENCONTRADO_POR_USUARIO),
		@Tag(TesteConstSaida.BD_USUARIO_LISTA_TUDO),
	})
	@Test
	void listarPagina1Com10RegistrosPorPagina() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(URL_LISTAR_TUDO)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isOk())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_SUCESSO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(
				RESULTADO_SUCESSO)))
		.andExpect(jsonPath(RETORNO_COMPLEMENTO_NOME0, Matchers.equalTo(lista.get(0).getNome())))
		.andExpect(jsonPath(RETORNO_COMPLEMENTO_NOME1, Matchers.equalTo(lista.get(1).getNome())))
		.andExpect(jsonPath(RETORNO_COMPLEMENTO_NOME2, Matchers.equalTo(lista.get(2).getNome())));
	}

}
