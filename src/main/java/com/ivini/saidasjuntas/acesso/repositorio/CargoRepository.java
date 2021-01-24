package com.ivini.saidasjuntas.acesso.repositorio;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ivini.saidasjuntas.acesso.modelo.Cargo;

@Repository
public interface CargoRepository extends CrudRepository<Cargo, Integer>{
	Optional<Cargo> findByNome(String nome);
}
