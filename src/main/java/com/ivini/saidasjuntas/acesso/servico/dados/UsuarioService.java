package com.ivini.saidasjuntas.acesso.servico.dados;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ivini.saidasjuntas.acesso.dto.AssociacaoDTO;
import com.ivini.saidasjuntas.acesso.dto.CadastroUsuarioDTO;
import com.ivini.saidasjuntas.acesso.dto.DetalhesUsuarioDTO;
import com.ivini.saidasjuntas.acesso.excecao.tipos.AbstractSaidasException;
import com.ivini.saidasjuntas.acesso.excecao.tipos.EmailJaExisteException;
import com.ivini.saidasjuntas.acesso.excecao.tipos.OutroResponsavelException;
import com.ivini.saidasjuntas.acesso.excecao.tipos.TokenNaoEncontradoException;
import com.ivini.saidasjuntas.acesso.excecao.tipos.UsuarioInativoException;
import com.ivini.saidasjuntas.acesso.excecao.tipos.UsuarioNaoConfirmadoException;
import com.ivini.saidasjuntas.acesso.excecao.tipos.UsuarioNaoEncontradoException;
import com.ivini.saidasjuntas.acesso.excecao.tipos.UsuarioSuspensoException;
import com.ivini.saidasjuntas.acesso.modelo.Cargo;
import com.ivini.saidasjuntas.acesso.modelo.Funcionalidade;
import com.ivini.saidasjuntas.acesso.modelo.TokenConfirmacao;
import com.ivini.saidasjuntas.acesso.modelo.Usuario;
import com.ivini.saidasjuntas.acesso.repositorio.CargoRepository;
import com.ivini.saidasjuntas.acesso.repositorio.FuncionalidadeRepository;
import com.ivini.saidasjuntas.acesso.repositorio.TokenConfirmacaoRepository;
import com.ivini.saidasjuntas.acesso.repositorio.UsuarioRepository;
import com.ivini.saidasjuntas.acesso.servico.infra.EnvioEmailService;
import com.ivini.saidasjuntas.acesso.util.DataSistemaHelper;
import com.ivini.saidasjuntas.acesso.util.UsuarioHelper;

@Service
public class UsuarioService {
	private final UsuarioRepository usuarioRep;
	private final EnvioEmailService envio;
	private final TokenConfirmacaoRepository tokenRep;
	//private final SenhaConfig senhaConfig;
	private final CargoRepository cargoRep;
	private final FuncionalidadeRepository funcionalidadeRep;

	@Autowired
	public UsuarioService(UsuarioRepository repository, TokenConfirmacaoRepository tokenRep, EnvioEmailService envio
			/*, SenhaConfig senhaConfig*/
			, CargoRepository cargoRep
			, FuncionalidadeRepository funcionalidadeRep) {
		this.usuarioRep = repository;
		this.tokenRep = tokenRep;
		this.envio = envio;
		//this.senhaConfig = senhaConfig;
		this.cargoRep = cargoRep;
		this.funcionalidadeRep = funcionalidadeRep;
	}
	
	@Transactional(rollbackFor = { AbstractSaidasException.class })
	public Usuario registrarNovoUsuario(CadastroUsuarioDTO param) throws AbstractSaidasException {
		if (usuarioRep.existsByEmail(param.getEmail())) {
			throw new EmailJaExisteException(param.getEmail());
		}
		Usuario novo = new Usuario();
		novo.setNome(param.getNome());
		novo.setEmail(param.getEmail());
		novo.setSenha(param.getSenha());//novo.setSenha(senhaConfig.encoder().encode(param.getSenha()));
		//novo.setCargos(cargoService.cargosUsuarioPadrao());
		Usuario usuario = usuarioRep.save(novo);

		enviarTokenPrivate(usuario);

		return usuario;
	}

	@Transactional(rollbackFor = { AbstractSaidasException.class })
	public TokenConfirmacao reenviarToken(String email) throws AbstractSaidasException {
		Usuario usuario = buscarPorEmail(email);
		return enviarTokenPrivate(usuario);
	}

