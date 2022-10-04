package com.example.web.controller;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/second-service")
public class SecondServiceController {

	// 로드밸런스 동작 체크를 위해서 추가된 내용
	// application.properties의 환경설정 정보를 제공하는 객체를 주입받는다.
	private final Environment env;
		
	
	@GetMapping("/welcome")
	public String welcome() {
		//return "Welcome to the Second service.";
		return String.format("Hi, there. This is a message from second service on PORT %s", env.getProperty("local.server.port"));
	}
}
