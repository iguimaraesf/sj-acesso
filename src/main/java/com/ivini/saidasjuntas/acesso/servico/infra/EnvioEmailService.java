package com.ivini.saidasjuntas.acesso.servico.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ivini.saidasjuntas.acesso.excecao.tipos.EnvioEmailException;

@Service
public class EnvioEmailService {
	private JavaMailSender sender;
	@Value("${spring.mail.username}")
	private String fromUser;
	
	@Autowired
	public EnvioEmailService(JavaMailSender sender) {
		this.sender = sender;
	}
	
	@Async
	public void sendMail(SimpleMailMessage msg) throws EnvioEmailException {
		try {
			this.sender.send(msg);
		} catch (MailException e) {
			e.printStackTrace();
			throw new EnvioEmailException(msg.getFrom(), obterEmail(msg.getTo()), e);
		}
	}

	private String obterEmail(String[] listaRecipientes) {
		if (listaRecipientes == null) {
			return "";
		}
		if (listaRecipientes.length == 0) {
			return "";
		}
		return listaRecipientes[0];
	}

	public SimpleMailMessage criarMensagem(String email, String titulo, String conteudo) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setSubject(titulo);
		msg.setText(conteudo);
		msg.setTo(email);
		msg.setFrom(this.fromUser);
		return msg;
	}
}
