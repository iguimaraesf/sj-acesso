package com.ivini.saidasjuntas.acesso.servico.dados;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ivini.saidasjuntas.acesso.modelo.Funcionalidade;
import com.ivini.saidasjuntas.acesso.repositorio.FuncionalidadeRepository;

@Service
@Deprecated
public class FuncionalidadeService {
	private final FuncionalidadeRepository funcionalidadeRepository;
	
	@Autowired
	public FuncionalidadeService(FuncionalidadeRepository funcionalidadeRepository) {
		this.funcionalidadeRepository = funcionalidadeRepository;
	}

	public int completarComFuncionalidadesPadrao(Set<Funcionalidade> privilegios) {
		int novos = 0;
		novos = somarNovo(novos, adicionarQuandoNaoExistir(privilegios, ConstFuncionalidade.KEY_FUNC_AVALIAR_EVENTO));
		novos = somarNovo(novos, adicionarQuandoNaoExistir(privilegios, ConstFuncionalidade.KEY_FUNC_AVALIAR_PARTICIPANTE));
		novos = somarNovo(novos, adicionarQuandoNaoExistir(privilegios, ConstFuncionalidade.KEY_FUNC_DENUNCIAR_EVENTO));
		novos = somarNovo(novos, adicionarQuandoNaoExistir(privilegios, ConstFuncionalidade.KEY_FUNC_DENUNCIAR_PARTICIPANTE));
		novos = somarNovo(novos, adicionarQuandoNaoExistir(privilegios, ConstFuncionalidade.KEY_FUNC_ORGANIZAR_EVENTO));
		novos = somarNovo(novos, adicionarQuandoNaoExistir(privilegios, ConstFuncionalidade.KEY_FUNC_PARTICIPAR_EVENTO));
		return novos;
	}

	private int somarNovo(int novos, Funcionalidade func) {
		if (func.getIdFuncionalidade() == null) {
			return 1 + novos;
		}
		return novos;
	}

	private Funcionalidade adicionarQuandoNaoExistir(Set<Funcionalidade> privilegios, String nome) {
		return privilegios.stream()
				.filter(priv -> priv.getNome().equals(nome))
				.findFirst()
				.orElse(adicionarFuncionalidade(privilegios, nome));
	}
	
	private Funcionalidade adicionarFuncionalidade(Set<Funcionalidade> privilegios, String nome) {
		Funcionalidade nova = criarFuncionalidade(nome);
		privilegios.add(nova);
		return nova;
	}

	private Funcionalidade criarFuncionalidade(String nome) {
		return funcionalidadeRepository.findByNome(nome).orElse(new Funcionalidade(null, nome));
	}

	
}