	public TokenConfirmacao confirmarUsuario(String codigo) throws AbstractSaidasException {
		TokenConfirmacao token = tokenRep.findByTokenGerado(codigo).orElseThrow(() -> new TokenNaoEncontradoException(codigo));
		tokenRep.delete(token);
		return token;
	}

	public Usuario loginUsuario(String email, String senha) throws AbstractSaidasException {
		Usuario usuario = buscarPorEmail(email);

		if (tokenRep.findByUsuarioIdUsuario(usuario.getIdUsuario()).isPresent()) {
			throw new UsuarioNaoConfirmadoException(email);
		}
		if (usuario.getDataInativacao() != null) {
			throw new UsuarioInativoException(email);
		}
		if (usuario.getDataFimSuspensao() != null && ChronoUnit.DAYS.between(LocalDate.now(), usuario.getDataFimSuspensao()) >= 0) {
			throw new UsuarioSuspensoException(email, usuario.getDataFimSuspensao());
		}
		conferirSenha(email, senha, usuario);
		return usuario;
	}

	public String inativar(String usuarioId) throws AbstractSaidasException {
		Usuario usuario = buscarPorId(usuarioId);
		usuario.setDataInativacao(LocalDate.now());
		usuarioRep.save(usuario);
		return DataSistemaHelper.formatarData(usuario.getDataInativacao());
	}

	public String reativar(String usuarioId) throws AbstractSaidasException {
		Usuario usuario = buscarPorId(usuarioId);
		usuario.setDataInativacao(null);
		usuarioRep.save(usuario);
		return DataSistemaHelper.formatarData(usuario.getDataInativacao());
	}
	
	public String suspender(String usuarioId) throws AbstractSaidasException {
		Usuario usuario = buscarPorId(usuarioId);
		usuario.setDataFimSuspensao(LocalDate.now().plusDays(UsuarioHelper.DIAS_SUSPENSAO));
		usuarioRep.save(usuario);
		return DataSistemaHelper.formatarData(usuario.getDataFimSuspensao());
	}

