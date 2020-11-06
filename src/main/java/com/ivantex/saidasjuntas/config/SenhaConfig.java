package com.ivantex.saidasjuntas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SenhaConfig implements WebMvcConfigurer {

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(BCryptVersion.$2Y, 10);
	}

}
