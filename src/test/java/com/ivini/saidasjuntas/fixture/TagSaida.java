package com.ivini.saidasjuntas.fixture;

import org.junit.jupiter.api.TestInfo;

public final class TagSaida {
	public static final String BD_USUARIO_EXISTE_POR_EMAIL = "repository:usuario:existe";
	public static final String BD_USUARIO_NAO_EXISTE_POR_EMAIL = "repository:usuario:nao-existe";
	public static final String BD_USUARIO_GRAVA = "repository:usuario:grava";
	public static final String BD_USUARIO_ENCONTRADO_POR_EMAIL = "repository:usuario:encontrado:por-email";
	public static final String BD_USUARIO_ENCONTRADO_POR_ID = "repository:usuario:encontrado:por-id";
	public static final String BD_USUARIO_NAO_ENCONTRADO_POR_ID = "repository:usuario:nao-encontrado:por-id";
	public static final String BD_USUARIO_NAO_ENCONTRADO_POR_EMAIL = "repository:usuario:nao-encontrado";
	public static final String MD_USUARIO_INATIVO = "modelo:usuario:inativo";
	public static final String MD_USUARIO_ESTA_SUSPENSO = "modelo:usuario:suspenso";
	public static final String MD_USUARIO_ESTEVE_SUSPENSO = "modelo:usuario:suspenso:passou";
	public static final String MD_USUARIO_SENHA_ERRADA = "modelo:usuario:senha-errada";
	public static final String BD_TOKEN_GRAVA = "repository:token:grava";
	public static final String BD_TOKEN_EXISTE_POR_USUARIO = "repository:token:existe";
	public static final String BD_TOKEN_NAO_EXISTE_POR_USUARIO = "repository:token:nao-existe";
	public static final String BD_TOKEN_ENCONTRADO_POR_TOKEN = "repository:token:confirmar-token:existe";
	public static final String BD_TOKEN_NAO_ENCONTRADO_POR_TOKEN = "repository:token:confirmar-token:nao-existe";
	public static final String INFRA_CODIFICA_SENHA = "infra:codifica-senha";
	public static final String SVC_CARGO_PADRAO_NENHUM = "servico:cargo:padrao:nenhum";
	public static final String BD_FUNCIONALIDADE_ENCONTRA_PARTICIPAR_EVENTO = "repository:funcionalidade:participar-evento";
	public static final String BD_FUNCIONALIDADE_ENCONTRA_AVALIAR_EVENTO = "repository:funcionalidade:avaliar-evento";
	public static final String BD_FUNCIONALIDADE_ENCONTRA_AVALIAR_PARTICIPANTE = "repository:funcionalidade:avaliar-participante";
	public static final String BD_FUNCIONALIDADE_ENCONTRA_ORGANIZAR_EVENTO = "repository:funcionalidade:organizar-evento";
	public static final String BD_FUNCIONALIDADE_DENUNCIAR_EVENTO = "repository:funcionalidade:denunciar-evento";
	public static final String BD_FUNCIONALIDADE_DENUNCIAR_PARTICIPANTE = "repository:funcionalidade:denunciar-participante";

	public static final String MD_CARGO_PADRAO = "modelo:cargo:cargo-padrao";
	public static final String MD_FUNCIONALIDADE_PARTICIPAR_EVENTO = "modelo:funcionalidade:participar-evento";

	public static final String MD_CARGO_VENDEDOR = "modelo:cargo:cargo-vendedor";
	public static final String MD_FUNCIONALIDADE_EDITAR_DOCUMENTOS = "modelo:funcionalidade:editar-documentos";
	public static final String MD_FUNCIONALIDADE_AVALIAR_EVENTO = "modelo:funcionalidade:avaliar-evento";
	public static final String MD_FUNCIONALIDADE_AVALIAR_PARTICIPANTE = "modelo:funcionalidade:avaliar-participante";
	public static final String MD_FUNCIONALIDADE_ORGANIZAR_EVENTO = "modelo:funcionalidade:organizar-evento";

	private TagSaida() {
	}
	
	public static boolean temTag(TestInfo info, String... tags) {
		for (String tagX : tags) {
			if (info.getTags().contains(tagX)) {
				return true;
			}
		}
		return false;
	}
}