	private Usuario buscarPorId(String usuarioId) throws UsuarioNaoEncontradoException {
		return usuarioRep.findById(usuarioId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
	}

	private Usuario buscarPorEmail(String email) throws UsuarioNaoEncontradoException {
		return usuarioRep.findByEmail(email)
				.orElseThrow(() -> new UsuarioNaoEncontradoException(email));
	}

	private TokenConfirmacao enviarTokenPrivate(Usuario usuario) throws AbstractSaidasException {
		TokenConfirmacao confirmacao = tokenRep.findByUsuarioIdUsuario(usuario.getIdUsuario()).orElse(null);
		if (confirmacao != null) {
			tokenRep.delete(confirmacao);
		}

		final TokenConfirmacao novoToken = Optional.ofNullable(tokenRep.save(new TokenConfirmacao(null, usuario, UUID.randomUUID().toString()))).orElse(new TokenConfirmacao("", usuario, ""));
		String tok = novoToken.getTokenGerado();
		SimpleMailMessage msg = envio.criarMensagem(usuario.getEmail(), "Confirme sua conta", 
				String.format("Clique <a href='http://localhost:8080/api/v1/confirmar?token=%s'>aqui</a> para confirmar a sua conta.", tok));
		envio.sendMail(msg);
		return confirmacao;
	}

	private void conferirSenha(String email, String senha, Usuario usuario) throws UsuarioNaoEncontradoException {
		//if (!senhaConfig.encoder().matches(senha, usuario.getSenha())) {
		if (!senha.equals(usuario.getSenha())) {
			throw new UsuarioNaoEncontradoException(email);
		}
	}

	public Page<DetalhesUsuarioDTO> listar(int pagAtual, int pagQtd) {
		Pageable paginacao = PageRequest.of(pagAtual - 1, pagQtd, Sort.by("nome"));
		Page<Usuario> usuarios = usuarioRep.findAll(paginacao);
		// TODO Ver como transformar essa função em lambda
		return usuarios.map(new Function<Usuario, DetalhesUsuarioDTO>() {
			@Override
			public DetalhesUsuarioDTO apply(Usuario usuario) {
				return UsuarioHelper.toDTO(usuario, tokenRep.findByUsuarioIdUsuario(usuario.getIdUsuario()));
			}
		});
	}
	
	public List<String> temQualDessesAcessos(String usuarioId, List<String> acessos) throws AbstractSaidasException {
		List<String> confirmados = new ArrayList<>();
		if (acessos == null) {
			return confirmados;
		}
		Usuario usuario = buscarPorId(usuarioId);
		if (usuario.getCargos() == null) {
			return confirmados;
		}
		usuario.getCargos().forEach(cargo -> {
			if (cargo.getPrivilegios() != null) {
				cargo.getPrivilegios().forEach(func -> {
					if (acessos.contains(func.getNome())) {
						confirmados.add(func.getNome());
					}
				});
			}
		});
		return confirmados;
	}

	// tornar-comerciante
	// meus-clientes
	// meu-extrato
	public String associarColaborador(AssociacaoDTO param) throws AbstractSaidasException {
		String idGerente = param.getIdReferencia();
		Usuario usuario = buscarPorId(param.getIdUsuario());
		Usuario referencia = usuario.getGerente();
		if (referencia != null && !idGerente.equals(referencia.getIdUsuario())) {
			throw new OutroResponsavelException(idGerente);
		}
		if (referencia == null) {
			usuario.setGerente(buscarPorId(idGerente));
		}
		Set<Cargo> lista = Optional.ofNullable(usuario.getCargos()).orElse(new HashSet<>());
		lista.add(lerCargoComFuncionalidades(DominioCargo.BD_CARGO_TORNAR_COMERCIANTE, 
				DominioFuncionalidade.KEY_TORNAR_COMERCIANTE_ATIVAR, 
				DominioFuncionalidade.KEY_TORNAR_COMERCIANTE_BUSCAR_POR_EMAIL, 
				DominioFuncionalidade.KEY_TORNAR_COMERCIANTE_TROCAR,
				DominioFuncionalidade.KEY_TORNAR_COMERCIANTE_INATIVAR));
		lista.add(lerCargoComFuncionalidades(DominioCargo.BD_CARGO_MEUS_CLIENTES, 
				DominioFuncionalidade.KEY_MEUS_CLIENTES_ULTIMA_COMPRA,
				DominioFuncionalidade.KEY_MEUS_CLIENTES_CONTATOS));
		usuarioRep.save(usuario);
		// TODO GRAVAR NO EXTRATO
		return usuario.getIdUsuario();
	}

	public String desassociarColaborador(AssociacaoDTO param) throws AbstractSaidasException {
		String idGerente = param.getIdReferencia();
		Usuario usuario = buscarPorId(param.getIdUsuario());
		Usuario referencia = usuario.getGerente();
		if (referencia != null && !idGerente.equals(referencia.getIdUsuario())) {
			throw new OutroResponsavelException(idGerente);
		}
		usuario.setGerente(null);
		Set<Cargo> lista = Optional.ofNullable(usuario.getCargos()).orElse(new HashSet<>());
		lista.remove(lerCargo(DominioCargo.BD_CARGO_TORNAR_COMERCIANTE));
		lista.remove(lerCargo(DominioCargo.BD_CARGO_MEUS_CLIENTES));
		usuarioRep.save(usuario);
		// TODO GRAVAR NO EXTRATO
		return usuario.getIdUsuario();
	}

	// validar-cupom
	public String associarFuncionario(AssociacaoDTO param) throws AbstractSaidasException {
		String idEmpregador = param.getIdReferencia();
		Usuario usuario = buscarPorId(param.getIdUsuario());
		Usuario referencia = usuario.getEmpregador();
		if (referencia != null && !idEmpregador.equals(referencia.getIdUsuario())) {
			throw new OutroResponsavelException(idEmpregador);
		}
		usuario.setEmpregador(buscarPorId(idEmpregador));
		
		Set<Cargo> lista = Optional.ofNullable(usuario.getCargos()).orElse(new HashSet<>());
		lista.add(lerCargoComFuncionalidades(DominioCargo.BD_CARGO_VALIDAR_CUPOM, 
				DominioFuncionalidade.KEY_VALIDAR_CUPOM_ESCANEAR,
				DominioFuncionalidade.KEY_VALIDAR_CUPOM_DIGITAR));

		usuarioRep.save(usuario);
		// TODO GRAVAR NO EXTRATO
		return usuario.getIdUsuario();
	}

	public String desassociarFuncionario(AssociacaoDTO param) throws AbstractSaidasException {
		String idEmpregador = param.getIdReferencia();
		Usuario usuario = buscarPorId(param.getIdUsuario());
		Usuario referencia = usuario.getEmpregador();
		if (referencia != null && !idEmpregador.equals(referencia.getIdUsuario())) {
			throw new OutroResponsavelException(idEmpregador);
		}
		usuario.setEmpregador(null);
		Set<Cargo> lista = Optional.ofNullable(usuario.getCargos()).orElse(new HashSet<>());
		lista.remove(lerCargo(DominioCargo.BD_CARGO_VALIDAR_CUPOM));
		usuarioRep.save(usuario);
		// TODO GRAVAR NO EXTRATO
		return usuario.getIdUsuario();
	}

	// marcar-suspeito
	public String associarSeguranca(AssociacaoDTO param) throws AbstractSaidasException {
		String idContratante = param.getIdReferencia();
		Usuario usuario = buscarPorId(param.getIdUsuario());
		Usuario referencia = usuario.getCentral();
		if (referencia != null && !idContratante.equals(referencia.getIdUsuario())) {
			throw new OutroResponsavelException(idContratante);
		}
		usuario.setCentral(buscarPorId(idContratante));
		
		Set<Cargo> lista = Optional.ofNullable(usuario.getCargos()).orElse(new HashSet<>());
		lista.add(lerCargoComFuncionalidades(DominioCargo.BD_FUNC_MARCAR_COMO_SUSPEITO, 
				DominioFuncionalidade.KEY_GRAVAR_SUSPEITA));

		usuarioRep.save(usuario);
		// TODO GRAVAR NO EXTRATO
		return usuario.getIdUsuario();
	}

	public String desassociarSeguranca(AssociacaoDTO param) throws AbstractSaidasException {
		String idContratante = param.getIdReferencia();
		Usuario usuario = buscarPorId(param.getIdUsuario());
		Usuario referencia = usuario.getCentral();
		if (referencia != null && !idContratante.equals(referencia.getIdUsuario())) {
			throw new OutroResponsavelException(idContratante);
		}
		usuario.setCentral(null);
		Set<Cargo> lista = Optional.ofNullable(usuario.getCargos()).orElse(new HashSet<>());
		lista.remove(lerCargo(DominioCargo.BD_FUNC_MARCAR_COMO_SUSPEITO));
		usuarioRep.save(usuario);
		// TODO GRAVAR NO EXTRATO
		return usuario.getIdUsuario();
	}

	private Cargo lerCargo(DominioCargo dominioCargo) throws AbstractSaidasException {
		String nomeCargo = dominioCargo.toString();
		return cargoRep.findByNome(nomeCargo).orElseThrow(() -> new TokenNaoEncontradoException(nomeCargo));
	}

	private Cargo lerCargoComFuncionalidades(DominioCargo dominioCargo, DominioFuncionalidade... dominioFuncionalidades) {
		String nomeCargo = dominioCargo.toString();
		Cargo cargo = cargoRep.findByNome(nomeCargo).orElse(new Cargo(null, nomeCargo, new HashSet<>()));
		completarFuncionalidades(cargo.getPrivilegios(), dominioFuncionalidades);
		return cargo;
	}

	private void completarFuncionalidades(Set<Funcionalidade> privilegios, DominioFuncionalidade... dominioFuncionalidades) {
		for (DominioFuncionalidade dominioFuncionalidade : dominioFuncionalidades) {
			String nome = dominioFuncionalidade.toString();
			privilegios.add(funcionalidadeRep.findByNome(nome).orElse(new Funcionalidade(null, nome)));
		}
	}

}
