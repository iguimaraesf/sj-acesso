package com.ivini.saidasjuntas.fixture;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;

import com.ivini.saidasjuntas.acesso.modelo.Cargo;
import com.ivini.saidasjuntas.acesso.modelo.Funcionalidade;
import com.ivini.saidasjuntas.acesso.repositorio.CargoRepository;
import com.ivini.saidasjuntas.acesso.repositorio.FuncionalidadeRepository;
import com.ivini.saidasjuntas.acesso.servico.dados.ConstCargo;
import com.ivini.saidasjuntas.acesso.servico.dados.ConstFuncionalidade;
import com.ivini.saidasjuntas.acesso.servico.dados.DominioCargo;
import com.ivini.saidasjuntas.acesso.servico.dados.DominioFuncionalidade;
import com.ivini.saidasjuntas.tag.TagSaida;
import com.ivini.saidasjuntas.tag.TesteConstSaida;

public final class CargoFixture {

	private CargoFixture() {
	}

	@Deprecated
	public static Set<Cargo> criarLista(TestInfo info) {
		if (TagSaida.temTag(info, TesteConstSaida.BD_CARGO_PADRAO)) {
			Set<Cargo> cargos = new HashSet<>();
			cargos.add(criarCargoComPrivilegios(info));
			return cargos;
		}
		return new HashSet<>();
	}

	@Deprecated
	private static Cargo criarCargoComPrivilegios(TestInfo info) {
		return new Cargo(1, ConstCargo.BD_CARGO_USUARIO_PADRAO, FuncionalidadeFixture.criarLista(info));
	}

	public static Set<Cargo> criarCargos(TestInfo info) {
		if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_CARGO_NULO)) {
			return null;
		}
		Set<Cargo> cargos = new HashSet<>();
		if (TagSaida.temTag(info, TesteConstSaida.BD_CARGO_TORNAR_COMERCIANTE)) {
			cargos.add(cargoComerciante());
			cargos.add(cargoMeusClientes());
		}
		return cargos;
	}
	
	private static Cargo cargoComerciante() {
		return criarCargoComPrivilegios(DominioCargo.BD_CARGO_TORNAR_COMERCIANTE,
				DominioFuncionalidade.KEY_TORNAR_COMERCIANTE_ATIVAR, 
				DominioFuncionalidade.KEY_TORNAR_COMERCIANTE_BUSCAR_POR_EMAIL, 
				DominioFuncionalidade.KEY_TORNAR_COMERCIANTE_TROCAR,
				DominioFuncionalidade.KEY_TORNAR_COMERCIANTE_INATIVAR);
	}
	
	private static Cargo cargoMeusClientes() {
		return criarCargoComPrivilegios(DominioCargo.BD_CARGO_MEUS_CLIENTES,
				DominioFuncionalidade.KEY_MEUS_CLIENTES_ULTIMA_COMPRA, 
				DominioFuncionalidade.KEY_MEUS_CLIENTES_CONTATOS);
	}
	
	private static Cargo cargoValidarCupom() {
		return criarCargoComPrivilegios(DominioCargo.BD_CARGO_VALIDAR_CUPOM,
				DominioFuncionalidade.KEY_VALIDAR_CUPOM_DIGITAR,
				DominioFuncionalidade.KEY_VALIDAR_CUPOM_ESCANEAR);
	}

	public static void configurarRepositorio(TestInfo info, CargoRepository cargoRep) {
		if (TagSaida.temTag(info, TesteConstSaida.BD_CARGO_TORNAR_COMERCIANTE)) {
			Mockito.when(cargoRep.findByNome(DominioCargo.BD_CARGO_TORNAR_COMERCIANTE.toString()))
				.thenReturn(Optional.of(cargoComerciante()));
			Mockito.when(cargoRep.findByNome(DominioCargo.BD_CARGO_MEUS_CLIENTES.toString()))
				.thenReturn(Optional.of(cargoMeusClientes()));
		}
		if (TagSaida.temTag(info, TesteConstSaida.BD_CARGO_VALIDAR_CUPOM)) {
			Mockito.when(cargoRep.findByNome(DominioCargo.BD_CARGO_VALIDAR_CUPOM.toString()))
				.thenReturn(Optional.of(cargoValidarCupom()));
		}
	}

	private static Cargo criarCargoComPrivilegios(DominioCargo cargoP, DominioFuncionalidade... funcs) {
		Cargo cargo = new Cargo();
		cargo.setIdCargo(1);
		cargo.setNome(cargoP.toString());
		Set<Funcionalidade> asTaisFuncionalidades = new HashSet<>();
		for (DominioFuncionalidade funcX : funcs) {
			asTaisFuncionalidades.add(new Funcionalidade(1, funcX.toString()));
		}
		cargo.setPrivilegios(asTaisFuncionalidades);
		return cargo;
	}

	public static void verificarRepositorio(TestInfo info, CargoRepository cargoRep) {
		if (TagSaida.temTag(info, TesteConstSaida.BD_CARGO_TORNAR_COMERCIANTE)) {
			Mockito.verify(cargoRep).findByNome(DominioCargo.BD_CARGO_TORNAR_COMERCIANTE.toString());
		}
		if (TagSaida.temTag(info, TesteConstSaida.BD_CARGO_MEUS_CLIENTES)) {
			Mockito.verify(cargoRep).findByNome(DominioCargo.BD_CARGO_MEUS_CLIENTES.toString());
		}
		if (TagSaida.temTag(info, TesteConstSaida.BD_CARGO_VALIDAR_CUPOM)) {
			Mockito.verify(cargoRep).findByNome(DominioCargo.BD_CARGO_VALIDAR_CUPOM.toString());
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
