package com.ivini.saidasjuntas.fixture;

import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;

import com.ivini.saidasjuntas.acesso.dto.CadastroUsuarioDTO;
import com.ivini.saidasjuntas.acesso.dto.CredenciaisDTO;
import com.ivini.saidasjuntas.acesso.excecao.tipos.EnvioEmailException;
import com.ivini.saidasjuntas.acesso.servico.infra.EnvioEmailService;
import com.ivini.saidasjuntas.tag.TesteConstSaida;
import com.ivini.saidasjuntas.tag.TagSaida;

public final class EnvioEmailFixture {
	private EnvioEmailFixture() {
	}

	public static void configurarRepositorioCredenciais(TestInfo info, EnvioEmailService envio, CredenciaisDTO cred) throws EnvioEmailException {
		configurarRepositorioComum(info, envio, cred.getUsuario());
	}

	public static void configurarRepositorioUsuario(TestInfo info, EnvioEmailService envio, CadastroUsuarioDTO param) throws EnvioEmailException {
		configurarRepositorioComum(info, envio, param.getEmail());
	}

	private static void configurarRepositorioComum(TestInfo info, EnvioEmailService envio, String email)
			throws EnvioEmailException {
		if (TagSaida.temTag(info, TesteConstSaida.INFRA_ERRO_ENVIAR_EMAIL)) {
			Mockito
				.doThrow(new EnvioEmailException("meu@gmail.com", email, null))
				.when(envio).sendMail(Mockito.any());
		} else {
			Mockito
				.doNothing()
				.when(envio).sendMail(Mockito.any());
		}
	}

	public static void verificarRepositorio(TestInfo info, EnvioEmailService envio) throws EnvioEmailException {
		if (TagSaida.metodoDeTesteComecaCom(info, "registrar")) {
			verificarRepositorioAoRegistrar(info, envio);
		}
		if (TagSaida.metodoDeTesteComecaCom(info, "reenviar")) {
			verificarRepositorioAoReenviar(info, envio);
		}
	}

	static void verificarRepositorioAoReenviar(TestInfo info, EnvioEmailService envio) throws EnvioEmailException {
		boolean validacoes = TagSaida.temTag(info, TesteConstSaida.VALIDA_CONFIRMACAO_SENHA_ERRADA,
				TesteConstSaida.VALIDA_EMAIL_DOMINIO_CURTO, 
				TesteConstSaida.VALIDA_EMAIL_SEM_ARROBA, 
				TesteConstSaida.VALIDA_EMAIL_SOMENTE_DOMINIO, 
				TesteConstSaida.VALIDA_NOME_VAZIO, 
				TesteConstSaida.VALIDA_SENHA_CURTA,
				TesteConstSaida.BD_TOKEN_ERRO_FATAL,
				TesteConstSaida.BD_USUARIO_NAO_ENCONTRADO_POR_EMAIL);
		if (validacoes) {
			Mockito.verify(envio, Mockito.never()).sendMail(Mockito.any());
		} else {
			Mockito.verify(envio).sendMail(Mockito.any());
		}
	}

	static void verificarRepositorioAoRegistrar(TestInfo info, EnvioEmailService envio) throws EnvioEmailException {
		boolean validacoes = TagSaida.temTag(info, TesteConstSaida.VALIDA_CONFIRMACAO_SENHA_ERRADA,
				TesteConstSaida.VALIDA_EMAIL_DOMINIO_CURTO, 
				TesteConstSaida.VALIDA_EMAIL_SEM_ARROBA, 
				TesteConstSaida.VALIDA_EMAIL_SOMENTE_DOMINIO, 
				TesteConstSaida.VALIDA_NOME_VAZIO, 
				TesteConstSaida.VALIDA_SENHA_CURTA,
				TesteConstSaida.BD_USUARIO_EXISTE_POR_EMAIL);
		if (validacoes) {
			Mockito.verify(envio, Mockito.never()).sendMail(Mockito.any());
		} else {
			Mockito.verify(envio).sendMail(Mockito.any());
		}
	}

}
