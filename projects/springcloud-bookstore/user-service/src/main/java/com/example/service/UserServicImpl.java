package com.example.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dto.UserDto;
import com.example.mapper.UserMapper;
import com.example.util.ModelMapperUtil;
import com.example.vo.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServicImpl implements UserService {

	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public UserDto createUser(UserDto userDto) {
		User user = ModelMapperUtil.map(userDto, User.class);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userMapper.insertUser(user);
		userDto.setId(user.getId());

		return userDto;
	}
	
	@Override
	public User getUserByEmail(String email) {
		return userMapper.getUserByEmail(email);
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userMapper.getUserByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException(email);
		}
		
		return user.getUserDetails();
	}
}
