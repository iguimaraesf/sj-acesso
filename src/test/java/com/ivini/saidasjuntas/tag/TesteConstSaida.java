package com.ivini.saidasjuntas.tag;

public final class TesteConstSaida {

	public static final String BD_USUARIO_EXISTE_POR_EMAIL = "repository:usuario:existe";
	public static final String BD_USUARIO_NAO_EXISTE_POR_EMAIL = "repository:usuario:nao-existe";
	public static final String BD_USUARIO_GRAVA = "repository:usuario:grava";
	public static final String BD_USUARIO_ENCONTRADO_POR_EMAIL = "repository:usuario:encontrado:por-email";
	public static final String BD_USUARIO_ENCONTRADO_POR_ID = "repository:usuario:encontrado:por-id";
	public static final String BD_USUARIO_NAO_ENCONTRADO_POR_ID = "repository:usuario:nao-encontrado:por-id";
	public static final String BD_USUARIO_NAO_ENCONTRADO_POR_EMAIL = "repository:usuario:nao-encontrado";
	public static final String BD_USUARIO_INATIVO = "repository:usuario:inativo";
	public static final String BD_USUARIO_SUSPENSO_ATE_HOJE = "repository:usuario:suspenso:ate-hoje";
	public static final String BD_USUARIO_SUSPENSO_ATE_ONTEM = "repository:usuario:suspenso:ate-ontem";
	public static final String BD_USUARIO_SENHA_ERRADA = "repository:usuario:senha-errada";
	public static final String BD_USUARIO_GERENTE_DIFERENTE = "repository:usuario:gerente-diferente";
	public static final String BD_USUARIO_GERENTE_IGUAL = "repository:usuario:gerente-mesmo";
	public static final String BD_USUARIO_GERENTE_NULO = "repository:usuario:gerente-null";
	public static final String BD_USUARIO_CARGO_NULO = "repository:usuario:cargo:null";
	public static final String BD_USUARIO_EMPREGADOR_IGUAL = "repository:usuario:empregador-igual";
	public static final String BD_USUARIO_EMPREGADOR_NULO = "repository:usuario:empregador-null";
	public static final String BD_USUARIO_EMPREGADOR_DIFERENTE = "repository:usuario:empregador-diferente";
	public static final String BD_USUARIO_CENTRAL_IGUAL = "repository:usuario:central-seguranca-igual";
	public static final String BD_USUARIO_CENTRAL_NULO = "repository:usuario:central-seguranca-null";
	public static final String BD_USUARIO_CENTRAL_DIFERENTE = "repository:usuario:central-seguranca-diferente";
	public static final String BD_USUARIO_LISTA_ERRO = "repository:usuario:listar:erro";
	public static final String BD_USUARIO_LISTA_VAZIA = "repository:usuario:listar:vazio";
	public static final String BD_USUARIO_LISTA_TUDO = "repository:usuario:listar:tudo";
	public static final String BD_USUARIO_LISTA_PRIMEIRA_PAGINA = "repository:usuario:listar:pag1";
	public static final String BD_USUARIO_LISTA_SEGUNDA_PAGINA = "repository:usuario:listar:pag2";
	public static final String BD_TOKEN_GRAVA = "repository:token:grava";
	public static final String BD_TOKEN_ENCONTRADO_POR_USUARIO = "repository:token:existe";
	public static final String BD_TOKEN_NAO_ENCONTRADO_POR_USUARIO = "repository:token:nao-existe";
	public static final String BD_TOKEN_ENCONTRADO_POR_TOKEN = "repository:token:confirmar-token:existe";
	public static final String BD_TOKEN_NAO_ENCONTRADO_POR_TOKEN = "repository:token:confirmar-token:nao-existe";
	public static final String INFRA_CODIFICA_SENHA = "infra:codifica-senha";
	public static final String INFRA_ERRO_ENVIAR_EMAIL = "infra:enviar-email:erro";
	public static final String SVC_CARGO_PADRAO_NENHUM = "servico:cargo:padrao:nenhum";
	@Deprecated
	public static final String BD_FUNCIONALIDADE_PARTICIPAR_EVENTO = "repository:funcionalidade:participar-evento";
	@Deprecated
	public static final String BD_FUNCIONALIDADE_AVALIAR_EVENTO = "repository:funcionalidade:avaliar-evento";
	@Deprecated
	public static final String BD_FUNCIONALIDADE_AVALIAR_PARTICIPANTE = "repository:funcionalidade:avaliar-participante";
	@Deprecated
	public static final String BD_FUNCIONALIDADE_ORGANIZAR_EVENTO = "repository:funcionalidade:organizar-evento";
	@Deprecated
	public static final String BD_FUNCIONALIDADE_DENUNCIAR_EVENTO = "repository:funcionalidade:denunciar-evento";
	@Deprecated
	public static final String BD_FUNCIONALIDADE_DENUNCIAR_PARTICIPANTE = "repository:funcionalidade:denunciar-participante";
	@Deprecated
	public static final String BD_FUNCIONALIDADE_EDITAR_DOCUMENTOS = "modelo:funcionalidade:editar-documentos";

	public static final String MD_CARGO_VENDEDOR = "modelo:cargo:cargo-vendedor";
	public static final String VALIDA_NOME_VAZIO = "validacao:nome-vazio";
	public static final String VALIDA_SENHA_CURTA = "validacao:senha-curta";
	public static final String VALIDA_CONFIRMACAO_SENHA_ERRADA = "validacao:confirmacao-senha-errada";
	public static final String VALIDA_EMAIL_SEM_ARROBA = "validacao:email:sem-arroba";
	public static final String VALIDA_EMAIL_SOMENTE_DOMINIO = "validacao:email:so-dominio";
	public static final String VALIDA_EMAIL_DOMINIO_CURTO = "validacao:email:dominio-curto";
	@Deprecated
	public static final String BD_CARGO_PADRAO_EXISTE = "repository:cargo:padrao-existe";
	@Deprecated
	public static final String BD_CARGO_PADRAO = "repository:cargo:cargo-padrao";
	public static final String BD_CARGO_TORNAR_COMERCIANTE = "repository:cargo:tornar-comerciante";
	public static final String BD_CARGO_MEUS_CLIENTES = "repository:cargo:meus-clientes";
	public static final String BD_CARGO_VALIDAR_CUPOM = "repository:cargo:validar-cupom";
	public static final String BD_TOKEN_ERRO_FATAL = "repository:token:erro-fatal";

}
