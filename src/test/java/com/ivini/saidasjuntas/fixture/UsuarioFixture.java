package com.ivini.saidasjuntas.fixture;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.ivini.saidasjuntas.acesso.dto.CadastroUsuarioDTO;
import com.ivini.saidasjuntas.acesso.dto.CredenciaisDTO;
import com.ivini.saidasjuntas.acesso.modelo.Usuario;
import com.ivini.saidasjuntas.acesso.repositorio.TokenConfirmacaoRepository;
import com.ivini.saidasjuntas.acesso.repositorio.UsuarioRepository;
import com.ivini.saidasjuntas.config.SenhaConfig;
import com.ivini.saidasjuntas.tag.TagSaida;
import com.ivini.saidasjuntas.tag.TesteConstSaida;

public class UsuarioFixture {

	private UsuarioFixture() {
	}

	public static Usuario usuarioComId(CadastroUsuarioDTO param, SenhaConfig senhaConfig) {
		Usuario usuario = usuarioSemId(param, senhaConfig);
		usuario.setIdUsuario("0001-0000");
		return usuario;
	}

	public static Usuario usuarioSemId(CadastroUsuarioDTO param, SenhaConfig senhaConfig) {
		Usuario usuario = new Usuario();
		usuario.setEmail(param.getEmail());
		usuario.setNome(param.getNome());
		//usuario.setSenha(senhaConfig.encoder().encode(param.getSenha()));
		usuario.setSenha(param.getSenha());

		usuario.setCargos(Collections.emptySet());
		usuario.setDataCriacao(LocalDateTime.now());
		return usuario;
	}

