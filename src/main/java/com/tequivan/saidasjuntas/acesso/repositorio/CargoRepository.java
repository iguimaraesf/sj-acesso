package com.tequivan.saidasjuntas.acesso.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tequivan.saidasjuntas.acesso.modelos.Cargo;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Integer>{

}
