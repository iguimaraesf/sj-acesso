package com.ivini.saidasjuntas.acesso.servico.dados;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.ivini.saidasjuntas.config.SenhaConfig;

@ActiveProfiles("test")
@SpringBootTest
class SenhaTest {

	private static final String MINHA_SENHA = "Teste$123";
	@Autowired
	private SenhaConfig senha;

	@Test
	void gerarSenha() {
		/*String cod1 = senha.encoder().encode(MINHA_SENHA);
		String cod2 = senha.encoder().encode(MINHA_SENHA);
		assertThat(senha.encoder().matches(MINHA_SENHA, cod1)).isTrue();
		assertThat(senha.encoder().matches(MINHA_SENHA, cod2)).isTrue();
		assertThat(cod1).isNotEqualTo(cod2);*/
	}
}
