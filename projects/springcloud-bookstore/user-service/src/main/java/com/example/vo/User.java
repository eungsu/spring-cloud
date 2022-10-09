package com.example.vo;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Alias("User")
public class User {

	private int id;
	private String email;
	private String password;
	private String name;
	private String tel;
	private int point;
	private String disabled;
	private Date createdDate;
	private Date updatedDate;
	private String role;
}
