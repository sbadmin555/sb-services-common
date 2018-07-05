package com.sb.services.common.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

	private static ObjectMapper mapper = new ObjectMapper();

	public static <T> T stringToObject(String json, Class<T> clazz) throws IOException {
		JsonNode node = mapper.readTree(json);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper.readValue(node.traverse(), clazz);
	}

	public static <T> T convertValue(Object data, Class<T> clazz) {
		return mapper.convertValue(data, clazz);
	}

	public static String writeValueAsString(Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}

}
