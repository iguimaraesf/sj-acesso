package com.ivantex.saidasjuntas.fixture;

import org.junit.jupiter.api.TestInfo;

public final class TagSaida {
	public static final String USUARIO_EXISTE = "repository:usuario:existe";
	public static final String USUARIO_NAO_EXISTE = "repository:usuario:nao-existe";
	public static final String GRAVA_USUARIO = "repository:usuario:grava";
	public static final String USUARIO_ENCONTRADO = "repository:usuario:encontrado";
	public static final String USUARIO_NAO_ENCONTRADO = "repository:usuario:nao-encontrado";
	public static final String GRAVA_TOKEN = "repository:token:grava";
	public static final String TOKEN_EXISTE = "repository:token:existe";

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
