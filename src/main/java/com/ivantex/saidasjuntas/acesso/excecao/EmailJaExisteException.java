package com.ivantex.saidasjuntas.acesso.excecao;

public class EmailJaExisteException extends AbstractSaidasException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7383250334267459170L;

	public EmailJaExisteException(String email) {
		super(String.format("O endereço de e-mail %s já está no sistema.", email));
	}

}
