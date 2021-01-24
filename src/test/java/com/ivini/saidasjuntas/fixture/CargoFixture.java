package com.ivini.saidasjuntas.fixture;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;

import com.ivini.saidasjuntas.acesso.modelo.Cargo;
import com.ivini.saidasjuntas.acesso.repositorio.CargoRepository;
import com.ivini.saidasjuntas.acesso.repositorio.FuncionalidadeRepository;
import com.ivini.saidasjuntas.acesso.servico.dados.ConstCargo;
import com.ivini.saidasjuntas.acesso.servico.dados.ConstFuncionalidade;
import com.ivini.saidasjuntas.tag.TagSaida;
import com.ivini.saidasjuntas.tag.TesteConstSaida;

public final class CargoFixture {

	private CargoFixture() {
	}

	public static Set<Cargo> criarLista(TestInfo info) {
		if (TagSaida.temTag(info, TesteConstSaida.BD_CARGO_PADRAO)) {
			Set<Cargo> cargos = new HashSet<>();
			cargos.add(criarCargoPadraoComPrivilegios(info));
			return cargos;
		}
		return null;
	}

	private static Cargo criarCargoPadraoComPrivilegios(TestInfo info) {
		return new Cargo(1, ConstCargo.BD_CARGO_USUARIO_PADRAO, FuncionalidadeFixture.criarLista(info));
	}

	public static int configurarRepositorio(TestInfo info, CargoRepository cargoRep, FuncionalidadeRepository funcionalidadeRep) {
		if (TagSaida.temTag(info, TesteConstSaida.BD_CARGO_PADRAO_EXISTE)) {
			Mockito.when(cargoRep.findByNome(ConstCargo.BD_CARGO_USUARIO_PADRAO)).thenReturn(Optional.of(criarCargoPadraoComPrivilegios(info)));
		} else {
			Mockito.when(cargoRep.findByNome(ConstCargo.BD_CARGO_USUARIO_PADRAO)).thenReturn(Optional.empty());
		}
		int qtd = 0;
		qtd = somaCasoNaoDevaEstarNoBancoDeDados(info, funcionalidadeRep, qtd, TesteConstSaida.BD_FUNCIONALIDADE_AVALIAR_EVENTO, ConstFuncionalidade.KEY_FUNC_AVALIAR_EVENTO);
		qtd = somaCasoNaoDevaEstarNoBancoDeDados(info, funcionalidadeRep, qtd, TesteConstSaida.BD_FUNCIONALIDADE_AVALIAR_PARTICIPANTE, ConstFuncionalidade.KEY_FUNC_AVALIAR_PARTICIPANTE);
		qtd = somaCasoNaoDevaEstarNoBancoDeDados(info, funcionalidadeRep, qtd, TesteConstSaida.BD_FUNCIONALIDADE_DENUNCIAR_EVENTO, ConstFuncionalidade.KEY_FUNC_DENUNCIAR_EVENTO);
		qtd = somaCasoNaoDevaEstarNoBancoDeDados(info, funcionalidadeRep, qtd, TesteConstSaida.BD_FUNCIONALIDADE_DENUNCIAR_PARTICIPANTE, ConstFuncionalidade.KEY_FUNC_DENUNCIAR_PARTICIPANTE);
		// qtd = somaCasoNaoDevaEstarNoBancoDeDados(info, funcionalidadeRep, qtd, ConstSaida.BD_FUNCIONALIDADE_EDITAR_DOCUMENTOS, ConstFuncionalidade.KEY_FUNC_);
		qtd = somaCasoNaoDevaEstarNoBancoDeDados(info, funcionalidadeRep, qtd, TesteConstSaida.BD_FUNCIONALIDADE_ORGANIZAR_EVENTO, ConstFuncionalidade.KEY_FUNC_ORGANIZAR_EVENTO);
		qtd = somaCasoNaoDevaEstarNoBancoDeDados(info, funcionalidadeRep, qtd, TesteConstSaida.BD_FUNCIONALIDADE_PARTICIPAR_EVENTO, ConstFuncionalidade.KEY_FUNC_PARTICIPAR_EVENTO);
		return qtd;
	}

	private static int somaCasoNaoDevaEstarNoBancoDeDados(TestInfo info, FuncionalidadeRepository funcionalidadeRep, int qtd, String tag, String key) {
		if (TagSaida.temTag(info, tag)) {
			Mockito.when(funcionalidadeRep.findByNome(key)).thenReturn(Optional.of(FuncionalidadeFixture.criarFuncionalidade(key)));
			return 1 + qtd;
		}
		Mockito.when(funcionalidadeRep.findByNome(key)).thenReturn(Optional.empty());
		return qtd;
	}

	public static void verificarRepositorio(TestInfo info, CargoRepository cargoRep, FuncionalidadeRepository funcionalidadeRep) {
		// TODO Esse trecho é importante para os acessos especiais (vendedor, admin, polícia etc)
		if (TagSaida.metodoDeTesteComecaCom(info, "registrar")) {
			verificarRepositorioAoRegistrar(info, cargoRep, funcionalidadeRep);
		}
	}

