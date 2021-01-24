package com.ivini.saidasjuntas.acesso.servico.dados;

public enum DominioCargo {
	BD_FUNC_TORNAR_COMERCIANTE("bd:func:tornar-comerciante"),
	BD_FUNC_MEUS_CLIENTES("bd:func:meus-clientes"),
	BD_FUNC_VALIDAR_CUPOM("bd:func:validar-cupom"),
	;

	private String nome;

	DominioCargo(String nome) {
		this.nome = nome;
	}
	
	@Override
	public String toString() {
		return nome;
	}
}