	public static Usuario criarUsuario(TestInfo info, CadastroUsuarioDTO usuario/*, PasswordEncoder encoder*/) {
		final Usuario usuarioX = new Usuario();
		usuarioX.setNome(usuario.getNome());
		usuarioX.setEmail(usuario.getEmail());
		if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_SENHA_ERRADA)) {
			//usuarioX.setSenha(encoder.encode("123"));
			usuarioX.setSenha("123");
		} else {
			//usuarioX.setSenha(encoder.encode(usuario.getSenha()));
			usuarioX.setSenha(usuario.getSenha());
		}
		if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_INATIVO)) {
			usuarioX.setDataInativacao(LocalDate.now());
		}
		if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_SUSPENSO_ATE_HOJE)) {
			usuarioX.setDataFimSuspensao(LocalDate.now());
		} else if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_SUSPENSO_ATE_ONTEM)) {
			usuarioX.setDataFimSuspensao(LocalDate.now().minusDays(1L));
		}
		if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID)) {
			usuarioX.setIdUsuario("1111-xxxx");
		}
		usuarioX.setCargos(CargoFixture.criarLista(info));
		return usuarioX;
	}

	public static void configurarRepositorioUsuario(TestInfo info, UsuarioRepository repository, SenhaConfig senhaConfig, CadastroUsuarioDTO param) {
		Mockito.when(repository.existsByEmail(param.getEmail())).thenReturn(TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_EXISTE_POR_EMAIL));
		Mockito.when(repository.save(Mockito.any())).thenReturn(usuarioComId(param, senhaConfig));
	}

	public static void configurarRepositorioCredenciais(TestInfo info, UsuarioRepository usuarioRep, SenhaConfig senhaConfig,
			CredenciaisDTO param) {
		if (TagSaida.metodoDeTesteComecaCom(info, "reenviar")) {
			credenciaisReenviar(info, usuarioRep, senhaConfig);
		}
	}

	public static void configurarRepositorioUsuarioEToken(TestInfo info, SenhaConfig senhaConfig, TokenConfirmacaoRepository tokenRep, UsuarioRepository usuarioRep) {
		final boolean naoEncontrouPorEmail = TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_EMAIL);
		final boolean naoEncontrouPorID = TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_ID);
		final boolean semUsuario = naoEncontrouPorEmail || naoEncontrouPorID;
		if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_EMAIL)) {
			Mockito.when(usuarioRep.findByEmail(Mockito.anyString())).thenReturn(Optional.of(criarUsuarioCadastrado01Maria(info, senhaConfig)));
		} else if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_ID)) {
			Mockito.when(usuarioRep.findById(Mockito.anyString())).thenReturn(Optional.of(criarUsuarioCadastrado01Maria(info, senhaConfig)));
		} else if (semUsuario) {
			Mockito.when(usuarioRep.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		} else {
			if (!TagSaida.metodoDeTesteComecaCom(info, "listar")) {
				throw new IllegalArgumentException("Use ConstSaida.BD_USUARIO_ENCONTRADO_POR_EMAIL, BD_USUARIO_ENCONTRADO_POR_ID ou ConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_EMAIL, BD_USUARIO_NAO_ENCONTRADO_POR_ID neste teste unitário.");
			}
		}

		if (!semUsuario && tokenRep != null) {
			if (TagSaida.temTag(info, TesteConstSaida.BD_TOKEN_ENCONTRADO_POR_USUARIO)) {
				Mockito.when(tokenRep.findByUsuarioIdUsuario(Mockito.any())).thenReturn(TokenConfirmacaoFixture.criarRetornandoAlgo());
			} else if (TagSaida.temTag(info, TesteConstSaida.BD_TOKEN_NAO_ENCONTRADO_POR_USUARIO)) {
				Mockito.when(tokenRep.findByUsuarioIdUsuario(Mockito.any())).thenReturn(TokenConfirmacaoFixture.criarRetornandoVazio());
			} else if (TagSaida.temTag(info, TesteConstSaida.BD_TOKEN_ERRO_FATAL)) {
				Mockito.when(tokenRep.findByUsuarioIdUsuario(Mockito.any())).thenThrow(new IllegalArgumentException("Teste de erro lendo o token"));
			} else {
				throw new IllegalArgumentException("Use ConstSaida.BD_TOKEN_ENCONTRADO_POR_USUARIO ou ConstSaida.BD_TOKEN_NAO_ENCONTRADO_POR_USUARIO ou ConstSaida.BD_TOKEN_ERRO_FATAL neste teste unitário.");
			}
		}
	}

	public static void configurarRepositorioUsuarioListar(TestInfo info, TokenConfirmacaoRepository tokenRep,
			UsuarioRepository usuarioRep, SenhaConfig senhaConfig) {
		Usuario u01 = criarUsuarioCadastrado01Maria(info, senhaConfig);
		Usuario u02 = criarUsuarioCadastrado02Pedro(info, senhaConfig);
		Usuario u03 = criarUsuarioCadastrado03Antonio(info, senhaConfig);
		Sort ordenacao = Sort.by("nome");
		if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_LISTA_ERRO)) {
			Mockito.when(usuarioRep.findAll(Mockito.any(Pageable.class))).thenThrow(new IllegalArgumentException("Erro de teste ao listar"));
		} else if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_LISTA_VAZIA)) {
			Mockito.when(usuarioRep.findAll(Mockito.any(Pageable.class))).thenReturn(Page.empty());
		} else if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_LISTA_TUDO)) {
			Mockito.when(usuarioRep.findAll(PageRequest.of(0, 10, ordenacao))).thenReturn(criarLista(u01, u02, u03));
			Mockito.when(tokenRep.findByUsuarioIdUsuario(Mockito.anyString())).thenReturn(Optional.empty());
		} else if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_LISTA_PRIMEIRA_PAGINA)) {
			Mockito.when(usuarioRep.findAll(PageRequest.of(0, 2, ordenacao))).thenReturn(criarLista(u01, u02));
			Mockito.when(tokenRep.findByUsuarioIdUsuario(Mockito.anyString())).thenReturn(Optional.empty());
		} else if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_LISTA_SEGUNDA_PAGINA)) {
			Mockito.when(usuarioRep.findAll(PageRequest.of(1, 2, ordenacao))).thenReturn(criarLista(u03));
			Mockito.when(tokenRep.findByUsuarioIdUsuario(Mockito.anyString())).thenReturn(Optional.empty());
		}
	}

	private static Page<Usuario> criarLista(Usuario... usuarios) {
		return new PageImpl<>(List.of(usuarios));
	}
	static void credenciaisReenviar(TestInfo info, UsuarioRepository usuarioRep, SenhaConfig senhaConfig) {
		if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_ENCONTRADO_POR_EMAIL)) {
			Mockito.when(usuarioRep.findByEmail(Mockito.anyString())).thenReturn(Optional.of(criarUsuarioCadastrado01Maria(info, senhaConfig)));
		} else if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_EMAIL)) {
			Mockito.when(usuarioRep.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		} else {
			throw new IllegalArgumentException("Use ConstSaida.BD_USUARIO_ENCONTRADO_POR_EMAIL ou ConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_EMAIL neste teste unitário.");
		}
	}

	private static Usuario criarUsuarioCadastrado(TestInfo info, SenhaConfig senhaConfig, String idUsuario, String email, String nome) {
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(idUsuario);
		usuario.setEmail(email);
		usuario.setNome(nome);
		usuario.setSenha(senha(info, senhaConfig));
		usuario.setDataInativacao(dataInativacaoUsuario(info));
		usuario.setDataFimSuspensao(dataFimSuspensao(info));
		return usuario;
	}

	public static Usuario criarUsuarioCadastrado01Maria(TestInfo info, SenhaConfig senhaConfig) {
		return criarUsuarioCadastrado(info, senhaConfig, "1111-2222", "maria@bol.com.br", "Maria Joaquina");
	}

	public static Usuario criarUsuarioCadastrado02Pedro(TestInfo info, SenhaConfig senhaConfig) {
		return criarUsuarioCadastrado(info, senhaConfig, "1111-2223", "pedro@yahoo.com.br", "Pedro Pereira");
	}

	public static Usuario criarUsuarioCadastrado03Antonio(TestInfo info, SenhaConfig senhaConfig) {
		return criarUsuarioCadastrado(info, senhaConfig, "1111-2224", "antonio@gmail.com", "Antônio da Costa");
	}

	private static String senha(TestInfo info, SenhaConfig senhaConfig) {
		if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_SENHA_ERRADA)) {
			//return senhaConfig.encoder().encode("senha errada---");
			return "senha errada---";
		}
		//return senhaConfig.encoder().encode(CredenciaisFixture.definirSenha(info));
		return CredenciaisFixture.definirSenha(info);
	}

	private static LocalDate dataFimSuspensao(TestInfo info) {
		if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_SUSPENSO_ATE_HOJE)) {
			return LocalDate.now();
		} else if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_SUSPENSO_ATE_ONTEM)) {
			return LocalDate.now().minusDays(1);
		}
		return null;
	}

	private static LocalDate dataInativacaoUsuario(TestInfo info) {
		if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_INATIVO)) {
			return LocalDate.now();
		}
		return null;
	}

	public static void verificarRepositorio(TestInfo info, UsuarioRepository usuarioRep, TokenConfirmacaoRepository tokenRep) {
		if (TagSaida.metodoDeTesteComecaCom(info, "registrar")) {
			verificarRepositorioAoRegistrar(info, usuarioRep);
		} else if (TagSaida.metodoDeTesteComecaCom(info, "login")) {
			verificarRepositorioNoLogin(info, tokenRep, usuarioRep);
		} else if (TagSaida.metodoDeTesteComecaCom(info, "inativar", "reativar", "suspender")) {
			verificarRepositorioAoInativarOuReativarOuSuspender(info, usuarioRep);
		} else if (TagSaida.metodoDeTesteComecaCom(info, "listar")) {
			verificarRepositorioAoListar(info, usuarioRep, tokenRep);
		}
	}

	private static void verificarRepositorioAoListar(TestInfo info, UsuarioRepository usuarioRep,
			TokenConfirmacaoRepository tokenRep) {
		Sort ordenacao = Sort.by("nome");
		if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_LISTA_ERRO, TesteConstSaida.BD_USUARIO_LISTA_VAZIA)) {
			Mockito.verify(usuarioRep).findAll(Mockito.any(Pageable.class));
			Mockito.verify(tokenRep, Mockito.never()).findByUsuarioIdUsuario(Mockito.anyString());
		} else if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_LISTA_TUDO)) {
			Mockito.verify(usuarioRep).findAll(PageRequest.of(0, 10, ordenacao));
			Mockito.verify(tokenRep, Mockito.times(3)).findByUsuarioIdUsuario(Mockito.anyString());
		} else if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_LISTA_PRIMEIRA_PAGINA)) {
			Mockito.verify(usuarioRep).findAll(PageRequest.of(0, 2, ordenacao));
			Mockito.verify(tokenRep, Mockito.times(2)).findByUsuarioIdUsuario(Mockito.anyString());
		} else if (TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_LISTA_SEGUNDA_PAGINA)) {
			Mockito.verify(usuarioRep).findAll(PageRequest.of(1, 2, ordenacao));
			Mockito.verify(tokenRep, Mockito.times(1)).findByUsuarioIdUsuario(Mockito.anyString());
		}
	}

	private static void verificarRepositorioAoRegistrar(TestInfo info, UsuarioRepository usuarioRep) {
		boolean validacoes = TagSaida.temTag(info, TesteConstSaida.VALIDA_CONFIRMACAO_SENHA_ERRADA,
				TesteConstSaida.VALIDA_EMAIL_DOMINIO_CURTO, 
				TesteConstSaida.VALIDA_EMAIL_SEM_ARROBA, 
				TesteConstSaida.VALIDA_EMAIL_SOMENTE_DOMINIO, 
				TesteConstSaida.VALIDA_NOME_VAZIO, 
				TesteConstSaida.VALIDA_SENHA_CURTA);
		boolean emailJaExiste = !validacoes && TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_EXISTE_POR_EMAIL);
		if (validacoes) {
			Mockito.verify(usuarioRep, Mockito.never()).existsByEmail(Mockito.anyString());
			Mockito.verify(usuarioRep, Mockito.never()).save(Mockito.any());
		}
		else if (emailJaExiste) {
			Mockito.verify(usuarioRep).existsByEmail(Mockito.anyString());
			Mockito.verify(usuarioRep, Mockito.never()).save(Mockito.any());
		}
		else {
			Mockito.verify(usuarioRep).existsByEmail(Mockito.anyString());
			Mockito.verify(usuarioRep).save(Mockito.any());
		}
	}

	private static void verificarRepositorioNoLogin(TestInfo info, TokenConfirmacaoRepository tokenRep, UsuarioRepository usuarioRep) {
		Mockito.verify(usuarioRep).findByEmail(Mockito.anyString());
		if (TagSaida.temTag(info, TesteConstSaida.BD_TOKEN_ENCONTRADO_POR_USUARIO, TesteConstSaida.BD_TOKEN_NAO_ENCONTRADO_POR_USUARIO)) {
			Mockito.verify(usuarioRep).findByEmail(Mockito.anyString());
		}
		final boolean parou = TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_EMAIL);
		if (parou) {
			Mockito.verify(tokenRep, Mockito.never()).findByUsuarioIdUsuario(Mockito.anyString());
		} else {
			Mockito.verify(tokenRep).findByUsuarioIdUsuario(Mockito.anyString());
		}
	}

	private static void verificarRepositorioAoInativarOuReativarOuSuspender(TestInfo info, UsuarioRepository usuarioRep) {
		Mockito.verify(usuarioRep, Mockito.never()).findByEmail(Mockito.anyString());
		Mockito.verify(usuarioRep).findById(Mockito.anyString());
		final boolean parou = TagSaida.temTag(info, TesteConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_ID);
		if (parou) {
			Mockito.verify(usuarioRep, Mockito.never()).save(Mockito.any());
		} else {
			Mockito.verify(usuarioRep).save(Mockito.any());
		}
	}

}
