package com.ivini.saidasjuntas.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ivini.saidasjuntas.acesso.dto.ListaMensagensRetornoDTO;
import com.ivini.saidasjuntas.acesso.dto.MensagemErroDTO;
import com.ivini.saidasjuntas.acesso.excecao.tipos.AbstractSaidasException;
import com.ivini.saidasjuntas.acesso.excecao.tipos.DocumentoJaExisteException;
import com.ivini.saidasjuntas.acesso.excecao.tipos.EmailJaExisteException;
import com.ivini.saidasjuntas.acesso.excecao.tipos.EnvioEmailException;
import com.ivini.saidasjuntas.acesso.excecao.tipos.OutroResponsavelException;
import com.ivini.saidasjuntas.acesso.excecao.tipos.TokenNaoEncontradoException;
import com.ivini.saidasjuntas.acesso.excecao.tipos.UsuarioInativoException;
import com.ivini.saidasjuntas.acesso.excecao.tipos.UsuarioNaoConfirmadoException;
import com.ivini.saidasjuntas.acesso.excecao.tipos.UsuarioNaoEncontradoException;
import com.ivini.saidasjuntas.acesso.excecao.tipos.UsuarioSuspensoException;
import com.ivini.saidasjuntas.acesso.util.MensagemErroHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class CentralExcecoesHandler {
	private final MessageSource messageSource;
	
	@Autowired
	public CentralExcecoesHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@ExceptionHandler({ UsuarioNaoEncontradoException.class, TokenNaoEncontradoException.class })
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public ListaMensagensRetornoDTO handleNaoEncontrado(AbstractSaidasException exception, Locale locale) {
		return MensagemErroHelper.criarResposta(MensagemErroHelper.criarMensagemErro(messageSource, "naoEncontrado", exception, locale));
	}

	@ExceptionHandler({ DocumentoJaExisteException.class, EmailJaExisteException.class })
	@ResponseStatus(code = HttpStatus.CONFLICT)
	public ListaMensagensRetornoDTO handleJaExiste(AbstractSaidasException exception, Locale locale) {
		return MensagemErroHelper.criarResposta(MensagemErroHelper.criarMensagemErro(messageSource, "jaExiste", exception, locale));
	}

	@ExceptionHandler({ UsuarioInativoException.class, UsuarioSuspensoException.class, UsuarioNaoConfirmadoException.class })
	@ResponseStatus(code = HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS)
	public ListaMensagensRetornoDTO handleContrato(AbstractSaidasException exception, Locale locale) {
		logExcecao(exception);
		return MensagemErroHelper.criarResposta(MensagemErroHelper.criarMensagemErro(messageSource, "contrato", exception, locale));
	}

	@ExceptionHandler({ EnvioEmailException.class })
	@ResponseStatus(code = HttpStatus.FAILED_DEPENDENCY)
	public ListaMensagensRetornoDTO handleEnvioEmail(AbstractSaidasException exception, Locale locale) {
		logExcecao(exception);
		return MensagemErroHelper.criarResposta(MensagemErroHelper.criarMensagemErro(messageSource, "falha", exception, locale));
	}

	@ExceptionHandler({ OutroResponsavelException.class })
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	// Exemplos: associar um usuário como funcionário, mas ele já é funcionário de outro.
	public ListaMensagensRetornoDTO handleOutroResponsavel(AbstractSaidasException exception, Locale locale) {
		return MensagemErroHelper.criarResposta(MensagemErroHelper.criarMensagemErro(messageSource, "acesso", exception, locale));
	}

	@ExceptionHandler({ MethodArgumentNotValidException.class })
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ListaMensagensRetornoDTO handleValidacaoCampos(MethodArgumentNotValidException exception) {
		List<MensagemErroDTO> mensagens = new ArrayList<>();
		exception.getBindingResult().getAllErrors().forEach(
			erroX -> mensagens.add(MensagemErroHelper.criarMensagemErro(erroX))
		);
		// logExcecao(exception);
		return MensagemErroHelper.criarResposta(mensagens);
	}

	@ExceptionHandler({ Exception.class })
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public ListaMensagensRetornoDTO handleOutras(Exception exception) {
		List<MensagemErroDTO> mensagens = new ArrayList<>();
		if (log.isErrorEnabled()) {
			log.error(exception.getClass().getName(), exception);
		}
		mensagens.add(new MensagemErroDTO("erro", exception.getMessage()));
		logExcecao(exception);
		return MensagemErroHelper.criarResposta(mensagens);
	}

	private void logExcecao(Exception exception) {
		// TODO no disco, por favor!
		exception.printStackTrace();
	}

}
