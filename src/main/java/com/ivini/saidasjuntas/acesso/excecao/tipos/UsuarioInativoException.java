package com.ivini.saidasjuntas.acesso.excecao.tipos;

public class UsuarioInativoException extends AbstractSaidasException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -346794103708899215L;

	public UsuarioInativoException(String emailOuDocumento) {
		//super(String.format("O usuário %s está inativo.", emailOuDocumento));
		super("usuario.inativo", emailOuDocumento);
	}

}
