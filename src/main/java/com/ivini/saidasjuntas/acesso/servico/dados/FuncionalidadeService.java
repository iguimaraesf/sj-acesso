package com.ivini.saidasjuntas.acesso.servico.dados;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ivini.saidasjuntas.acesso.modelos.Funcionalidade;
import com.ivini.saidasjuntas.acesso.repositorio.FuncionalidadeRepository;

@Service
public class FuncionalidadeService {
	private final FuncionalidadeRepository funcionalidadeRepository;
	
	@Autowired
	public FuncionalidadeService(FuncionalidadeRepository funcionalidadeRepository) {
		this.funcionalidadeRepository = funcionalidadeRepository;
	}

	public void completarComFuncionalidadesPadrao(List<Funcionalidade> privilegios) {
		adicionarQuandoNaoExistir(privilegios, ConstFuncionalidade.BD_FUNC_PARTICIPAR_EVENTO);
		adicionarQuandoNaoExistir(privilegios, ConstFuncionalidade.BD_FUNC_AVALIAR_EVENTO);
		adicionarQuandoNaoExistir(privilegios, ConstFuncionalidade.BD_FUNC_AVALIAR_PARTICIPANTE);
		adicionarQuandoNaoExistir(privilegios, ConstFuncionalidade.BD_FUNC_ORGANIZAR_EVENTO);
	}

	private Funcionalidade adicionarQuandoNaoExistir(List<Funcionalidade> privilegios, String nome) {
		return privilegios.stream()
				.filter(priv -> priv.getNome().equals(nome))
				.findFirst()
				.orElse(adicionarFuncionalidade(privilegios, nome));
	}
	
	private Funcionalidade adicionarFuncionalidade(List<Funcionalidade> privilegios, String nome) {
		Funcionalidade nova = criarFuncionalidade(nome);
		privilegios.add(nova);
		return nova;
	}

	private Funcionalidade criarFuncionalidade(String nome) {
		return funcionalidadeRepository.findByNome(nome).orElse(new Funcionalidade(null, nome, null));
	}

	
}
