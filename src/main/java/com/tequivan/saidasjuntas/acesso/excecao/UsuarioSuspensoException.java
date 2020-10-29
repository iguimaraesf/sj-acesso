package com.tequivan.saidasjuntas.acesso.excecao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UsuarioSuspensoException extends AbstractSaidasException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6546136119710764622L;

	public UsuarioSuspensoException(String emailOuDocumento, LocalDate dataFim) {
		super(String.format("O usuário %s está suspenso até %s.", emailOuDocumento, dataFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
	}

}
