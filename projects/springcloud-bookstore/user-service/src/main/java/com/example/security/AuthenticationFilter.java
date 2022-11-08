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
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.util.StringUtils;

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
			// HttpServletRequest객체의 body부 내용을 읽어서 RequestLogin객체에 저장하고 반환한다.
			RequestLogin requestLogin = ModelMapperUtil.map(request.getInputStream(), RequestLogin.class);
			// RequestLogin객체에 저장된 이메일과 비밀번호로 UsernamePasswordAuthenticationToken객체를 생성한다.
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(requestLogin.getEmail(), requestLogin.getPassword());
			// 인증관리자의 authenticate(Authentication authentication) 메소드에 이메일과 비밀번호가 포함된 UsernamePasswordAuthenticationToken객체를 전달하고 인증처리를 요청한다.
			// 인증이 완료되면 인증된 사용자정보과 권한정보가 포함된 Authentication객체를 반환한다. 
			return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
		} catch (IOException ex) {
			throw new PreAuthenticatedCredentialsNotFoundException("인증에 필요한 정보를 찾을 수 없습니다.", ex);
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		// Authentication객체에서 인증주체(Principal)객체를 조회하고, 사용자 이메일을 조회한다.
		String email = ((UserDetails) authResult.getPrincipal()).getUsername();
		// 사용자 이메일로 사용자정보를 조회한다.
		User user = userService.getUserByEmail(email); 
		
		// Claims는 JWT의 비공개 정보를 저장할 때 사용되는 객체는 정보는 name/value의 쌍으로 저장한다.
		Claims claims = Jwts.claims();
		claims.put("id", user.getId());
		claims.put("role", user.getRole());
		
		// 환경설정 정보에서 토큰 만료시간과 시크릿문자열을 조회한다.
		long expirationTime = Long.parseLong(env.getProperty("security.jwt.token.expiration_time"));
		String secretString = StringUtils.trimAllWhitespace(env.getProperty("security.jwt.token.secret"));
		// 서명에 필요한 SecretKey 객체를 생성한다.
		SecretKey secretKey = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
		
		// JWT 토큰을 생성한다.
		String token = Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expirationTime))
				.signWith(secretKey, SignatureAlgorithm.HS256)
				.compact();
		
		// 응답헤더에 JWT 토큰을 추가한다.
		response.addHeader("token", token);
	}
}
