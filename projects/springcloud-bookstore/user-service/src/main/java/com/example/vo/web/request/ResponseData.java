package com.example.vo.web.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseData {
	private boolean success;
	private String message;
	private Object item;
	private List<?> items;
	
	public ResponseData( ) {
		this.success = true;
		this.message = null;
	}
	
	public ResponseData(Object item) {
		this.success = true;
		this.message = null;
		this.item = item;
	}
	
	public ResponseData(List<?> items) {
		this.success = true;
		this.message = null;
		this.items= items;
	}

	public ResponseData(boolean success, String message) {
		this.success = success;
		this.message = message;
	}
}
