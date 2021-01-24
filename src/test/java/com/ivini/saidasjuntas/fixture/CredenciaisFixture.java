package com.ivini.saidasjuntas.fixture;

import org.junit.jupiter.api.TestInfo;

import com.ivini.saidasjuntas.tag.TesteConstSaida;
import com.ivini.saidasjuntas.acesso.dto.CredenciaisDTO;
import com.ivini.saidasjuntas.tag.TagSaida;

public final class CredenciaisFixture {
	private CredenciaisFixture() {
	}

	public static CredenciaisDTO criar(TestInfo info) {
		var dto = new CredenciaisDTO();
		dto.setSenha(definirSenha(info));
		dto.setUsuario(definirEmail(info));
		return dto;
	}
	
	public static String definirSenha(TestInfo info) {
		if (TagSaida.temTag(info, TesteConstSaida.VALIDA_SENHA_CURTA)) {
			return "1!a";
		}
		return "123/4mudar-isso";
	}

	public static String definirEmail(TestInfo info) {
		if (TagSaida.temTag(info, TesteConstSaida.VALIDA_EMAIL_SEM_ARROBA)) {
			return "zemeio.com";
		} else if (TagSaida.temTag(info, TesteConstSaida.VALIDA_EMAIL_SOMENTE_DOMINIO)) {
			return "ze@meio";
		} else if (TagSaida.temTag(info, TesteConstSaida.VALIDA_EMAIL_DOMINIO_CURTO)) {
			return "ze@meio.c.br";
		}
		return "ze-silva_teste@meio.com";
	}


}
