package com.ivini.saidasjuntas.acesso.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListaMensagensRetornoDTO {
	private List<MensagemErroDTO> mensagens;
	private LocalDateTime momento;
	private Object complemento;
}
