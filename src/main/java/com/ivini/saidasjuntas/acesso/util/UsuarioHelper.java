package com.ivini.saidasjuntas.acesso.util;

import java.time.LocalDate;
import java.util.Optional;

import com.ivini.saidasjuntas.acesso.dto.DetalhesUsuarioDTO;
import com.ivini.saidasjuntas.acesso.modelo.TokenConfirmacao;
import com.ivini.saidasjuntas.acesso.modelo.Usuario;

public final class UsuarioHelper {
	public static final int DIAS_SUSPENSAO =  15;

	private UsuarioHelper() {
	}
	
	public static DetalhesUsuarioDTO toDTO(Usuario usuario, Optional<TokenConfirmacao> token) {
		DetalhesUsuarioDTO dto = new DetalhesUsuarioDTO();
		dto.setId(usuario.getIdUsuario());
		dto.setNome(usuario.getNome());
		dto.setEmail(usuario.getEmail());
		dto.setSenha(usuario.getSenha());
		dto.setAtivo(estaAtivo(usuario));
		dto.setConfirmado(token.isEmpty());
		dto.setSuspenso(estaSuspenso(usuario));
		// Privilégios serão consultados apenas no back-end. Não serão retornados para o front ao buscar os usuários.
		return dto;
	}

	/*public static List<GrantedAuthority> listaPrivilegios(List<Cargo> cargos) {
		List<GrantedAuthority> res = new ArrayList<>();
		if (cargos != null) {
			cargos.forEach(carg -> res.addAll(toGrantedAuthority(carg.getPrivilegios())));
		}
		return res;
	}

	private static List<GrantedAuthority> toGrantedAuthority(List<Funcionalidade> privilegios) {
		return privilegios.stream().map(UsuarioHelper::toAuth).collect(Collectors.toList());
	}

	private static GrantedAuthority toAuth(Funcionalidade func) {
		return new SimpleGrantedAuthority("ROLE_" + func.getNome());
	}*/

	public static boolean estaSuspenso(Usuario usuario) {
		return Optional.ofNullable(usuario).map(usu -> !eAnteriorDataAtual(usu.getDataFimSuspensao())).orElse(true);
	}
	
	private static boolean eAnteriorDataAtual(LocalDate dataFimSuspensao) {
		return Optional.ofNullable(dataFimSuspensao).map(data -> data.isBefore(LocalDate.now())).orElse(true);
	}

	public static boolean estaAtivo(Usuario usuario) {
		return Optional.ofNullable(usuario).map(usu -> usu.getDataInativacao() == null).orElse(false);
	}


}
