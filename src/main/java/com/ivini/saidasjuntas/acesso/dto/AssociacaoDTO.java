package com.ivini.saidasjuntas.acesso.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssociacaoDTO {
	@NotEmpty(message = "{Preenchido.codigo.usuario}")
	private String idUsuario;
	@NotEmpty(message = "{Preenchido.codigo.referencia}")
	private String idReferencia;
	private String mensagem;
}
