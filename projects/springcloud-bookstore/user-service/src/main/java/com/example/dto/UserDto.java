package com.example.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {

	private int id;
	private String email;
	private String password;
	private String name;
	private String tel;
}
