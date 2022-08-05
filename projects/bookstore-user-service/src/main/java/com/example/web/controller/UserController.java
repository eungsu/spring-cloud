package com.example.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.request.UserRequest;

@RestController
@RequestMapping("/users")
public class UserController {

	@GetMapping("/health_check")
	public String status() {
		return "It's working in User Service";
	}
	
	@PostMapping
	public String createUser(@RequestBody UserRequest userRequest) {
		return "Create user method is called";
	}
}
