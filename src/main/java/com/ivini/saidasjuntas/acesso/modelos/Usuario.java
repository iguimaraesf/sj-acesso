package com.ivini.saidasjuntas.acesso.modelos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PreRemove;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
public class Usuario {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(length = 32)
	private String usuarioId;
	@Column(length = 255, nullable = false)
	private String nome;
	@Column(length = 255, nullable = false, unique = true)
	private String email;
	@Column(length = 25, nullable = false)
	@JsonIgnore
	private String senha;
	@JsonIgnore
	private LocalDate dataFimSuspensao;
	@JsonIgnore
	private LocalDate dataInativacao;
	@CreationTimestamp
	@JsonIgnore
	private LocalDateTime dataCriacao;
	@UpdateTimestamp
	@JsonIgnore
	private LocalDateTime dataAtualizacao;
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JsonIgnore
	private List<Cargo> cargos;

	@PreRemove
	private void limpeza() {
		if (cargos != null) {
			this.cargos.clear();
		}
	}
}
