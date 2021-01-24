package com.ivini.saidasjuntas.acesso.excecao.tipos;

public class TokenNaoEncontradoException extends AbstractSaidasException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -856103341054464165L;

	public TokenNaoEncontradoException(String token) {
		super("naoEncontrado.token", token);
	}

}
