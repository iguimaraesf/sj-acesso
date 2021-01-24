package com.ivini.saidasjuntas.acesso.excecao.tipos;

public class EmailJaExisteException extends AbstractSaidasException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7383250334267459170L;

	public EmailJaExisteException(String email) {
		super("jaExiste.email", email);
	}

}
