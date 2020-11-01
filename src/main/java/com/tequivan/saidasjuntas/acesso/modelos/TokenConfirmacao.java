package com.tequivan.saidasjuntas.acesso.modelos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@AllArgsConstructor
@Data
public class TokenConfirmacao {
	@Id
	@Column(nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tokenId;
	@Column(nullable = false, unique = true)
	private String token;
	@OneToOne(fetch = FetchType.EAGER, targetEntity = Usuario.class)
	private Usuario usuario;
}
