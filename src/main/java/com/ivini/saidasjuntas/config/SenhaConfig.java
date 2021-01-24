package com.ivini.saidasjuntas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SenhaConfig implements WebMvcConfigurer {

	/*@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(BCryptVersion.$2Y, 10);
	}*/

	// sem isso gera erro: No primary or default constructor found for interface org.springframework.security.core.userdetails.UserDetails
//	@Bean
//	public HandlerMethodArgumentResolver specificationArgumentResolver() {
//		return new AuthenticationPrincipalArgumentResolver();
//	}
	/*@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
	   argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
	}*/
}
