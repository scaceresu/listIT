package com.x4mv.listit.security.filter;

import static com.x4mv.listit.security.TokenJwtConfig.CONTENT_TYPE;
import static com.x4mv.listit.security.TokenJwtConfig.HEADER_AUTHORIZATION;
import static com.x4mv.listit.security.TokenJwtConfig.PREFIX_TOKEN;
import static com.x4mv.listit.security.TokenJwtConfig.SECRET_KEY;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.x4mv.listit.security.SimpleGrantedAuthorityJsonCreator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.lang.Arrays;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtValidationFilter extends BasicAuthenticationFilter{

	public JwtValidationFilter(AuthenticationManager authenticationManager){
		super(authenticationManager);

	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
	        throws IOException, ServletException {
	    // TODO Auto-generated method stub
		
		// obtenemos el token con el bearer 
		String header = request.getHeader(HEADER_AUTHORIZATION);

		if (header == null || !header.startsWith(PREFIX_TOKEN)){
			chain.doFilter(request, response);
			return;
		}

		// removemos el bearer 
		
		String token = header.replace(PREFIX_TOKEN, "");


		try {
			
			Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();

			// obtener los datos del claims
			String username = (String) claims.get("username");
			Object authoritiesClaims = claims.get("authorities");


			Collection<? extends GrantedAuthority> authorities = Arrays.asList(
				new ObjectMapper()
				.addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
				.readValue(authoritiesClaims
				.toString().getBytes(), 
				SimpleGrantedAuthority[].class));

			UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(username, null, authorities);	
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);

			chain.doFilter(request, response);

		} catch ( JwtException e) {

			Map<String, String> body = new HashMap<>();

			body.put("Error", e.getMessage());
			body.put("message", "El token es invalido");

			response.getWriter().write(new ObjectMapper().writeValueAsString(body));
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType(CONTENT_TYPE);

		}


	}

}
