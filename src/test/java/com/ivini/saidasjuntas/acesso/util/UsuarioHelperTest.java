package com.ivini.saidasjuntas.acesso.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import com.ivini.saidasjuntas.acesso.dto.CadastroUsuarioDTO;
import com.ivini.saidasjuntas.acesso.modelo.Usuario;
import com.ivini.saidasjuntas.fixture.UsuarioFixture;
import com.ivini.saidasjuntas.tag.TesteConstSaida;

class UsuarioHelperTest {

	//private PasswordEncoder encoder = new BCryptPasswordEncoder();
	private Usuario usuario;
	
	@BeforeEach
	void setUp(TestInfo info) {
		CadastroUsuarioDTO dto = new CadastroUsuarioDTO();
		dto.setSenha("123123");
		usuario = UsuarioFixture.criarUsuario(info, dto/*, encoder*/);
	}

	@Test
	void estaAtivoNull() {
		boolean res = UsuarioHelper.estaAtivo(null);
		assertThat(res).isFalse();
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_INATIVO)
	})
	@Test
	void estaAtivoNao() {
		boolean res = UsuarioHelper.estaAtivo(usuario);
		assertThat(res).isFalse();
	}

	@Test
	void estaAtivoSim() {
		boolean res = UsuarioHelper.estaAtivo(usuario);
		assertThat(res).isTrue();
	}
	
	@Test
	void estaSuspensoNull() {
		boolean res = UsuarioHelper.estaSuspenso(null);
		assertThat(res).isTrue();
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_SUSPENSO_ATE_HOJE)
	})
	@Test
	void estaSuspensoSim() {
		boolean res = UsuarioHelper.estaSuspenso(usuario);
		assertThat(res).isTrue();
	}

	@Tags({
		@Tag(TesteConstSaida.BD_USUARIO_SUSPENSO_ATE_ONTEM)
	})
	@Test
	void esteveSuspensoSim() {
		boolean res = UsuarioHelper.estaSuspenso(usuario);
		assertThat(res).isFalse();
	}

	@Test
	void nuncaFoiSuspenso() {
		boolean res = UsuarioHelper.estaSuspenso(usuario);
		assertThat(res).isFalse();
	}

	@Test
	void listaPrivilegiosNull() {
		/*List<GrantedAuthority> lista = UsuarioHelper.listaPrivilegios(null);
		assertThat(lista).isEmpty();*/
	}

	@Tags({
		@Tag(TesteConstSaida.BD_CARGO_PADRAO),
		@Tag(TesteConstSaida.BD_FUNCIONALIDADE_AVALIAR_EVENTO),
	})
	@Test
	void listaPrivilegiosPadrao() {
		/*List<GrantedAuthority> lista = UsuarioHelper.listaPrivilegios(usuario.getCargos());
		assertThat(lista).isNotEmpty();
		GrantedAuthority auth0 = lista.get(0);
		assertThat(auth0.getAuthority()).isEqualTo("ROLE_" + ConstFuncionalidade.KEY_FUNC_AVALIAR_EVENTO);*/
	}
}
