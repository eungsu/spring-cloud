package com.example.filters;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.google.common.net.HttpHeaders;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
	
	@Autowired
	private Environment env;

	public AuthorizationHeaderFilter() {
		super(Config.class);
	}
	
	
	public static class Config {}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				return onError(exchange, "missing authorization header", HttpStatus.BAD_REQUEST);
			}
			
			String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
			String token = authorizationHeader.replace("Bearer", "");
			
			try {
				String secretString = env.getProperty("security.jwt.token.secret");
				SecretKey secretKey = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
				
				Claims claims = Jwts.parserBuilder()
					.setSigningKey(secretKey)
					.build()
					.parseClaimsJws(token)
					.getBody();
				
				int userId = (Integer) claims.get("id");
				String role = (String) claims.get("role");
				
				log.info("JWT Claims USER-ID -> {}", userId);
				log.info("JWT Claims USER-ROLE -> {}", role);
				
				request.mutate()
					.header("X-Authorization-Id", String.valueOf(userId))
					.header("X-Authorization-Role", role)
					.build();
				
				return chain.filter(exchange);
				
			} catch (Exception e) {
				e.printStackTrace();
				return onError(exchange, "missing authorization header", HttpStatus.UNAUTHORIZED);
			}
			
		};
	}
	
	private Mono<Void> onError(ServerWebExchange exchange, String error, HttpStatus httpStatus) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		
		log.error(error);
		return response.setComplete();
	}
	
}
