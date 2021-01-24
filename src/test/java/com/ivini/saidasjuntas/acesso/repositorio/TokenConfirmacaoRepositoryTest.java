package com.ivini.saidasjuntas.acesso.repositorio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.persistence.EntityExistsException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.ivini.saidasjuntas.acesso.modelo.TokenConfirmacao;
import com.ivini.saidasjuntas.acesso.modelo.Usuario;

@DataJpaTest
class TokenConfirmacaoRepositoryTest {

	@Autowired
	private TokenConfirmacaoRepository tokenRep;
	@Autowired
	private UsuarioRepository usuarioRep;
	
	@Test
	void doisTokensComMesmoUsuarioLancaExcecao() {
		final Usuario usuario = gravarUsuario();
		gravarTok1(usuario);

		final TokenConfirmacao param3 = new TokenConfirmacao(null, usuario, "token2");
		final Exception excecao = assertThrows(DataIntegrityViolationException.class, () -> {
			tokenRep.save(param3);
		});
		assertThat(excecao).hasCauseInstanceOf(EntityExistsException.class);
	}

	@Test
	void existePorUsuarioEncontraUm() {
		final Usuario usuario = gravarUsuario();
		gravarTok1(usuario);
		assertThat(tokenRep.findByUsuarioIdUsuario(usuario.getIdUsuario())).isNotEmpty();
	}

	private Usuario gravarUsuario() {
		final Usuario param1 = new Usuario();
		param1.setNome("Teste da Silva");
		param1.setEmail("teste@dasilva.com");
		param1.setSenha("mudar123");
		final Usuario usuario = usuarioRep.save(param1);
		return usuario;
	}
	
	private void gravarTok1(final Usuario usuario) {
		final TokenConfirmacao param2 = new TokenConfirmacao(null, usuario, "tok1");
		tokenRep.save(param2);
	}

}
