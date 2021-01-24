package com.ivini.saidasjuntas.acesso.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern.Flag;

import lombok.Data;

@Data
public class BaseEmailDTO {
	@Email(message = "{Valido.email}", regexp = "^[a-z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-z0-9]+\\.[a-z0-9]{2,}(\\.[a-z0-9]+)*$", flags = { Flag.CASE_INSENSITIVE, Flag.DOTALL })
	private String email;

}
