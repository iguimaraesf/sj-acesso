package com.ivantex.saidasjuntas.acesso.repositorio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.persistence.EntityExistsException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.ivantex.saidasjuntas.acesso.modelos.TokenConfirmacao;
import com.ivantex.saidasjuntas.acesso.modelos.Usuario;

@DataJpaTest
class TokenConfirmacaoRepositoryTest {

	@Autowired
	private TokenConfirmacaoRepository tokenRep;
	@Autowired
	private UsuarioRepository usuarioRep;
	
	@Test
	void doisTokensComMesmoUsuarioLancaExcecao() {
		final Usuario param1 = new Usuario();
		param1.setNome("Teste da Silva");
		param1.setEmail("teste@dasilva.com");
		param1.setSenha("mudar123");
		final Usuario usuario = usuarioRep.save(param1);

		final TokenConfirmacao param2 = new TokenConfirmacao(null, usuario, "tok1");
		tokenRep.save(param2);

		final TokenConfirmacao param3 = new TokenConfirmacao(null, usuario, "token2");
		final Exception excecao = assertThrows(DataIntegrityViolationException.class, () -> {
			tokenRep.save(param3);
		});
		assertThat(excecao).hasCauseInstanceOf(EntityExistsException.class);
	}
}
