package com.ivantex.saidasjuntas.acesso.excecao;

public class TokenJaValidadoException extends AbstractSaidasException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -856103341054464165L;

	public TokenJaValidadoException(String token) {
		super(String.format("O token %s já foi validado", token));
	}

}
