package com.ivini.saidasjuntas.fixture;

import java.util.Optional;

import org.junit.jupiter.api.TestInfo;

import com.ivini.saidasjuntas.acesso.modelo.TokenConfirmacao;
import com.ivini.saidasjuntas.config.SenhaConfig;
import com.ivini.saidasjuntas.tag.TagSaida;
import com.ivini.saidasjuntas.tag.TesteConstSaida;

public final class TokenConfirmacaoFixture {
	private TokenConfirmacaoFixture() {
	}

	public static TokenConfirmacao criar(TestInfo info, SenhaConfig senhaConfig) {
		return new TokenConfirmacao("1233-4444", UsuarioFixture.criarUsuarioCadastrado01Maria(info, senhaConfig), "3333-3333");
	}

	public static Optional<TokenConfirmacao> criarRetornandoAlgo() {
		return Optional.of(new TokenConfirmacao(null, null, null));
	}

	public static Optional<TokenConfirmacao> criarRetornandoVazio() {
		return Optional.empty();
	}

	public static Optional<TokenConfirmacao> tokenJaExiste(TestInfo info) {
		if (TagSaida.temTag(info, TesteConstSaida.BD_TOKEN_ENCONTRADO_POR_TOKEN)) {
			return criarRetornandoAlgo();
		}
		return criarRetornandoVazio();
	}

}
