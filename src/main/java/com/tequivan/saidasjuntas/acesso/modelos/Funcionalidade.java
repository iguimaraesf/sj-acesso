package com.tequivan.saidasjuntas.acesso.modelos;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PreRemove;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Funcionalidade {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "funcionalidade_id", nullable = false, unique = true)
	private Integer funcionalidadeId;
	@Column(length = 40)
	private String nome;
	@ManyToMany(mappedBy = "privilegios", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE })
	private List<Cargo> cargos;

	@PreRemove
	private void limpeza() {
		if (cargos != null) {
			this.cargos.clear();
		}
	}
}
