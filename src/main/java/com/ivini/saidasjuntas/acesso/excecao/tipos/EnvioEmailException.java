package com.ivini.saidasjuntas.acesso.excecao.tipos;

import org.springframework.mail.MailException;

public class EnvioEmailException extends AbstractSaidasException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6282607566738462765L;

	public EnvioEmailException(String emailDe, String emailPara, MailException e) {
		super("envio.email", emailPara, e);
	}

}
