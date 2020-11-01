package com.tequivan.saidasjuntas.acesso.repositorio;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tequivan.saidasjuntas.acesso.modelos.TokenConfirmacao;
import com.tequivan.saidasjuntas.acesso.modelos.Usuario;

@Repository
public interface TokenConfirmacaoRepository extends CrudRepository<TokenConfirmacao, Long> {
	TokenConfirmacao findByToken(String token);
	TokenConfirmacao findByUsuario(Usuario usuario);
	boolean existsByUsuario(Usuario usuario);
}
