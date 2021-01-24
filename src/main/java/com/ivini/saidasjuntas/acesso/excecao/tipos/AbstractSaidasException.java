package com.ivini.saidasjuntas.acesso.excecao.tipos;

import lombok.Getter;

@Getter
public class AbstractSaidasException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2506753488277376523L;
	private final String valor;

	protected AbstractSaidasException(String msg, String valor) {
		this(msg, valor, null);
	}

	protected AbstractSaidasException(String msg, String valor, Exception e) {
		super(msg, e);
		this.valor = valor;
	}
}
