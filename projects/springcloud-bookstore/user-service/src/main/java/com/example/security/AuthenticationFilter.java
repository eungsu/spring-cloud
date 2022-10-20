package com.example.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.service.UserService;
import com.example.util.ModelMapperUtil;
import com.example.vo.RequestLogin;
import com.example.vo.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private final UserService userService;
	private final Environment env;
	
	public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, Environment env) {
		super.setAuthenticationManager(authenticationManager);
		this.userService = userService;
		this.env = env;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			RequestLogin requestLogin = ModelMapperUtil.readValue(request.getInputStream(), RequestLogin.class);
			return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(requestLogin.getEmail(), requestLogin.getPassword()));
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String email = ((UserDetails) authResult.getPrincipal()).getUsername();
		User user = userService.getUserByEmail(email); 
		
		Claims claims = Jwts.claims();
		claims.put("id", user.getId());
		claims.put("role", user.getRole());
		
		long expirationTime = Long.parseLong(env.getProperty("security.jwt.token.expiration_time"));
		String secretString = env.getProperty("security.jwt.token.secret");
		SecretKey secretKey = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
		
		String token = Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expirationTime))
				.signWith(secretKey, SignatureAlgorithm.HS256)
				.compact();
		
		response.addHeader("token", token);
	}
}
