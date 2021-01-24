package com.ivini.saidasjuntas.fixture;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.TestInfo;

import com.ivini.saidasjuntas.acesso.modelo.Funcionalidade;
import com.ivini.saidasjuntas.acesso.servico.dados.ConstFuncionalidade;
import com.ivini.saidasjuntas.tag.TagSaida;
import com.ivini.saidasjuntas.tag.TesteConstSaida;

public final class FuncionalidadeFixture {
	private FuncionalidadeFixture() {
	}

	public static Set<Funcionalidade> criarLista(TestInfo info) {
		Set<Funcionalidade> funcs = new HashSet<>();
		if (TagSaida.temTag(info, TesteConstSaida.BD_FUNCIONALIDADE_AVALIAR_EVENTO)) {
			funcs.add(criarFuncionalidade(ConstFuncionalidade.KEY_FUNC_AVALIAR_EVENTO));
		}
		if (TagSaida.temTag(info, TesteConstSaida.BD_FUNCIONALIDADE_AVALIAR_PARTICIPANTE)) {
			funcs.add(criarFuncionalidade(ConstFuncionalidade.KEY_FUNC_AVALIAR_PARTICIPANTE));
		}
		if (TagSaida.temTag(info, TesteConstSaida.BD_FUNCIONALIDADE_DENUNCIAR_EVENTO)) {
			funcs.add(criarFuncionalidade(ConstFuncionalidade.KEY_FUNC_DENUNCIAR_EVENTO));
		}
		if (TagSaida.temTag(info, TesteConstSaida.BD_FUNCIONALIDADE_DENUNCIAR_PARTICIPANTE)) {
			funcs.add(criarFuncionalidade(ConstFuncionalidade.KEY_FUNC_DENUNCIAR_PARTICIPANTE));
		}
		if (TagSaida.temTag(info, TesteConstSaida.BD_FUNCIONALIDADE_EDITAR_DOCUMENTOS)) {
			funcs.add(criarFuncionalidade(ConstFuncionalidade.BD_FUNC_EDITAR_DOCUMENTOS));
		}
		if (TagSaida.temTag(info, TesteConstSaida.BD_FUNCIONALIDADE_ORGANIZAR_EVENTO)) {
			funcs.add(criarFuncionalidade(ConstFuncionalidade.KEY_FUNC_ORGANIZAR_EVENTO));
		}
		if (TagSaida.temTag(info, TesteConstSaida.BD_FUNCIONALIDADE_PARTICIPAR_EVENTO)) {
			funcs.add(criarFuncionalidade(ConstFuncionalidade.KEY_FUNC_PARTICIPAR_EVENTO));
		}
		return funcs;
	}

	public static Funcionalidade criarFuncionalidade(String nome) {
		return new Funcionalidade(1, nome);
	}

}
