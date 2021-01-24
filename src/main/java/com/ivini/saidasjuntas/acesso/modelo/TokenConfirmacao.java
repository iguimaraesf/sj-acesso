package com.ivini.saidasjuntas.acesso.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TokenConfirmacao {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(length = 36)
	private String idToken;

	@OneToOne(fetch = FetchType.EAGER, targetEntity = Usuario.class)
	@JoinColumn(name = "id_usuario")
	@MapsId
	private Usuario usuario;
	@Column(length = 36, nullable = false, unique = true)
	private String tokenGerado;
}
