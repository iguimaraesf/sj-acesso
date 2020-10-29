package com.tequivan.saidasjuntas.acesso.excecao;

public class UsuarioNaoConfirmadoException extends AbstractSaidasException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7847279725319254118L;

	public UsuarioNaoConfirmadoException(String emailOuDocumento) {
		super(String.format("O usuário %s ainda não foi confirmado.", emailOuDocumento));
	}

}
