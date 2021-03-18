package com.ivini.saidasjuntas.fixture;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;

import com.ivini.saidasjuntas.acesso.modelo.Funcionalidade;
import com.ivini.saidasjuntas.acesso.repositorio.FuncionalidadeRepository;
import com.ivini.saidasjuntas.acesso.servico.dados.ConstFuncionalidade;
import com.ivini.saidasjuntas.tag.TagSaida;
import com.ivini.saidasjuntas.tag.TesteConstSaida;

public final class FuncionalidadeFixture {
	private FuncionalidadeFixture() {
	}

	@Deprecated
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

	@Deprecated
	public static Funcionalidade criarFuncionalidade(String nome) {
		return new Funcionalidade(1, nome);
	}

	public static void configurarRepositorio(TestInfo info, FuncionalidadeRepository funcionalidadeRep) {
		Mockito.when(funcionalidadeRep.findByNome(Mockito.anyString())).thenReturn(Optional.empty());
	}

	public static void verificarRepositorio(TestInfo info, FuncionalidadeRepository funcionalidadeRep) {
		Mockito.verify(funcionalidadeRep).findByNome(Mockito.anyString());
	}

}
