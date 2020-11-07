package com.ivini.saidasjuntas.acesso.servico.dados;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ivini.saidasjuntas.acesso.modelos.Funcionalidade;
import com.ivini.saidasjuntas.acesso.repositorio.FuncionalidadeRepository;
import com.ivini.saidasjuntas.fixture.TagSaida;

@ExtendWith(MockitoExtension.class)
class FuncionalidadeServiceTest {
	private List<Funcionalidade> privilegios;

	@Mock
	private FuncionalidadeRepository funcionalidadeRepository;
	
	@InjectMocks
	private FuncionalidadeService service;
	
	@BeforeEach
	void setUp(TestInfo info) {
		mockFuncionalidade(info, TagSaida.FUNCIONALIDADE_ENCONTRA_PARTICIPAR_EVENTO, ConstFuncionalidade.BD_FUNC_PARTICIPAR_EVENTO);
		mockFuncionalidade(info, TagSaida.FUNCIONALIDADE_ENCONTRA_AVALIAR_EVENTO, ConstFuncionalidade.BD_FUNC_AVALIAR_EVENTO);
		mockFuncionalidade(info, TagSaida.FUNCIONALIDADE_ENCONTRA_AVALIAR_PARTICIPANTE, ConstFuncionalidade.BD_FUNC_AVALIAR_PARTICIPANTE);
		mockFuncionalidade(info, TagSaida.FUNCIONALIDADE_ENCONTRA_ORGANIZAR_EVENTO, ConstFuncionalidade.BD_FUNC_ORGANIZAR_EVENTO);
		
		privilegios = new ArrayList<>();
		service.completarComFuncionalidadesPadrao(privilegios);
		Mockito.verify(funcionalidadeRepository).findByNome(ConstFuncionalidade.BD_FUNC_AVALIAR_EVENTO);
		Mockito.verify(funcionalidadeRepository).findByNome(ConstFuncionalidade.BD_FUNC_AVALIAR_PARTICIPANTE);
		Mockito.verify(funcionalidadeRepository).findByNome(ConstFuncionalidade.BD_FUNC_ORGANIZAR_EVENTO);
		Mockito.verify(funcionalidadeRepository).findByNome(ConstFuncionalidade.BD_FUNC_PARTICIPAR_EVENTO);
	}

	private void mockFuncionalidade(TestInfo info, String tag, String nome) {
		if (TagSaida.temTag(info, tag)) {
			Mockito.when(funcionalidadeRepository.findByNome(nome))
				.thenReturn(Optional.of(new Funcionalidade(null, nome, null)));
		} else {
			Mockito.when(funcionalidadeRepository.findByNome(nome))
				.thenReturn(Optional.empty());
		}
	}

	@Test
	void semFuncionalidades() {
		assertThat(privilegios).hasSize(4);
	}

	@Tags({
		@Tag(TagSaida.FUNCIONALIDADE_ENCONTRA_PARTICIPAR_EVENTO),
	})
	@Test
	void temFuncionalidadeParticiparEvento() {
		assertThat(privilegios).hasSize(4);
	}

	@Tags({
		@Tag(TagSaida.FUNCIONALIDADE_ENCONTRA_PARTICIPAR_EVENTO),
		@Tag(TagSaida.FUNCIONALIDADE_ENCONTRA_AVALIAR_EVENTO),
	})
	@Test
	void temFuncionalidadesParticiparEventoEAvaliarEvento() {
		assertThat(privilegios).hasSize(4);
	}

	@Tags({
		@Tag(TagSaida.FUNCIONALIDADE_ENCONTRA_PARTICIPAR_EVENTO),
		@Tag(TagSaida.FUNCIONALIDADE_ENCONTRA_AVALIAR_EVENTO),
		@Tag(TagSaida.FUNCIONALIDADE_ENCONTRA_AVALIAR_PARTICIPANTE),
	})
	@Test
	void temFuncionalidadesParticiparEventoAvaliarEventoEAvaliarParticipante() {
		assertThat(privilegios).hasSize(4);
	}

	@Tags({
		@Tag(TagSaida.FUNCIONALIDADE_ENCONTRA_PARTICIPAR_EVENTO),
		@Tag(TagSaida.FUNCIONALIDADE_ENCONTRA_AVALIAR_EVENTO),
		@Tag(TagSaida.FUNCIONALIDADE_ENCONTRA_AVALIAR_PARTICIPANTE),
		@Tag(TagSaida.FUNCIONALIDADE_ENCONTRA_ORGANIZAR_EVENTO),
	})
	@Test
	void temFuncionalidadesParticiparEventoAvaliarEventoAvaliarParticipanteEOrganizarEvento() {
		assertThat(privilegios).hasSize(4);
	}
}
