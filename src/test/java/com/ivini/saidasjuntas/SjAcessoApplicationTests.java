package com.ivini.saidasjuntas;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.ivini.saidasjuntas.controller.UsuarioAbertoController;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SjAcessoApplicationTests {

	@Autowired
	private UsuarioAbertoController usuarioController;

	@Test
	void contextLoads() {
		assertThat(usuarioController).isNotNull();
	}

}
