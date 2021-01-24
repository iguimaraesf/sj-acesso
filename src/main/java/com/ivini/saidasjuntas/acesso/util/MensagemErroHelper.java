package com.ivini.saidasjuntas.acesso.util;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.ivini.saidasjuntas.acesso.dto.ListaMensagensRetornoDTO;
import com.ivini.saidasjuntas.acesso.dto.MensagemErroDTO;
import com.ivini.saidasjuntas.acesso.excecao.tipos.AbstractSaidasException;

public final class MensagemErroHelper {
	private static final String[] SEM_PARAMETROS = new String[0];

	private MensagemErroHelper() {
	}

	public static MensagemErroDTO criarMensagemErro(ObjectError erroX) {
		FieldError campo = ((FieldError)erroX);
		
		return new MensagemErroDTO(campo.getField(), campo.getDefaultMessage());
	}

	public static MensagemErroDTO criarMensagemErro(MessageSource messageSource, String chave, AbstractSaidasException exception, Locale locale) {
		return new MensagemErroDTO(chave, lerMensagemI18N(messageSource, exception, locale));
	}

	public static MensagemErroDTO criarMensagemSucesso(MessageSource messageSource, String chave, Locale locale) {
		return new MensagemErroDTO(chave, lerMensagemI18N(messageSource, chave, locale));
	}

	private static String lerMensagemI18N(MessageSource messageSource, String chave, Locale locale) {
		return messageSource.getMessage(chave, SEM_PARAMETROS, locale);
	}

	public static String lerMensagemI18N(MessageSource messageSource, AbstractSaidasException exception, Locale locale) {
		return messageSource.getMessage(exception.getMessage(), new String[] { exception.getValor() }, locale);
	}

	public static ListaMensagensRetornoDTO criarResposta(MensagemErroDTO umCampo) {
		return criarResposta(umCampo, null);
	}

	public static ListaMensagensRetornoDTO criarResposta(MensagemErroDTO umCampo, Object complemento) {
		return criarResposta(Arrays.asList(umCampo), complemento);
	}

	public static ListaMensagensRetornoDTO criarResposta(List<MensagemErroDTO> campos) {
		return new ListaMensagensRetornoDTO(campos, LocalDateTime.now(), null);
	}

	public static ListaMensagensRetornoDTO criarResposta(List<MensagemErroDTO> campos, Object complemento) {
		return new ListaMensagensRetornoDTO(campos, LocalDateTime.now(), complemento);
	}
}
