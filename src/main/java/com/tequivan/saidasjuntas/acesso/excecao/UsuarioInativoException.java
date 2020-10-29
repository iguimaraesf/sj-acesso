package com.tequivan.saidasjuntas.acesso.excecao;

public class UsuarioInativoException extends AbstractSaidasException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -346794103708899215L;

	public UsuarioInativoException(String emailOuDocumento) {
		super(String.format("O usuário %s está inativo.", emailOuDocumento));
	}

}
