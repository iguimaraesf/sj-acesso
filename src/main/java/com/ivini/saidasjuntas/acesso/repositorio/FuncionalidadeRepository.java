package com.ivini.saidasjuntas.acesso.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ivini.saidasjuntas.acesso.modelos.Funcionalidade;

@Repository
public interface FuncionalidadeRepository extends JpaRepository<Funcionalidade, Integer> {
	void deleteByNome(String nome);
	Optional<Funcionalidade> findByNome(String nome);
}
