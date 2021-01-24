package com.ivini.saidasjuntas.fixture;

import java.util.Optional;

import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;

import com.ivini.saidasjuntas.acesso.modelo.TokenConfirmacao;
import com.ivini.saidasjuntas.acesso.repositorio.TokenConfirmacaoRepository;
import com.ivini.saidasjuntas.config.SenhaConfig;
import com.ivini.saidasjuntas.tag.TesteConstSaida;
import com.ivini.saidasjuntas.tag.TagSaida;

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

	public static void configurarRepositorio(TestInfo info, TokenConfirmacaoRepository tokenRep, SenhaConfig senhaConfig) {
		if (TagSaida.metodoDeTesteComecaCom(info, "registrar", "confirmarToken")) {
			configurarRepositorioAoRegistrarOuConfirmarToken(info, tokenRep, senhaConfig);
		} else if (TagSaida.metodoDeTesteComecaCom(info, "reenviar")) {
			configurarRepositorioAoReenviar(info, tokenRep, senhaConfig);
		}
	}

	public static void verificarRepositorio(TestInfo info, TokenConfirmacaoRepository tokenRep) {
		if (TagSaida.metodoDeTesteComecaCom(info, "registrar")) {
			verificarRepositorioAoRegistrar(info, tokenRep);
		} else if (TagSaida.metodoDeTesteComecaCom(info, "confirmarToken")) {
			verificarRepositorioAoConfirmarToken(info, tokenRep);
		} else if (TagSaida.metodoDeTesteComecaCom(info, "reenviar")) {
			verificarRepositorioAoReenviarToken(info, tokenRep);
		}
	}

	static void configurarRepositorioAoRegistrarOuConfirmarToken(TestInfo info, TokenConfirmacaoRepository tokenRep, SenhaConfig senhaConfig) {
		if (TagSaida.temTag(info, TesteConstSaida.BD_TOKEN_ERRO_FATAL)) {
			Mockito.when(tokenRep.findByTokenGerado(Mockito.anyString())).thenThrow(new IllegalArgumentException("Teste dando erro inesperado ao procurar por token"));
			return;
		}
		configurarRepositorioComum(info, tokenRep, senhaConfig);
	}

	private static void configurarRepositorioComum(TestInfo info, TokenConfirmacaoRepository tokenRep, SenhaConfig senhaConfig) {
		Mockito.when(tokenRep.save(Mockito.any())).thenReturn(TokenConfirmacaoFixture.criar(info, senhaConfig));
		Mockito.when(tokenRep.findByUsuarioIdUsuario(Mockito.anyString())).thenReturn(TokenConfirmacaoFixture.tokenJaExiste(info));
		if (TagSaida.temTag(info, TesteConstSaida.BD_TOKEN_NAO_ENCONTRADO_POR_TOKEN)) {
			Mockito.when(tokenRep.findByTokenGerado(Mockito.anyString())).thenReturn(Optional.empty());
			Mockito.doThrow(new IllegalArgumentException("Nunca deveria ter chegado aqui")).when(tokenRep).delete(Mockito.any());
		} else {
			TokenConfirmacao token = new TokenConfirmacao("0000-1111", UsuarioFixture.criarUsuarioCadastrado01Maria(info, senhaConfig), "123");
			Mockito.when(tokenRep.findByTokenGerado(Mockito.anyString())).thenReturn(Optional.of(token));
			Mockito.doNothing().when(tokenRep).delete(token);
		}
	}

	static void configurarRepositorioAoReenviar(TestInfo info, TokenConfirmacaoRepository tokenRep, SenhaConfig senhaConfig) {
		if (TagSaida.temTag(info, TesteConstSaida.BD_TOKEN_ERRO_FATAL)) {
			Mockito.when(tokenRep.findByUsuarioIdUsuario(Mockito.anyString())).thenThrow(new IllegalArgumentException("Teste dando erro inesperado ao buscar por ID de usuário"));
			return;
		}
		configurarRepositorioComum(info, tokenRep, senhaConfig);
	}

	static void verificarRepositorioAoConfirmarToken(TestInfo info, TokenConfirmacaoRepository tokenRep) {
		Mockito.verify(tokenRep).findByTokenGerado(Mockito.anyString());
		if (TagSaida.temTag(info, TesteConstSaida.BD_TOKEN_ERRO_FATAL)) {
			Mockito.verify(tokenRep, Mockito.never()).delete(Mockito.any());
			return;
		}
		verificarRepositorioComum(info, tokenRep);
	}

	static void verificarRepositorioAoReenviarToken(TestInfo info, TokenConfirmacaoRepository tokenRep) {
		if (TagSaida.temTag(info, TesteConstSaida.BD_TOKEN_ERRO_FATAL, TesteConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_EMAIL)) {
			Mockito.verify(tokenRep, Mockito.never()).delete(Mockito.any());
			return;
		}
		verificarRepositorioComum(info, tokenRep);
	}

	private static void verificarRepositorioComum(TestInfo info, TokenConfirmacaoRepository tokenRep) {
		boolean tokenNaoEncontradoPorToken = TagSaida.temTag(info, TesteConstSaida.BD_TOKEN_NAO_ENCONTRADO_POR_TOKEN);
		boolean tokenEncontradoPorToken = TagSaida.temTag(info, TesteConstSaida.BD_TOKEN_ENCONTRADO_POR_TOKEN);
		if (tokenNaoEncontradoPorToken) {
			Mockito.verify(tokenRep, Mockito.never()).delete(Mockito.any());
		} else if (tokenEncontradoPorToken || !tokenNaoEncontradoPorToken) {
			Mockito.verify(tokenRep).delete(Mockito.any());
		}
	}

	static void verificarRepositorioAoRegistrar(TestInfo info, TokenConfirmacaoRepository tokenRep) {
		boolean validacoes = TagSaida.temTag(info, TesteConstSaida.VALIDA_CONFIRMACAO_SENHA_ERRADA,
				TesteConstSaida.VALIDA_EMAIL_DOMINIO_CURTO, 
				TesteConstSaida.VALIDA_EMAIL_SEM_ARROBA, 
				TesteConstSaida.VALIDA_EMAIL_SOMENTE_DOMINIO, 
				TesteConstSaida.VALIDA_NOME_VAZIO, 
				TesteConstSaida.VALIDA_SENHA_CURTA,
				TesteConstSaida.BD_USUARIO_EXISTE_POR_EMAIL);
		boolean tokenNaoEncontradoPorToken = !validacoes && TagSaida.temTag(info, TesteConstSaida.BD_TOKEN_ENCONTRADO_POR_TOKEN);
		if (validacoes) {
			Mockito.verify(tokenRep, Mockito.never()).findByUsuarioIdUsuario(Mockito.anyString());
			Mockito.verify(tokenRep, Mockito.never()).delete(Mockito.any());
			Mockito.verify(tokenRep, Mockito.never()).save(Mockito.any());
		} else if (tokenNaoEncontradoPorToken) {
			Mockito.verify(tokenRep).findByUsuarioIdUsuario(Mockito.anyString());
			Mockito.verify(tokenRep).delete(Mockito.any());
			Mockito.verify(tokenRep).save(Mockito.any());
		} else {
			Mockito.verify(tokenRep).findByUsuarioIdUsuario(Mockito.anyString());
			Mockito.verify(tokenRep, Mockito.never()).delete(Mockito.any());
			Mockito.verify(tokenRep).save(Mockito.any());
		}
	}
}
