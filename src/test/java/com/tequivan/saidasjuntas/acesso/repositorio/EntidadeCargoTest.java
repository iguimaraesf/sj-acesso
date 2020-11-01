package com.tequivan.saidasjuntas.acesso.repositorio;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.tequivan.saidasjuntas.acesso.modelos.Cargo;
import com.tequivan.saidasjuntas.acesso.modelos.Funcionalidade;

@DataJpaTest
class EntidadeCargoTest {
	@Autowired
	private CargoRepository cargoRep;
	@Autowired
	private FuncionalidadeRepository funcionalidadeRep;

	private Cargo res;
	@BeforeEach
	void setUp() {
		final List<Funcionalidade> lista = new ArrayList<>();
		lista.add(new Funcionalidade(null, "criar_usuario", null));
		lista.add(new Funcionalidade(null, "listar_usuarios", null));
		final Cargo param = new Cargo(null, "O cargo", lista);
		res = cargoRep.save(param);
		// argoRep.flush();
		assertThat(res).isNotNull();
	}

	//@Test
	void gravaCargoCom2FuncionalidadesComSucesso() {
		/*final List<Funcionalidade> funcionalidades = funcionalidadeRep.findAllById(res.getPrivilegios().stream()
				.map(Funcionalidade::getFuncionalidadeId)
				.collect(Collectors.toList()));
		
		assertThat(funcionalidades).containsAll(res.getPrivilegios());*/
	}
	
	@Test
	void regravacaoNaoDeixaLinhasOrfans() {
		final Cargo param = cargoRep.findById(res.getCargoId()).get();
		final List<Funcionalidade> lista = new ArrayList<>();
		lista.add(new Funcionalidade(null, "excluir_usuario", null));
		param.setPrivilegios(lista);
		final Cargo res1 = cargoRep.save(param);
		
		final Funcionalidade func = funcionalidadeRep.findById(res1.getPrivilegios().iterator().next().getFuncionalidadeId()).get();
		
		assertThat(func.getNome()).isEqualTo("excluir_usuario");
	}
	
	@Test
	void excluirRelacao() {
		res.getPrivilegios().remove(0);
		funcionalidadeRep.deleteByNome("criar_usuario");
		// funcionalidadeRep.flush();
		final Cargo param = cargoRep.findById(res.getCargoId()).get();
		assertThat(param.getPrivilegios()).hasSize(1);
		assertThat(param.getPrivilegios().get(0).getNome()).isEqualTo("listar_usuarios");
	}
}
