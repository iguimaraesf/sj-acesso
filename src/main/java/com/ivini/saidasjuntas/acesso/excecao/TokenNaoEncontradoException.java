package com.ivini.saidasjuntas.acesso.excecao;

public class TokenNaoEncontradoException extends AbstractSaidasException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -856103341054464165L;

	public TokenNaoEncontradoException(String token) {
		super(String.format("O token %s não foi encontrado", token));
	}

}
