package com.example.util;

import java.io.IOException;
import java.io.InputStream;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ModelMapperUtil {

	public static <T> T map(Object source, Class<? extends T> destination) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		return modelMapper.map(source, destination);
	}
	
	public static <T> T map(InputStream in, Class<? extends T> destination) throws IOException {
		return new ObjectMapper().readValue(in, destination);
	}
}
