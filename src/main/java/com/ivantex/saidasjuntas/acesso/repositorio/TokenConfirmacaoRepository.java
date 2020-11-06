package com.ivantex.saidasjuntas.acesso.repositorio;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ivantex.saidasjuntas.acesso.modelos.TokenConfirmacao;
import com.ivantex.saidasjuntas.acesso.modelos.Usuario;

@Repository
public interface TokenConfirmacaoRepository extends CrudRepository<TokenConfirmacao, Long> {
	Optional<TokenConfirmacao> findByToken(String token);
	Optional<TokenConfirmacao> findByUsuario(Usuario usuario);
	boolean existsByUsuario(Usuario usuario);
	void deleteByUsuario(Usuario usuario);
}
