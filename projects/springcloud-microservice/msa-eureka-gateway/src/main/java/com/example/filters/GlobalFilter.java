package com.example.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

	public GlobalFilter() {
		super(Config.class);
	}
	
	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			ServerHttpResponse response = exchange.getResponse();
			
			log.info("Global Filter baseMessage: {}", config.getBaseMessage());
			
			if (config.isPreLogger()) {
				log.info("Global Filter start: 요청 아이디 -> {}", request.getId());
			}
			
			// Mono는 Spring WebFlex에서 지원하는 객체다.
			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				if (config.isPostLogger()) {
					log.info("Global Filter end: 응답 코드 -> {}", response.getStatusCode());
				}
			}));
		});
	}
	
	// Config 클래스는 application.yml에서 설정된 값을 전달받는 객체다.
	@Getter @Setter
	public static class Config {
		private String baseMessage;
		private boolean preLogger;
		private boolean postLogger;
	}

}
