package com.ivini.saidasjuntas.acesso.modelo;

import java.util.Set;

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
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cargo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_cargo", nullable = false, unique = true)
	private Integer idCargo;
	@Column(length = 40, unique = true)
	@NotEmpty
	private String nome;
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE })
	@JoinTable(
			name = "privilegios",
			joinColumns = @JoinColumn(name = "id_cargo"),
			inverseJoinColumns = @JoinColumn(referencedColumnName = "id_funcionalidade"))
	private Set<Funcionalidade> privilegios;

	@PreRemove
	private void limpeza() {
		if (privilegios != null) {
			this.privilegios.clear();
		}
	}
}
