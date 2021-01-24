package com.ivini.saidasjuntas.acesso.servico.infra;

import org.springframework.beans.factory.annotation.Autowired;

import com.ivini.saidasjuntas.acesso.modelo.Usuario;
import com.ivini.saidasjuntas.acesso.repositorio.TokenConfirmacaoRepository;
import com.ivini.saidasjuntas.acesso.repositorio.UsuarioRepository;

//@Service
public class SaidasUserDetailsService /*implements UserDetailsService*/ {

	private final UsuarioRepository usuarioRep;
	private final TokenConfirmacaoRepository tokenConfirmacaoRep;
	
	@Autowired
	public SaidasUserDetailsService(UsuarioRepository usuarioRep, TokenConfirmacaoRepository tokenConfirmacaoRep) {
		this.usuarioRep = usuarioRep;
		this.tokenConfirmacaoRep = tokenConfirmacaoRep;
	}

	/*@Override
	public UserDetails loadUserByUsername(String username) {
		Usuario usuario = usuarioRep.findByEmail(username).orElse(
				new Usuario(null, username, username, "123123", null, null, null, null, null));
		return new User(username, usuario.getSenha(),
				UsuarioHelper.estaAtivo(usuario), // enabled
				tokenValidado(usuario),	// accountNonExpired
				true, // credentialsNonExpired
				UsuarioHelper.estaSuspenso(usuario), // accountNonLocked
				UsuarioHelper.listaPrivilegios(usuario.getCargos()));
	}*/

	private boolean tokenValidado(Usuario usuario) {
		return tokenConfirmacaoRep.findByUsuarioIdUsuario(usuario.getIdUsuario()).isEmpty();
	}

}
