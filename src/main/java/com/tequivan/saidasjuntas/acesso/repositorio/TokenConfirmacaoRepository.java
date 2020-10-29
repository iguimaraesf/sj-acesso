package com.tequivan.saidasjuntas.acesso.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tequivan.saidasjuntas.acesso.modelos.TokenConfirmacao;
import com.tequivan.saidasjuntas.acesso.modelos.Usuario;

@Repository
public interface TokenConfirmacaoRepository extends JpaRepository<TokenConfirmacao, Long> {
	TokenConfirmacao findByToken(String token);
	TokenConfirmacao findByUsuario(Usuario usuario);
	boolean existsByUsuario(Usuario usuario);
}
