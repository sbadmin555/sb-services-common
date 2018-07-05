package com.sb.services.common.generator;

import org.modelmapper.ModelMapper;

public class ModelMapperGenerator {
	static ModelMapper modelMapper = null;

	public static ModelMapper getMapper() {
		if (modelMapper == null) {
			modelMapper = new ModelMapper();
			modelMapper.getConfiguration().setAmbiguityIgnored(true);
		}
		return modelMapper;
	}

	public static void initialise() {
		modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		modelMapper.getConfiguration().setFieldMatchingEnabled(true);
	}
}
