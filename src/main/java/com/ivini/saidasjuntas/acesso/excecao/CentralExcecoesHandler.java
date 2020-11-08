package com.ivini.saidasjuntas.acesso.excecao;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CentralExcecoesHandler {
	@ExceptionHandler({ UsuarioNaoEncontradoException.class, TokenNaoEncontradoException.class })
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public ResponseMsg handleNaoEncontrado(AbstractSaidasException exception) {
		return criarResposta(exception.getMessage());
	}

	@ExceptionHandler({ DocumentoJaExisteException.class, EmailJaExisteException.class })
	@ResponseStatus(code = HttpStatus.CONFLICT)
	public ResponseMsg handleJaExiste(AbstractSaidasException exception) {
		return criarResposta(exception.getMessage());
	}

	@ExceptionHandler({ UsuarioInativoException.class, UsuarioSuspensoException.class, UsuarioNaoConfirmadoException.class })
	@ResponseStatus(code = HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS)
	public ResponseMsg handleContrato(AbstractSaidasException exception) {
		return criarResposta(exception.getMessage());
	}

	private ResponseMsg criarResposta(String message) {
		return new ResponseMsg(message, LocalDateTime.now());
	}
}
