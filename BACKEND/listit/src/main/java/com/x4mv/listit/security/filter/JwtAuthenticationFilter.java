package com.x4mv.listit.security.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.x4mv.listit.dto.LoginDTO;
import static com.x4mv.listit.security.TokenJwtConfig.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{


	private AuthenticationManager authenticationManager;


	public JwtAuthenticationFilter(AuthenticationManager authenticationManager){
		this.authenticationManager = authenticationManager;
	}
	

	// cuando un usuario intenta autenticarse
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
	        throws AuthenticationException {
		//
		LoginDTO loginData = new LoginDTO();	
		// recibimos el json y lo convertimos en un objeto de java 
		try {
			loginData = new ObjectMapper().readValue(request.getInputStream(), LoginDTO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}


		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
			loginData.getCorreo(), 
			loginData.getContrasena()
		);

		return authenticationManager.authenticate(authenticationToken);
	}


	// cuando un usuario se loggea de forma correcta
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
	FilterChain filterChain, Authentication authResult) throws IOException, ServletException{


		// obtenermos el username -> en este caso seria el correo 
		User usuario = (User)authResult.getPrincipal();
		String correo = usuario.getUsername();


		// obtenemos los roles permitidos
		Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();

		Claims claims = Jwts.claims()
		.add("authorities",new ObjectMapper().writeValueAsString(roles))
		.build();

		// construimos el token

		String token = Jwts.builder()
				.subject(correo)
				.claims(claims)
				.expiration(new Date(System.currentTimeMillis() + 3600000))
				.issuedAt(new Date())	
				.signWith(SECRET_KEY)
				.compact();


		response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN+token);


		// mensaje de respuesta 
		Map<String,String> body = new HashMap<>();
		body.put("token", token);
		body.put("username: ", correo);
		body.put("message:", "Se ha logueado correctamente");

		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setContentType(CONTENT_TYPE);
		response.setStatus(200);


	}


	// en caso de fallar el login 
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
		throws IOException, ServletException{


		Map<String,String> body = new HashMap<>();
		body.put("message", "Error al intentar ingresar");
		body.put("Error", failed.getMessage());


		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setContentType(CONTENT_TYPE);
		response.setStatus(401);



	}

}
