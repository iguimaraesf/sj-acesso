package com.ivantex.saidasjuntas.fixture;

import org.junit.jupiter.api.TestInfo;

public final class TagSaida {
	public static final String USUARIO_EXISTE = "repository:usuario:existe";
	public static final String USUARIO_NAO_EXISTE = "repository:usuario:nao-existe";
	public static final String GRAVA_USUARIO = "repository:usuario:grava";
	public static final String USUARIO_ENCONTRADO_POR_EMAIL = "repository:usuario:encontrado:por-email";
	public static final String USUARIO_ENCONTRADO_POR_ID = "repository:usuario:encontrado:por-id";
	public static final String USUARIO_NAO_ENCONTRADO_POR_ID = "repository:usuario:nao-encontrado:por-id";
	public static final String USUARIO_NAO_ENCONTRADO = "repository:usuario:nao-encontrado";
	public static final String USUARIO_INATIVO = "modelo:usuario:inativo";
	public static final String USUARIO_ESTA_SUSPENSO = "modelo:usuario:suspenso";
	public static final String USUARIO_ESTEVE_SUSPENSO = "modelo:usuario:suspenso:passou";
	public static final String USUARIO_SENHA_ERRADA = "modelo:usuario:senha-errada";
	public static final String GRAVA_TOKEN = "repository:token:grava";
	public static final String PROCURA_TOKEN_POR_USUARIO_EXISTE = "repository:token:existe";
	public static final String PROCURA_TOKEN_POR_USUARIO_NAO_EXISTE = "repository:token:nao-existe";
	public static final String CONFIRMAR_TOKEN_EXISTE = "repository:token:confirmar-token:existe";
	public static final String CONFIRMAR_TOKEN_NAO_EXISTE = "repository:token:confirmar-token:nao-existe";
	public static final String CODIFICA_SENHA = "infra:codifica-senha";

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
