package com.example.vo.web.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRequest {

	private String email;
	private String password;
	private String name;
	private String tel;
	
}
