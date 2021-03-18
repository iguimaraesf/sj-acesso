package com.ivini.saidasjuntas;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ivini.saidasjuntas.controller.BaseController;
import com.ivini.saidasjuntas.controller.UsuarioAbertoController;
import com.ivini.saidasjuntas.controller.UsuarioAdminController;
import com.ivini.saidasjuntas.controller.UsuarioProtegidoController;

class SjAcessoApplicationIT extends BaseController {

	@Autowired
	private UsuarioAbertoController aberto;
	@Autowired
	private UsuarioProtegidoController protegido;
	@Autowired
	private UsuarioAdminController admin;

	@Test
	void contextLoads() {
		assertThat(aberto).isNotNull();
		assertThat(protegido).isNotNull();
		assertThat(admin).isNotNull();
	}

}
