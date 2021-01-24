package com.ivini.saidasjuntas.config;

import org.springframework.beans.factory.annotation.Autowired;

import com.ivini.saidasjuntas.acesso.servico.infra.SaidasUserDetailsService;

//@EnableWebSecurity
public class SegurancaConfig /*extends WebSecurityConfigurerAdapter*/ {

	private static final String API_ADMIN_LISTAR = "/api/*/admin/listar";
	private static final String API_ADMIN_SUSPENDER = "/api/*/admin/suspender";
	private static final String API_ADMIN_REATIVAR = "/api/*/admin/reativar";
	private static final String API_ADMIN_INATIVAR = "/api/*/admin/inativar";
	private static final String API_ABERTO = "/api/*/aberto/**";
	@Autowired
	private SenhaConfig senhaConfig;
	@Autowired
	private SaidasUserDetailsService service;
	
	/*@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.authorizeRequests()
			//.anyRequest().permitAll()
			.antMatchers(API_ABERTO).permitAll()
			.antMatchers(API_ADMIN_INATIVAR, API_ADMIN_REATIVAR).hasRole("INAT-REAT")
			.antMatchers(API_ADMIN_SUSPENDER).hasRole("SUSPENDER")
			.antMatchers(API_ADMIN_LISTAR).hasRole("UM-USUARIO")
			.anyRequest().authenticated()
			.and()
			.httpBasic()
			.and().csrf().disable()
			;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(service)
			.passwordEncoder(senhaConfig.encoder());
	}*/

	/*@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.withUser("teste1").password(senhaConfig.encoder().encode("teste1")).roles("USER")
			.and()
			.withUser("admin1").password(senhaConfig.encoder().encode("admin1")).roles("USER", "ADMIN");
	}*/
	
}
