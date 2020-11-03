package com.ivantex.saidasjuntas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan(basePackages = {
		"com.ivantex.saidasjuntas.acesso.repositorio",
		"com.ivantex.saidasjuntas.acesso.modelos"})
@SpringBootApplication
public class SjAcessoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SjAcessoApplication.class, args);
	}

}
