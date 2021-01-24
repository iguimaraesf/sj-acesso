package com.ivini.saidasjuntas.acesso.excecao.tipos;

public class OutroResponsavelException extends AbstractSaidasException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8074724563064274745L;

	public OutroResponsavelException(String idTentativa) {
		super("usuario.outro-responsavel", idTentativa);
	}

}
