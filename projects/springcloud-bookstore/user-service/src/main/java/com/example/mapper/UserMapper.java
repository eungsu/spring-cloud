package com.example.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.vo.User;

@Mapper
public interface UserMapper {
	void insertUser(User user);
	User getUserByEmail(String email);
}
