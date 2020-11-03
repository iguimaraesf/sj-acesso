package com.ivantex.saidasjuntas.acesso.repositorio;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ivantex.saidasjuntas.acesso.modelos.Cargo;

@Repository
public interface CargoRepository extends CrudRepository<Cargo, Integer>{

}