	// TODO Esse trecho é importante para os acessos especiais (vendedor, admin, polícia etc)
	private static void verificarRepositorioAoRegistrar(TestInfo info, CargoRepository cargoRep, FuncionalidadeRepository funcionalidadeRep) {
		boolean validacoes = TagSaida.temTag(info, TesteConstSaida.VALIDA_CONFIRMACAO_SENHA_ERRADA,
				TesteConstSaida.VALIDA_EMAIL_DOMINIO_CURTO, 
				TesteConstSaida.VALIDA_EMAIL_SEM_ARROBA, 
				TesteConstSaida.VALIDA_EMAIL_SOMENTE_DOMINIO, 
				TesteConstSaida.VALIDA_NOME_VAZIO, 
				TesteConstSaida.VALIDA_SENHA_CURTA,
				TesteConstSaida.BD_USUARIO_EXISTE_POR_EMAIL);
		boolean cargoExiste = !validacoes && TagSaida.temTag(info, TesteConstSaida.BD_CARGO_PADRAO_EXISTE);
		boolean temFuncionalidades = TagSaida.temTag(info, TesteConstSaida.BD_FUNCIONALIDADE_AVALIAR_EVENTO,
				TesteConstSaida.BD_FUNCIONALIDADE_AVALIAR_PARTICIPANTE,
				TesteConstSaida.BD_FUNCIONALIDADE_DENUNCIAR_EVENTO,
				TesteConstSaida.BD_FUNCIONALIDADE_DENUNCIAR_PARTICIPANTE,
				TesteConstSaida.BD_FUNCIONALIDADE_ORGANIZAR_EVENTO,
				TesteConstSaida.BD_FUNCIONALIDADE_PARTICIPAR_EVENTO);
		if (validacoes) {
			Mockito.verify(cargoRep, Mockito.never()).findByNome(ConstCargo.BD_CARGO_USUARIO_PADRAO);
			Mockito.verify(cargoRep, Mockito.never()).save(Mockito.any());
			Mockito.verifyNoInteractions(funcionalidadeRep);
		} else if (cargoExiste) {
			Mockito.verify(cargoRep).findByNome(ConstCargo.BD_CARGO_USUARIO_PADRAO);
			if (temFuncionalidades) {
				// se tem no banco de dados o cargo e as funcionalidades, não precisa salvar.
				Mockito.verify(cargoRep, Mockito.never()).save(Mockito.any());
			} else {
				Mockito.verify(cargoRep).save(Mockito.any());
			}
		} else {
			Mockito.verify(cargoRep).findByNome(ConstCargo.BD_CARGO_USUARIO_PADRAO);
			Mockito.verify(cargoRep).save(Mockito.any());
		}
		if (!validacoes) {
			verificarChamadaFuncionalidade(funcionalidadeRep, TesteConstSaida.BD_FUNCIONALIDADE_AVALIAR_EVENTO, ConstFuncionalidade.KEY_FUNC_AVALIAR_EVENTO);
			verificarChamadaFuncionalidade(funcionalidadeRep, TesteConstSaida.BD_FUNCIONALIDADE_AVALIAR_PARTICIPANTE, ConstFuncionalidade.KEY_FUNC_AVALIAR_PARTICIPANTE);
			verificarChamadaFuncionalidade(funcionalidadeRep, TesteConstSaida.BD_FUNCIONALIDADE_DENUNCIAR_EVENTO, ConstFuncionalidade.KEY_FUNC_DENUNCIAR_EVENTO);
			verificarChamadaFuncionalidade(funcionalidadeRep, TesteConstSaida.BD_FUNCIONALIDADE_DENUNCIAR_PARTICIPANTE, ConstFuncionalidade.KEY_FUNC_DENUNCIAR_PARTICIPANTE);
			// verificarChamadaFuncionalidade(info, funcionalidadeRep, ConstSaida.BD_FUNCIONALIDADE_EDITAR_DOCUMENTOS, ConstFuncionalidade.KEY_FUNC_);
			verificarChamadaFuncionalidade(funcionalidadeRep, TesteConstSaida.BD_FUNCIONALIDADE_ORGANIZAR_EVENTO, ConstFuncionalidade.KEY_FUNC_ORGANIZAR_EVENTO);
			verificarChamadaFuncionalidade(funcionalidadeRep, TesteConstSaida.BD_FUNCIONALIDADE_PARTICIPAR_EVENTO, ConstFuncionalidade.KEY_FUNC_PARTICIPAR_EVENTO);
		}
	}

	private static void verificarChamadaFuncionalidade(FuncionalidadeRepository funcionalidadeRep, String func, String key) {
		Mockito.verify(funcionalidadeRep).findByNome(key);
	}

}
