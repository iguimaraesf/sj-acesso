package com.ivini.saidasjuntas.controller;

import java.util.Locale;

import org.springframework.context.MessageSource;

import com.ivini.saidasjuntas.acesso.dto.ListaMensagensRetornoDTO;
import com.ivini.saidasjuntas.acesso.util.MensagemErroHelper;

public class BaseSaidaController {
	private static final String MENSAGEM_RETORNO_SUCESSO = "mensagem.retorno.sucesso";

	protected ListaMensagensRetornoDTO mensagemSucesso(MessageSource messageSource, Locale locale) {
		return mensagemSucesso(messageSource, locale, null);
	}

	protected ListaMensagensRetornoDTO mensagemSucesso(MessageSource messageSource, Locale locale, Object complemento) {
		return MensagemErroHelper.criarResposta(MensagemErroHelper.criarMensagemSucesso(messageSource, MENSAGEM_RETORNO_SUCESSO, locale), complemento);
	}

}
