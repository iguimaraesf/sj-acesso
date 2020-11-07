package com.ivini.saidasjuntas.acesso.modelos;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PreRemove;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cargo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cargo_id", nullable = false, unique = true)
	private Integer cargoId;
	@Column(length = 40, unique = true)
	private String nome;
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE })
	@JoinTable(
			name = "privilegios",
			joinColumns = @JoinColumn(name = "cargo_id"),
			inverseJoinColumns = @JoinColumn(referencedColumnName = "funcionalidade_id"))
	private List<Funcionalidade> privilegios;

	@PreRemove
	private void limpeza() {
		if (privilegios != null) {
			this.privilegios.clear();
		}
	}
}
