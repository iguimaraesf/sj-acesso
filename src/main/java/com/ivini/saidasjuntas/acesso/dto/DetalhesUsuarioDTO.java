package com.ivini.saidasjuntas.acesso.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class DetalhesUsuarioDTO {
	private String id;
	@NotEmpty
	private String nome;
	@Email
	private String email;
	private String senha;
	private boolean confirmado;
	private boolean ativo;
	private boolean suspenso;

}
