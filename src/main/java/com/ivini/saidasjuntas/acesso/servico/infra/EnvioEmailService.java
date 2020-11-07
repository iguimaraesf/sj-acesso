package com.ivini.saidasjuntas.acesso.servico.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EnvioEmailService {
	private JavaMailSender sender;
	@Value("spring.mail.username")
	private String fromUser;
	
	@Autowired
	public EnvioEmailService(JavaMailSender sender) {
		this.sender = sender;
	}
	
	@Async
	public void sendMail(SimpleMailMessage msg) {
		this.sender.send(msg);
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
