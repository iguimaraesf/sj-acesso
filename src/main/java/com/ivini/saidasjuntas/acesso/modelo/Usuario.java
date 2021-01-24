package com.ivini.saidasjuntas.acesso.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(length = 36)
	private String idUsuario;
	@Column(length = 255, nullable = false)
	@NotEmpty
	private String nome;
	@Column(length = 255, nullable = false, unique = true)
	@NotEmpty
	@Email
	private String email;
	@Column(length = 60, nullable = false)
	@NotEmpty
	@JsonIgnore
	private String senha;
	@JsonIgnore
	private LocalDate dataFimSuspensao;
	@JsonIgnore
	private LocalDate dataInativacao;
	@ManyToOne(optional = true)
	@JoinColumn(name = "id_empregador", nullable = true)
	private Usuario empregador;
	@CreationTimestamp
	@JsonIgnore
	private LocalDateTime dataCriacao;
	@UpdateTimestamp
	@JsonIgnore
	private LocalDateTime dataAtualizacao;
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JsonIgnore
	private Set<Cargo> cargos;

	@PreRemove
	private void limpeza() {
		if (cargos != null) {
			this.cargos.clear();
		}
	}
}
