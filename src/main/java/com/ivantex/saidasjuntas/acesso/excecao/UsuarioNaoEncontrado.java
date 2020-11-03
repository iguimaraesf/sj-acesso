package com.ivantex.saidasjuntas.acesso.excecao;

public class UsuarioNaoEncontrado extends AbstractSaidasException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9166513503894420364L;

	public UsuarioNaoEncontrado(String emailOuDocumento) {
		super(String.format("Nenhuma pessoa encontrada na busca por %s.", emailOuDocumento));
	}

}
