package com.ivini.saidasjuntas.controller;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ivini.saidasjuntas.acesso.dto.CadastroUsuarioDTO;
import com.ivini.saidasjuntas.acesso.dto.CredenciaisDTO;
import com.ivini.saidasjuntas.acesso.dto.ListaMensagensRetornoDTO;
import com.ivini.saidasjuntas.acesso.excecao.tipos.AbstractSaidasException;
import com.ivini.saidasjuntas.acesso.modelo.Usuario;
import com.ivini.saidasjuntas.acesso.servico.dados.UsuarioService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/aberto")
@Api("Tratamento dos acessos aos usuários do sistema")
public class UsuarioAbertoController extends BaseSaidaController {
	private final UsuarioService service;
	private final MessageSource messageSource;

	@Autowired
	public UsuarioAbertoController(UsuarioService service, MessageSource messageSource) {
		this.service = service;
		this.messageSource = messageSource;
	}
	
	@GetMapping("/confirmar/{token}")
	public ListaMensagensRetornoDTO confirmar(@PathVariable("token") String codigo, Locale locale) throws AbstractSaidasException {
		service.confirmarUsuario(codigo);
		return mensagemSucesso(messageSource, locale);
	}

	@PostMapping("/entrar")
	public ListaMensagensRetornoDTO entrar(@RequestBody CredenciaisDTO dadosLogin, Locale locale) throws AbstractSaidasException {
		Usuario usuario = service.loginUsuario(dadosLogin.getUsuario(), dadosLogin.getSenha());
		return mensagemSucesso(messageSource, locale, usuario);
	}

	@PostMapping("/registrar")
	@ApiOperation("Registro de um novo usuário, com confirmação por link enviado por e-mail")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ListaMensagensRetornoDTO registrar(@RequestBody @Valid CadastroUsuarioDTO usuario, Locale locale/*, @AuthenticationPrincipal UserDetails sessao */) throws AbstractSaidasException {
		service.registrarNovoUsuario(usuario);
		return mensagemSucesso(messageSource, locale);
	}

	@PostMapping("/reenviar")
	public ListaMensagensRetornoDTO reenviar(@RequestBody CredenciaisDTO credenciais, Locale locale) throws AbstractSaidasException {
		service.reenviarToken(credenciais.getUsuario());
		return mensagemSucesso(messageSource, locale);
	}

}
