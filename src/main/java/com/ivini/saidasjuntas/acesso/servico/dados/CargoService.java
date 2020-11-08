package com.ivini.saidasjuntas.acesso.servico.dados;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ivini.saidasjuntas.acesso.modelos.Cargo;
import com.ivini.saidasjuntas.acesso.repositorio.CargoRepository;

@Service
public class CargoService {
	private final CargoRepository repository;
	private final FuncionalidadeService funcionalidadeService;
	
	public CargoService(CargoRepository cargoRepository, FuncionalidadeService funcionalidadeService) {
		this.repository = cargoRepository;
		this.funcionalidadeService = funcionalidadeService;
	}

	@Transactional
	public List<Cargo> cargosUsuarioPadrao() {
		Cargo cargo = repository.findByNome(ConstCargo.BD_CARGO_USUARIO_PADRAO)
				.orElse(new Cargo(null, ConstCargo.BD_CARGO_USUARIO_PADRAO, new ArrayList<>()));
		funcionalidadeService.completarComFuncionalidadesPadrao(cargo.getPrivilegios());
		repository.save(cargo);
		return Arrays.asList(cargo);
	}

}
