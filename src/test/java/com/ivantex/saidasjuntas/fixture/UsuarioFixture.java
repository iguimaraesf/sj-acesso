package com.ivantex.saidasjuntas.fixture;

import com.ivantex.saidasjuntas.acesso.dto.UsuarioDTO;
import com.ivantex.saidasjuntas.acesso.modelos.Usuario;

public class UsuarioFixture {

	private UsuarioFixture() {
	}

	private static UsuarioDTO criarUsuarioDTOPersonalizado(String nome, String email) {
		final UsuarioDTO usuario = new UsuarioDTO();
		usuario.setNome(nome);
		usuario.setEmail(email);
		usuario.setSenha("senhajoao");
		return usuario;
	}

	public static UsuarioDTO criarUsuarioDTOJoao() {
		return criarUsuarioDTOPersonalizado("João da Silva", "joao@gmail.com");
	}

	public static Usuario criarUsuario(UsuarioDTO usuario) {
		final Usuario usuarioX = new Usuario();
		usuarioX.setNome(usuario.getNome());
		usuarioX.setEmail(usuario.getEmail());
		usuarioX.setSenha("$2a$10$WbO163R/LKQcNFnu48T44OPyOdeqPd4zFecKHLLG3cOJ0NDSVMrfe");
		return usuarioX;
	}

}
