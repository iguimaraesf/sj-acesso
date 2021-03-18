package com.ivini.saidasjuntas.acesso.servico.dados;

public enum DominioCargo {
	BD_CARGO_TORNAR_COMERCIANTE("admin:tornar:comerciante"),
	BD_CARGO_MEUS_CLIENTES("colaborador:ver:meus-clientes"),
	BD_FUNC_MARCAR_COMO_SUSPEITO("seguranca:marcar-como-suspeito"),
	BD_CARGO_VALIDAR_CUPOM("vendedor:validar-cupom"),
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
