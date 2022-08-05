package com.example.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.dto.UserDto;

@Service
public class UserServiceImpl implements UserService {

	@Override
	public UserDto createUser(UserDto userDto) {
		userDto.setUserId(UUID.randomUUID().toString());
		return null;
	}
}
