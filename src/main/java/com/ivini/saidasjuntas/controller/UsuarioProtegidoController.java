package com.ivini.saidasjuntas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
}
