package com.ivini.saidasjuntas.acesso.excecao;

public class TokenInvalidoException extends AbstractSaidasException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -856103341054464165L;

	public TokenInvalidoException(String token) {
		super(String.format("O token %s é inválido", token));
	}

}
