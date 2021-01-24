package com.ivini.saidasjuntas.acesso.servico.dados;

public enum DominioFuncionalidade {
	KEY_TORNAR_COMERCIANTE_BUSCAR_POR_EMAIL("tornar-comerciante:buscar-por-email"),
	KEY_TORNAR_COMERCIANTE_ATIVAR("tornar-comerciante:ativar"),
	KEY_TORNAR_COMERCIANTE_INATIVAR("tornar-comerciante:inativar"),
	//
	KEY_MEUS_CLIENTES_ULTIMA_COMPRA("meus-clientes:ultima-compra"),
	KEY_MEUS_CLIENTES_CONTATOS("meus-clientes:contatos"),
	
	KEY_VALIDAR_CUPOM_ESCANEAR("validar-cupom:escanear"),
	KEY_VALIDAR_CUPOM_DIGITAR("validar-cupom:digitar"),
	;

	private String nome;

	DominioFuncionalidade(String nome) {
		this.nome = nome;
	}
	
	@Override
	public String toString() {
		return nome;
	}

}
