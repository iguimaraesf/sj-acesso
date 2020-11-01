package com.tequivan.saidasjuntas.acesso.repositorio;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tequivan.saidasjuntas.acesso.modelos.Cargo;

@Repository
public interface CargoRepository extends CrudRepository<Cargo, Integer>{

}
