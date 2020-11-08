package com.ivini.saidasjuntas.fixture;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.TestInfo;

import com.ivini.saidasjuntas.acesso.modelos.Funcionalidade;
import com.ivini.saidasjuntas.acesso.servico.dados.ConstFuncionalidade;

public final class FuncionalidadeFixture {
	private FuncionalidadeFixture() {
	}

	public static List<Funcionalidade> criarLista(TestInfo info) {
		if (TagSaida.temTag(info,
				TagSaida.MD_FUNCIONALIDADE_PARTICIPAR_EVENTO, TagSaida.MD_FUNCIONALIDADE_AVALIAR_EVENTO,
				TagSaida.MD_FUNCIONALIDADE_AVALIAR_PARTICIPANTE, TagSaida.MD_FUNCIONALIDADE_ORGANIZAR_EVENTO,
				TagSaida.MD_FUNCIONALIDADE_EDITAR_DOCUMENTOS)) {
			List<Funcionalidade> funcs = new ArrayList<>();
			if (TagSaida.temTag(info, TagSaida.MD_FUNCIONALIDADE_AVALIAR_EVENTO)) {
				funcs.add(criarFuncionalidade(ConstFuncionalidade.KEY_FUNC_AVALIAR_EVENTO));
			}
			if (TagSaida.temTag(info, TagSaida.MD_FUNCIONALIDADE_AVALIAR_PARTICIPANTE)) {
				funcs.add(criarFuncionalidade(ConstFuncionalidade.KEY_FUNC_AVALIAR_PARTICIPANTE));
			}
			if (TagSaida.temTag(info, TagSaida.MD_FUNCIONALIDADE_EDITAR_DOCUMENTOS)) {
				funcs.add(criarFuncionalidade(ConstFuncionalidade.BD_FUNC_EDITAR_DOCUMENTOS));
			}
			if (TagSaida.temTag(info, TagSaida.MD_FUNCIONALIDADE_ORGANIZAR_EVENTO)) {
				funcs.add(criarFuncionalidade(ConstFuncionalidade.KEY_FUNC_ORGANIZAR_EVENTO));
			}
			if (TagSaida.temTag(info, TagSaida.MD_FUNCIONALIDADE_PARTICIPAR_EVENTO)) {
				funcs.add(criarFuncionalidade(ConstFuncionalidade.KEY_FUNC_PARTICIPAR_EVENTO));
			}
			return funcs;
		}
		return null;
	}

	private static Funcionalidade criarFuncionalidade(String nome) {
		return new Funcionalidade((int)(Math.random() * 99), nome, null);
	}
}
