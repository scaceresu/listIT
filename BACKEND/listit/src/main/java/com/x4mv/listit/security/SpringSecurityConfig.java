package com.x4mv.listit.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatchers;

import com.x4mv.listit.security.filter.JwtAuthenticationFilter;
import com.x4mv.listit.security.filter.JwtValidationFilter;

@Configuration
public class SpringSecurityConfig{


	@Autowired
	private AuthenticationConfiguration authenticationConfiguration;


	@Bean
	AuthenticationManager authenticationManager() throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		
		return new BCryptPasswordEncoder();
	}


	@Bean
	SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

		return httpSecurity.authorizeHttpRequests(auth -> auth 
			.requestMatchers("/auth/**").permitAll()
			.anyRequest().authenticated())
			.addFilter(new JwtAuthenticationFilter(authenticationManager()))
			.addFilter(new JwtValidationFilter(authenticationManager()))
			.csrf(config -> config.disable())
			.sessionManagement(management -> {
				management.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			})
			.build();
		
	}


}
