package com.ivantex.saidasjuntas.acesso.repositorio;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ivantex.saidasjuntas.acesso.modelos.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, String> {
	boolean existsByEmail(String email);
	Optional<Usuario> findByEmail(String email);
}
