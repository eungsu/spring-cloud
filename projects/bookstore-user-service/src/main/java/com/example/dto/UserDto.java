package com.example.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class UserDto {

	private String email;
	private String password;
	private String name;
	private String userId;
	private Date createdDate;
	private String encryptedPassword;
}
