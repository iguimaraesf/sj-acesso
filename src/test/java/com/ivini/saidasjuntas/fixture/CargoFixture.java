package com.ivini.saidasjuntas.fixture;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.TestInfo;

import com.ivini.saidasjuntas.acesso.modelos.Cargo;
import com.ivini.saidasjuntas.acesso.servico.dados.ConstCargo;

public final class CargoFixture {
	private CargoFixture() {
	}

	public static List<Cargo> criarLista(TestInfo info) {
		if (TagSaida.temTag(info, TagSaida.MD_CARGO_PADRAO)) {
			List<Cargo> cargos = new ArrayList<>();
			cargos.add(criarCargoPadraoComPrivilegios(info));
			return cargos;
		}
		return null;
	}

	private static Cargo criarCargoPadraoComPrivilegios(TestInfo info) {
		return new Cargo(1, ConstCargo.BD_CARGO_USUARIO_PADRAO, FuncionalidadeFixture.criarLista(info));
	}

}
