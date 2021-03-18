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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ivini.saidasjuntas.acesso.dto.CadastroUsuarioDTO;
import com.ivini.saidasjuntas.acesso.excecao.tipos.EnvioEmailException;
import com.ivini.saidasjuntas.acesso.repositorio.CargoRepository;
import com.ivini.saidasjuntas.acesso.repositorio.FuncionalidadeRepository;
import com.ivini.saidasjuntas.acesso.repositorio.TokenConfirmacaoRepository;
import com.ivini.saidasjuntas.acesso.repositorio.UsuarioRepository;
import com.ivini.saidasjuntas.acesso.servico.infra.EnvioEmailService;
import com.ivini.saidasjuntas.config.SenhaConfig;
import com.ivini.saidasjuntas.fixture.EnvioEmailFixture;
import com.ivini.saidasjuntas.fixture.JsonFixture;
import com.ivini.saidasjuntas.fixture.UsuarioDTOFixture;
import com.ivini.saidasjuntas.fixture.UsuarioFixture;
import com.ivini.saidasjuntas.tag.TesteConstSaida;

@AutoConfigureMockMvc
class AbertoRegistrarIT extends BaseController {
	private static final String URL_REGISTRAR = "/api/v1/aberto/registrar";

	@Autowired
	private SenhaConfig senhaConfig;
	@MockBean
	private UsuarioRepository usuarioRep;
	@MockBean
	private CargoRepository cargoRep;
	@MockBean
	private FuncionalidadeRepository funcionalidadeRep;
	@MockBean
	private EnvioEmailService envio;
	@MockBean
	private TokenConfirmacaoRepository tokenRep;

	private String jsonParam;

	@BeforeEach
	void setUp(TestInfo info) throws JsonProcessingException, EnvioEmailException {
		CadastroUsuarioDTO param = UsuarioDTOFixture.criar(info);
		UsuarioFixture.configurarRepositorioRegistrarUsuario(info, usuarioRep, tokenRep, senhaConfig, param);
		EnvioEmailFixture.configurarRepositorioUsuario(info, envio, param);
		//TokenConfirmacaoFixture.configurarRepositorio(info, tokenRep, senhaConfig);
		//CargoFixture.configurarRepositorio(info, cargoRep, funcionalidadeRep);
		jsonParam = JsonFixture.toJson(param);
	}

	@AfterEach
	void tearDown(TestInfo info) throws EnvioEmailException {
		UsuarioFixture.verificarRepositorioRegistrar(info, usuarioRep, tokenRep);
		//TokenConfirmacaoFixture.verificarRepositorio(info, tokenRep);
		//CargoFixture.verificarRepositorio(info, cargoRep,  funcionalidadeRep);
		EnvioEmailFixture.verificarRepositorio(info, envio);
	}

	@Tags({
		@Tag(TesteConstSaida.VALIDA_CONFIRMACAO_SENHA_ERRADA),
		@Tag(TesteConstSaida.VALIDA_EMAIL_DOMINIO_CURTO),
		@Tag(TesteConstSaida.VALIDA_NOME_VAZIO),
		@Tag(TesteConstSaida.VALIDA_SENHA_CURTA),
	})
	@Test
	void registrarDadosTodosErrados() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(URL_REGISTRAR)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(jsonParam))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder("nome", "email", "senha", "confirmacaoSenha")))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(
				"Eu preciso que você me diga o seu nome", 
				"O endereço de e-mail é inválido", 
				"Deve ter no mínimo 6 letras", 
				"A senha está diferente")));
	}

	/**
	 * Cadastro de um e-mail que já existe.
	 * @throws Exception
	 */
	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_EXISTE_POR_EMAIL),
	})
	@Test
	void registrarEmailJaCadastrado() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(URL_REGISTRAR)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(jsonParam))
		.andExpect(status().isConflict())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder("jaExiste")))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(
				"O usuário ze-silva_teste@meio.com já existe no sistema")));
	}

	/**
	 * Falha de um módulo: envio de e-mail.
	 * @throws Exception
	 */
	@Tags({
		@Tag(TesteConstSaida.INFRA_ERRO_ENVIAR_EMAIL),
		@Tag(TesteConstSaida.BD_TOKEN_ENCONTRADO_POR_TOKEN),
	})
	@Test
	void registrarJaTemTokenMasErroEnviarEmail() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(URL_REGISTRAR)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(jsonParam))
		.andExpect(status().isFailedDependency())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder("falha")))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(
				"Erro enviando e-mail para ze-silva_teste@meio.com")));
	}
	
	@Test
	void registrarSemTokenEmailEnviado() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(URL_REGISTRAR)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(jsonParam))
		.andExpect(status().isCreated())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_SUCESSO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(
				RESULTADO_SUCESSO)));
	}

	@Tags({
		@Tag(TesteConstSaida.BD_CARGO_PADRAO_EXISTE)
	})
	@Test
	void registrarCargoPadraoJaExiste() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(URL_REGISTRAR)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(jsonParam))
		.andExpect(status().isCreated())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_SUCESSO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(
				RESULTADO_SUCESSO)));
	}

	@Tags({
		@Tag(TesteConstSaida.BD_CARGO_PADRAO_EXISTE),
		@Tag(TesteConstSaida.BD_FUNCIONALIDADE_AVALIAR_EVENTO),
		@Tag(TesteConstSaida.BD_FUNCIONALIDADE_AVALIAR_PARTICIPANTE),
		@Tag(TesteConstSaida.BD_FUNCIONALIDADE_DENUNCIAR_EVENTO),
		@Tag(TesteConstSaida.BD_FUNCIONALIDADE_DENUNCIAR_PARTICIPANTE),
		@Tag(TesteConstSaida.BD_FUNCIONALIDADE_EDITAR_DOCUMENTOS),
		@Tag(TesteConstSaida.BD_FUNCIONALIDADE_ORGANIZAR_EVENTO),
		@Tag(TesteConstSaida.BD_FUNCIONALIDADE_PARTICIPAR_EVENTO),
	})
	@Test
	void registrarCargoPadraoJaExisteFuncionalidadesTambem() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(URL_REGISTRAR)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(jsonParam))
		.andExpect(status().isCreated())
		.andExpect(jsonPath(RETORNO_MENSAGENS_CHAVE, Matchers.containsInAnyOrder(CHAVE_SUCESSO)))
		.andExpect(jsonPath(RETORNO_MENSAGENS_DESCRICAO, Matchers.containsInAnyOrder(
				RESULTADO_SUCESSO)));
	}

}
