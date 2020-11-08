package com.ivini.saidasjuntas.fixture;

import java.time.LocalDate;

import org.junit.jupiter.api.TestInfo;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ivini.saidasjuntas.acesso.dto.UsuarioDTO;
import com.ivini.saidasjuntas.acesso.modelos.Usuario;

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

	public static Usuario criarUsuario(TestInfo info, UsuarioDTO usuario, PasswordEncoder encoder) {
		final Usuario usuarioX = new Usuario();
		usuarioX.setNome(usuario.getNome());
		usuarioX.setEmail(usuario.getEmail());
		if (TagSaida.temTag(info, TagSaida.MD_USUARIO_SENHA_ERRADA)) {
			usuarioX.setSenha(encoder.encode("123"));
		} else {
			usuarioX.setSenha(encoder.encode(usuario.getSenha()));
		}
		if (TagSaida.temTag(info, TagSaida.MD_USUARIO_INATIVO)) {
			usuarioX.setDataInativacao(LocalDate.now());
		}
		if (TagSaida.temTag(info, TagSaida.MD_USUARIO_ESTA_SUSPENSO)) {
			usuarioX.setDataFimSuspensao(LocalDate.now());
		} else if (TagSaida.temTag(info, TagSaida.MD_USUARIO_ESTEVE_SUSPENSO)) {
			usuarioX.setDataFimSuspensao(LocalDate.now().minusDays(1L));
		}
		if (TagSaida.temTag(info, TagSaida.BD_USUARIO_ENCONTRADO_POR_ID)) {
			usuarioX.setUsuarioId("1111-xxxx");
		}
		usuarioX.setCargos(CargoFixture.criarLista(info));
		return usuarioX;
	}

}
