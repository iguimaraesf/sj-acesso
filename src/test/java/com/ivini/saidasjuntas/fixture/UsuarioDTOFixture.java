package com.ivini.saidasjuntas.fixture;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.TestInfo;

import com.ivini.saidasjuntas.acesso.dto.CadastroUsuarioDTO;
import com.ivini.saidasjuntas.acesso.dto.DetalhesUsuarioDTO;
import com.ivini.saidasjuntas.acesso.modelo.Usuario;
import com.ivini.saidasjuntas.config.SenhaConfig;
import com.ivini.saidasjuntas.tag.TagSaida;
import com.ivini.saidasjuntas.tag.TesteConstSaida;

public final class UsuarioDTOFixture {
	private UsuarioDTOFixture() {
	}

	public static CadastroUsuarioDTO criar(TestInfo info) {
		CadastroUsuarioDTO dto = new CadastroUsuarioDTO();
		dto.setNome(definirNome(info));
		dto.setEmail(CredenciaisFixture.definirEmail(info));
		dto.setSenha(CredenciaisFixture.definirSenha(info));
		dto.setConfirmacaoSenha(definirConfirmacaoSenha(info, dto.getSenha()));
		return dto;
	}

	public static List<DetalhesUsuarioDTO> listaComOs3Registros(TestInfo info, SenhaConfig senhaConfig) {
		DetalhesUsuarioDTO u01 = toDetalheUsuarioDTO(UsuarioFixture.criarUsuarioCadastrado01Maria(info, senhaConfig));
		DetalhesUsuarioDTO u02 = toDetalheUsuarioDTO(UsuarioFixture.criarUsuarioCadastrado02Pedro(info, senhaConfig));
		DetalhesUsuarioDTO u03 = toDetalheUsuarioDTO(UsuarioFixture.criarUsuarioCadastrado03Antonio(info, senhaConfig));
		return Arrays.asList(u01, u02, u03);
	}

	private static DetalhesUsuarioDTO toDetalheUsuarioDTO(Usuario usuario) {
		DetalhesUsuarioDTO dto = new DetalhesUsuarioDTO();
		dto.setAtivo(usuario.getDataInativacao() == null);
		dto.setConfirmado(true);
		dto.setEmail(usuario.getEmail());
		dto.setId(usuario.getIdUsuario());
		dto.setNome(usuario.getNome());
		dto.setSenha(usuario.getSenha());
		dto.setSuspenso(usuario.getDataFimSuspensao() != null && usuario.getDataFimSuspensao().isBefore(LocalDate.now()));
		return dto;
	}

	private static String definirConfirmacaoSenha(TestInfo info, String senhaCorreta) {
		if (TagSaida.temTag(info, TesteConstSaida.VALIDA_CONFIRMACAO_SENHA_ERRADA)) {
			return "!@#r2";
		}
		return senhaCorreta;
	}

	private static String definirNome(TestInfo info) {
		if (TagSaida.temTag(info, TesteConstSaida.VALIDA_NOME_VAZIO)) {
			return "";
		}
		return "João da Silva";
	}

	private static CadastroUsuarioDTO criarUsuarioDTOPersonalizado(String nome, String email) {
		final CadastroUsuarioDTO usuario = new CadastroUsuarioDTO();
		usuario.setNome(nome);
		usuario.setEmail(email);
		usuario.setSenha("senhajoao");
		return usuario;
	}

	public static CadastroUsuarioDTO criarUsuarioDTOJoao() {
		return criarUsuarioDTOPersonalizado("João da Silva", "joao@gmail.com");
	}

}
