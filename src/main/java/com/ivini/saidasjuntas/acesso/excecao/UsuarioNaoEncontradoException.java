package com.ivini.saidasjuntas.acesso.excecao;

public class UsuarioNaoEncontradoException extends AbstractSaidasException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9166513503894420364L;

	public UsuarioNaoEncontradoException(String emailOuDocumento) {
		super(String.format("Nenhuma pessoa encontrada na busca por %s.", emailOuDocumento));
	}

}
