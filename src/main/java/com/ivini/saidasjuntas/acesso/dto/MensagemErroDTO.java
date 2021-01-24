package com.ivini.saidasjuntas.acesso.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MensagemErroDTO {
	private String chave;
	private String descricao;
}
