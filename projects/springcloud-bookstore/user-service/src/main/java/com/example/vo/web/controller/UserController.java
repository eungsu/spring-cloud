package com.example.vo.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.UserDto;
import com.example.service.UserService;
import com.example.util.ModelMapperUtil;
import com.example.vo.web.request.ResponseData;
import com.example.vo.web.request.UserRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user-service/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	
	@PostMapping
	public ResponseEntity<ResponseData> createUser(@RequestBody UserRequest userRequest) {
		UserDto userDto = ModelMapperUtil.map(userRequest, UserDto.class);
		userDto = userService.createUser(userDto);
		
		
		return ResponseEntity.ok(new ResponseData(userDto));
	}
	
}
