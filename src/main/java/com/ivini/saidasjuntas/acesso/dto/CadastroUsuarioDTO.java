package com.ivini.saidasjuntas.acesso.dto;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.ivini.saidasjuntas.acesso.validacao.MesmosValores;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@MesmosValores(message = "{Valido.senha}", primeiro = "senha", segundo = "confirmacaoSenha")
public class CadastroUsuarioDTO extends BaseEmailDTO implements ISenhaComConfirmacao {
	@NotEmpty(message = "{Preenchido.nome}")
	private String nome;
	@Length(min = 6, message = "{Tamanho.minimo}")
	private String senha;
	private String confirmacaoSenha;
}
