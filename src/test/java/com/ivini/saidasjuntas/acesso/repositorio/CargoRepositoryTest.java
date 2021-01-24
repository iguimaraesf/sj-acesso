package com.ivini.saidasjuntas.acesso.repositorio;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ivini.saidasjuntas.acesso.modelo.Cargo;
import com.ivini.saidasjuntas.acesso.modelo.Funcionalidade;

@DataJpaTest
class CargoRepositoryTest {
	@Autowired
	private CargoRepository cargoRep;
	@Autowired
	private FuncionalidadeRepository funcionalidadeRep;

	private Cargo res;
	@BeforeEach
	void setUp() {
		final Set<Funcionalidade> lista = new HashSet<>();
		lista.add(new Funcionalidade(null, "criar_usuario"));
		lista.add(new Funcionalidade(null, "listar_usuarios"));
		final Cargo param = new Cargo(null, "O cargo", lista);
		res = cargoRep.save(param);
		assertThat(res).isNotNull();
	}

	@Test
	void regravacaoNaoDeixaLinhasOrfans() {
		final Cargo param = cargoRep.findById(res.getIdCargo()).get();
		final Set<Funcionalidade> lista = new HashSet<>();
		lista.add(new Funcionalidade(null, "excluir_usuario"));
		param.setPrivilegios(lista);
		final Cargo res1 = cargoRep.save(param);
		
		final Funcionalidade func = funcionalidadeRep.findById(res1.getPrivilegios().iterator().next().getIdFuncionalidade()).get();
		
		assertThat(func.getNome()).isEqualTo("excluir_usuario");
	}
}
