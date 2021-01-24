package com.ivini.saidasjuntas.acesso.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Funcionalidade {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_funcionalidade", nullable = false, unique = true)
	private Integer idFuncionalidade;
	@Column(length = 40,  unique = true)
	@NotEmpty
	private String nome;
	//@ManyToMany(mappedBy = "privilegios", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE })
	//private List<Cargo> cargos;

	/*@PreRemove
	private void limpeza() {
		if (cargos != null) {
			this.cargos.clear();
		}
	}*/
}
