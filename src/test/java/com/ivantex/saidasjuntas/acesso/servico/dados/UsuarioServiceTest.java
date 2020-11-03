package com.ivantex.saidasjuntas.acesso.servico.dados;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ivantex.saidasjuntas.acesso.modelos.Usuario;
import com.ivantex.saidasjuntas.acesso.repositorio.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
	@Mock
	private UsuarioRepository repository;
	
	@InjectMocks
	private UsuarioService service;
	
	@Test
	void deveGravarNovoUsuario() {
		final Usuario usuario = criarUsuario("João da Silva", "joao@gmail.com", "senhajoao");
		Mockito.when(repository.existsByEmail(usuario.getEmail())).thenReturn(false);
		
	}

	private Usuario criarUsuario(String nome, String email, String senha) {
		final Usuario usuario = new Usuario();
		usuario.setNome(nome);
		usuario.setEmail(email);
		usuario.setSenha(senha);
		return usuario;
	}
}
