package com.example.util;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class ModelMapperUtil {

	private static ModelMapper modelMapper;
	static {
		modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
	}
	
	public static <T> T map(Object source, Class<? extends T> destination) {
		return modelMapper.map(source, destination);
	}
}
