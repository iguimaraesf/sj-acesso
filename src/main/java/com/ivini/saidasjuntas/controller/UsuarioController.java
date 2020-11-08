package com.ivini.saidasjuntas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ivini.saidasjuntas.acesso.dto.UsuarioDTO;
import com.ivini.saidasjuntas.acesso.excecao.AbstractSaidasException;
import com.ivini.saidasjuntas.acesso.modelos.Usuario;
import com.ivini.saidasjuntas.acesso.servico.dados.UsuarioService;

@RestController
@RequestMapping("/api/v1")
public class UsuarioController {
	@Autowired
	private UsuarioService service;
	
	@PostMapping("/registrar")
	public void registrar(@RequestBody UsuarioDTO usuario) throws AbstractSaidasException {
		service.registrarNovoUsuario(usuario);
	}
	
	@PostMapping("/confirmar")
	public void confirmar(@RequestBody String codigo) throws AbstractSaidasException {
		service.confirmarUsuario(codigo);
	}

	@PostMapping("/reenviar")
	public void reenviar(@RequestBody CredenciaisDTO credenciais) throws AbstractSaidasException {
		service.reenviarToken(credenciais.getUsuario(),  credenciais.getSenha());
	}

	@PostMapping("/entrar")
	public Usuario entrar(@RequestBody CredenciaisDTO credenciais) throws AbstractSaidasException {
		return service.loginUsuario(credenciais.getUsuario(), credenciais.getSenha());
	}
	
	@PostMapping("/inativar")
	public void inativar(@RequestBody String usuarioId) throws AbstractSaidasException {
		service.inativar(usuarioId);
	}

	@PostMapping("/reativar")
	public void reativar(@RequestBody String usuarioId) throws AbstractSaidasException {
		service.reativar(usuarioId);
	}

	@PostMapping("/suspender")
	public void suspender(@RequestBody String usuarioId) throws AbstractSaidasException {
		service.suspender(usuarioId);
	}

}
