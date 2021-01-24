package com.ivini.saidasjuntas.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class BaseController {
	protected static final String CHAVE_ERRO = "erro";
	protected static final String CHAVE_SUCESSO = "mensagem.retorno.sucesso";
	protected static final String CHAVE_NAO_ENCONTRADO = "naoEncontrado";
	protected static final String CHAVE_CONTRATO = "contrato";
	protected static final String RESULTADO_SUCESSO = "sucesso!";
	protected static final String RESULTADO_EMAIL_NAO_ENCONTRADO = "O usuário ze-silva_teste@meio.com não foi encontrado";
	protected static final String RESULTADO_EMAIL_NAO_CONFIRMADO = "O usuário ze-silva_teste@meio.com ainda não foi confirmado";
	protected static final String RESULTADO_EMAIL_INATIVO = "O usuário ze-silva_teste@meio.com está inativo";
	private static final String HOJE_FORMATADO = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	protected static final String RESULTADO_EMAIL_SUSPENSO = "Este usuário está suspenso até " + HOJE_FORMATADO;
	protected static final String RETORNO_MENSAGENS_DESCRICAO = "$.mensagens[*].descricao";
	protected static final String RETORNO_MENSAGENS_CHAVE = "$.mensagens[*].chave";
	protected static final String RETORNO_COMPLEMENTO_OBJETO = "$.complemento";

	protected BaseController() {
	}

}
