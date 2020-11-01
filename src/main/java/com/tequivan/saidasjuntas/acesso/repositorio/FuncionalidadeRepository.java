package com.tequivan.saidasjuntas.acesso.repositorio;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tequivan.saidasjuntas.acesso.modelos.Funcionalidade;

@Repository
public interface FuncionalidadeRepository extends CrudRepository<Funcionalidade, Integer> {
	void deleteByNome(String nome);
}
