package com.ivantex.saidasjuntas.acesso.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ivantex.saidasjuntas.acesso.modelos.Funcionalidade;

@Repository
public interface FuncionalidadeRepository extends JpaRepository<Funcionalidade, Integer> {
	void deleteByNome(String nome);
}