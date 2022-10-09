package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.UserDto;
import com.example.service.UserService;
import com.example.util.ModelMapperUtil;
import com.example.vo.RequestUser;
import com.example.vo.ResponseUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user-service/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	
	@PostMapping
	public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser requestUser) {
		UserDto userDto = ModelMapperUtil.map(requestUser, UserDto.class);
		userDto = userService.createUser(userDto);
		ResponseUser responseUser = ModelMapperUtil.map(userDto, ResponseUser.class);
		
		return ResponseEntity.ok(responseUser);
	}
	
}
