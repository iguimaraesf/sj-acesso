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
	private final CargoRepository cargoRepository;
	private final FuncionalidadeService funcionalidadeService;
	
	public CargoService(CargoRepository cargoRepository, FuncionalidadeService funcionalidadeService) {
		this.cargoRepository = cargoRepository;
		this.funcionalidadeService = funcionalidadeService;
	}

	@Transactional
	public List<Cargo> cargosUsuarioPadrao() {
		Cargo cargo = cargoRepository.findByNome("bd:usuario-padrao")
				.orElse(new Cargo(null, "bd:usuario-padrao", new ArrayList<>()));
		funcionalidadeService.completarComFuncionalidadesPadrao(cargo.getPrivilegios());
		cargoRepository.save(cargo);
		return Arrays.asList(cargo);
	}

}
