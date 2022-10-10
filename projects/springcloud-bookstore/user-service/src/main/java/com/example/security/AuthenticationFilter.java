package com.example.security;

import java.io.IOException;
import java.security.Key;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private UserService userService;
	
	public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService) {
		super.setAuthenticationManager(authenticationManager);
		this.userService = userService;
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
		
		Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
		String token = Jwts.builder()
				.setSubject(String.valueOf(user.getId()))
				.setExpiration(new Date(System.currentTimeMillis() + 60*60*24*1000))
				.signWith(key)
				.compact();
		
		response.addHeader("token", token);
		response.addHeader("userId", String.valueOf(user.getId()));
	}
}
