package com.ivini.saidasjuntas.acesso.servico.dados;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ivini.saidasjuntas.acesso.modelo.Cargo;
import com.ivini.saidasjuntas.acesso.repositorio.CargoRepository;

@Deprecated
@Service
public class CargoService {
	private final CargoRepository cargoRep;
	private final FuncionalidadeService funcionalidadeService;

	@Autowired
	public CargoService(CargoRepository cargoRepository, FuncionalidadeService funcionalidadeService) {
		this.cargoRep = cargoRepository;
		this.funcionalidadeService = funcionalidadeService;
	}

	@Deprecated
	// Se é usuário comum, não tem acessos especiais.
	@Transactional
	public List<Cargo> cargosUsuarioPadrao() {
		Cargo cargo = cargoRep.findByNome(ConstCargo.BD_CARGO_USUARIO_PADRAO)
				.orElse(new Cargo(null, ConstCargo.BD_CARGO_USUARIO_PADRAO, new HashSet<>()));
		int novos = funcionalidadeService.completarComFuncionalidadesPadrao(cargo.getPrivilegios());
		if (cargo.getIdCargo() == null || novos > 0) {
			cargoRep.save(cargo);
		}
		return Arrays.asList(cargo);
	}

}
