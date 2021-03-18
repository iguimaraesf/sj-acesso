package com.ivini.saidasjuntas.controller;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ivini.saidasjuntas.acesso.dto.AssociacaoDTO;
import com.ivini.saidasjuntas.acesso.dto.ListaMensagensRetornoDTO;
import com.ivini.saidasjuntas.acesso.excecao.tipos.AbstractSaidasException;
import com.ivini.saidasjuntas.acesso.servico.dados.UsuarioService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v1/prot/usuario")
@Api("Tratamento dos acessos aos usuários deste módulo")
public class UsuarioProtegidoController extends BaseSaidaController {
	private final UsuarioService service;
	private final MessageSource messageSource;

	@Autowired
	public UsuarioProtegidoController(UsuarioService service, MessageSource messageSource) {
		this.service = service;
		this.messageSource = messageSource;
	}

	@PatchMapping("/associar/colaborador")
	public ListaMensagensRetornoDTO associarColaborador(@RequestBody @Valid AssociacaoDTO associacao, Locale locale) throws AbstractSaidasException {
		return mensagemSucesso(messageSource, locale, service.associarColaborador(associacao));
	}

	@PatchMapping("/desassociar/colaborador")
	public ListaMensagensRetornoDTO desassociarColaborador(@RequestBody @Valid AssociacaoDTO associacao, Locale locale) throws AbstractSaidasException {
		return mensagemSucesso(messageSource, locale, service.desassociarColaborador(associacao));
	}

	@PatchMapping("/associar/seguranca")
	public ListaMensagensRetornoDTO associarSeguranca(@RequestBody @Valid AssociacaoDTO associacao, Locale locale) throws AbstractSaidasException {
		return mensagemSucesso(messageSource, locale, service.associarSeguranca(associacao));
	}

	@PatchMapping("/desassociar/seguranca")
	public ListaMensagensRetornoDTO desassociarSeguranca(@RequestBody @Valid AssociacaoDTO associacao, Locale locale) throws AbstractSaidasException {
		return mensagemSucesso(messageSource, locale, service.desassociarSeguranca(associacao));
	}

	@PatchMapping("/associar/funcionario")
	public ListaMensagensRetornoDTO associarFuncionario(@RequestBody @Valid AssociacaoDTO associacao, Locale locale) throws AbstractSaidasException {
		return mensagemSucesso(messageSource, locale, service.associarFuncionario(associacao));
	}

	@PatchMapping("/desassociar/funcionario")
	public ListaMensagensRetornoDTO desassociarFuncionario(@RequestBody @Valid AssociacaoDTO associacao, Locale locale) throws AbstractSaidasException {
		return mensagemSucesso(messageSource, locale, service.desassociarFuncionario(associacao));
	}
}
