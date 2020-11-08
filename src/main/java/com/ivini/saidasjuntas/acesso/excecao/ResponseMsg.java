package com.ivini.saidasjuntas.acesso.excecao;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseMsg {
	private String mensagem;
	private LocalDateTime momento;
}
