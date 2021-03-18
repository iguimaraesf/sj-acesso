package com.ivini.saidasjuntas.acesso.servico.dados;

public enum DominioFuncionalidade {
	KEY_TORNAR_COMERCIANTE_BUSCAR_POR_EMAIL("tornar:comerciante:buscar-por-email"),
	KEY_TORNAR_COMERCIANTE_ATIVAR("tornar:comerciante:ativar"),
	KEY_TORNAR_COMERCIANTE_INATIVAR("tornar:comerciante:inativar"),
	KEY_TORNAR_COMERCIANTE_TROCAR("tornar:comerciante:trocar"),
	//
	KEY_MEUS_CLIENTES_ULTIMA_COMPRA("meus-clientes:ultima-compra"),
	KEY_MEUS_CLIENTES_CONTATOS("meus-clientes:contatos"),
	//
	KEY_VALIDAR_CUPOM_ESCANEAR("cupom:validar:escanear"),
	KEY_VALIDAR_CUPOM_DIGITAR("cupom:validar:digitar"),
	//
	KEY_GRAVAR_SUSPEITA("seguranca:gravar-como-suspeita"),
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
