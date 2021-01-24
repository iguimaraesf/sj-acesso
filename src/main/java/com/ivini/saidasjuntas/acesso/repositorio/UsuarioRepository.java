package com.ivini.saidasjuntas.acesso.repositorio;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.ivini.saidasjuntas.acesso.modelo.Usuario;

@Repository
public interface UsuarioRepository extends PagingAndSortingRepository<Usuario, String> {
	boolean existsByEmail(String email);
	Optional<Usuario> findByEmail(String email);
	// List<Usuario> findByNomeLike(String nome);
}
