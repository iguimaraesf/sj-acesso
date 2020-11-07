package com.ivini.saidasjuntas.acesso.excecao;

public class DocumentoJaExisteException extends AbstractSaidasException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 475110509927543660L;

	public DocumentoJaExisteException(String numeroDocumento) {
		super(String.format("O documento com o número %s já está presente no sistema.", numeroDocumento));
	}

}
