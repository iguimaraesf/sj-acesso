package com.ivini.saidasjuntas.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ivini.saidasjuntas.acesso.dto.DetalhesUsuarioDTO;
import com.ivini.saidasjuntas.acesso.dto.ListaMensagensRetornoDTO;
import com.ivini.saidasjuntas.acesso.excecao.tipos.AbstractSaidasException;
import com.ivini.saidasjuntas.acesso.servico.dados.UsuarioService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v1/admin")
@Api("Gerenciamento dos usuários cadastrados")
public class UsuarioAdminController extends BaseSaidaController {
	private final UsuarioService service;
	private final MessageSource messageSource;

	@Autowired
	public UsuarioAdminController(UsuarioService service, MessageSource messageSource) {
		this.service = service;
		this.messageSource = messageSource;
	}
	
	/*
	 * Aqui as credenciais guardam o CÓDIGO do usuário, não o e-mail.
	 */
	//@PreAuthorize("hasHole('INAT-REAT')")
	@PatchMapping("/inativar/{usuarioId}")
	public ListaMensagensRetornoDTO inativar(@PathVariable String usuarioId, Locale locale) throws AbstractSaidasException {
		String data = service.inativar(usuarioId);
		return mensagemSucesso(messageSource, locale, data);
	}

	//@PreAuthorize("hasHole('INAT-REAT')")
	@PatchMapping("/reativar/{usuarioId}")
	public ListaMensagensRetornoDTO reativar(@PathVariable String usuarioId, Locale locale) throws AbstractSaidasException {
		String data = service.reativar(usuarioId);
		return mensagemSucesso(messageSource, locale, data);
	}

	//@PreAuthorize("hasHole('SUSPENDER')")
	@PatchMapping("/suspender/{usuarioId}")
	public ListaMensagensRetornoDTO suspender(@PathVariable String usuarioId, Locale locale) throws AbstractSaidasException {
		String data = service.suspender(usuarioId);
		return mensagemSucesso(messageSource, locale, data);
	}

	//@PreAuthorize("hasHole('UM-USUARIO')")
	@GetMapping({"/listar/{paginaAtual}/{registrosPorPagina}", "/listar", "/listar/{paginaAtual}"})
	public ListaMensagensRetornoDTO listar(@PathVariable(required = false) Integer paginaAtual, @PathVariable(required = false) Integer registrosPorPagina, Locale locale) {
		int atual = paginaAtual == null || paginaAtual <= 0 ? 1 : paginaAtual;
		int qtd = registrosPorPagina == null || registrosPorPagina <= 0 ? 10 : registrosPorPagina;
		Page<DetalhesUsuarioDTO> lista = service.listar(atual, qtd);
		return mensagemSucesso(messageSource, locale, lista);
	}
	
	@PatchMapping("/tornar/vendedor/{usuarioId}/{supervisorId}/{mensagem}")
	public ListaMensagensRetornoDTO tornarVendedor(@PathVariable String usuarioId, @PathVariable String supervisorId, @PathVariable String mensagem, Locale locale) throws AbstractSaidasException {
		return mensagemSucesso(messageSource, locale, service.associarVendedor(usuarioId, supervisorId, mensagem));
	}
}
