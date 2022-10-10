package com.example.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.dto.UserDto;
import com.example.vo.User;

public interface UserService extends UserDetailsService {

	UserDto createUser(UserDto userDto);
	User getUserByEmail(String email);
}
