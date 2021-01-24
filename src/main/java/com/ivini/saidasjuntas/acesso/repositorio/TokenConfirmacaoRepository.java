package com.ivini.saidasjuntas.acesso.repositorio;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ivini.saidasjuntas.acesso.modelo.TokenConfirmacao;

@Repository
public interface TokenConfirmacaoRepository extends CrudRepository<TokenConfirmacao, String> {
	Optional<TokenConfirmacao> findByTokenGerado(String token);
	Optional<TokenConfirmacao> findByUsuarioIdUsuario(String id);
}
