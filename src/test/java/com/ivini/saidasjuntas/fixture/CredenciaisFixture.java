package com.ivini.saidasjuntas.fixture;

import org.junit.jupiter.api.TestInfo;

import com.ivini.saidasjuntas.tag.TesteConstSaida;
import com.ivini.saidasjuntas.acesso.dto.AssociacaoDTO;
import com.ivini.saidasjuntas.acesso.dto.CredenciaisDTO;
import com.ivini.saidasjuntas.tag.TagSaida;

public final class CredenciaisFixture {
	public static final String CODIGO_USUARIO_INEXISTENTE = "0xxx";

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

	public static AssociacaoDTO criarAssociacao(TestInfo info) {
		AssociacaoDTO dto = new AssociacaoDTO();
		if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID)) {
			dto.setIdUsuario(UsuarioFixture.ID_1111_2222_MARIA);
		} else if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_ID)) {
			dto.setIdUsuario(CODIGO_USUARIO_INEXISTENTE);
		}
		if (TagSaida.temTag(info, 
				TesteConstSaida.BD_USUARIO_GERENTE_DIFERENTE, TesteConstSaida.BD_USUARIO_EMPREGADOR_DIFERENTE, TesteConstSaida.BD_USUARIO_CENTRAL_DIFERENTE)) {
			dto.setIdReferencia(CODIGO_USUARIO_INEXISTENTE);
		} else if (TagSaida.temTag(info, 
				TesteConstSaida.BD_USUARIO_GERENTE_IGUAL, TesteConstSaida.BD_USUARIO_EMPREGADOR_IGUAL, TesteConstSaida.BD_USUARIO_CENTRAL_IGUAL,
				TesteConstSaida.BD_USUARIO_GERENTE_NULO, TesteConstSaida.BD_USUARIO_EMPREGADOR_NULO, TesteConstSaida.BD_USUARIO_CENTRAL_NULO)) {
			// esse campo é obrigatório, mesmo que o valor na base de dados seja null.
			dto.setIdReferencia(UsuarioFixture.ID_1111_2224_ANTONIO);
		}
		// TODO agora falta a mensagem
		return dto;
	}


}
