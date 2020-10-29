package com.tequivan.saidasjuntas.acesso.repositorio;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tequivan.saidasjuntas.acesso.modelos.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, String> {
	boolean existsByNumeroDocumento(String numeroDocumento);
	boolean existsByEmail(String email);
	Optional<Usuario> findByEmailOrNumeroDocumento(String emailOuDocumento);
}
