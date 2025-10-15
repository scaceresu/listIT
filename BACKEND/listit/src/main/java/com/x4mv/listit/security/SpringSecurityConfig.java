package com.x4mv.listit.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatchers;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.x4mv.listit.security.filter.JwtAuthenticationFilter;
import com.x4mv.listit.security.filter.JwtValidationFilter;

import io.jsonwebtoken.lang.Arrays;

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

		// TODO: privatizar las rutas dependiendo del rol
		return httpSecurity.authorizeHttpRequests(auth -> auth 
			.requestMatchers("/auth/**").permitAll()
			.anyRequest().authenticated())
			.addFilter(new JwtAuthenticationFilter(authenticationManager()))
			.addFilter(new JwtValidationFilter(authenticationManager()))
			.csrf(config -> config.disable())
			.cors(config -> config.configurationSource(corsConfigurationSource()))
			.sessionManagement(management -> {
				management.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			})
			.build();
		
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource(){

		CorsConfiguration config = new CorsConfiguration(); 
		config.setAllowedOriginPatterns(java.util.Arrays.asList("*"));
		config.setAllowedMethods(java.util.Arrays.asList("GET", "POST", "PUT", "DELETE"));
		config.setAllowedHeaders(java.util.Arrays.asList("Authorization", "Content-type")); 
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;


	}

	@Bean
	FilterRegistrationBean<CorsFilter> corsFilter(){
		FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<>(
			new CorsFilter(corsConfigurationSource()));
	
		corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);;
		return corsBean;
	}


}
