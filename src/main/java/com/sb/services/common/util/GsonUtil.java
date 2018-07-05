package com.sb.services.common.util;

import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GsonUtil {
	
	public static final Pattern pathPattern = Pattern.compile("\\.");

	/**
	 * Returns nested JSON element identified by the path
	 *
	 * The path is a set of tokens separated by a period. Each token can be either a key
	 * name, or an index in array.
	 *
	 * @param json json object or array to look into
	 * @param path identificator of a nested element
	 * @return json element (array or object) or a vaue. null if not found
	 */
	public static JsonElement getJsonValue(JsonElement json, String path) {
		if (null == json || null == path) {
			return null;
		}
		if (path.length() < 1) {
			return null;
		}
		String[] jsonPath = pathPattern.split(path, 2);
		String key = jsonPath[0];
		if (null == key || key.length() < 1) {
			return null;
		}
		if (json instanceof JsonObject) {
			JsonElement next = ((JsonObject)json).get(key);
			if (null == next) {
				return null;
			}
			if (jsonPath.length < 2) {
				return next;
			}
			return getJsonValue(next, jsonPath[1].trim());
		}
		if (json instanceof JsonArray) {
			int ind = -1;
			try {
				ind = Integer.parseInt(key);
			} catch (NumberFormatException e) {
				ind = -1;
			}
			if (ind < 0 || ind >= ((JsonArray)json).size()) {
				return null;
			}
			JsonElement next = null;
			try {
				next = ((JsonArray)json).get(ind);
			} catch (IndexOutOfBoundsException e) {
				return null;
			}
			if (jsonPath.length < 2) {
				return next;
			}
			return getJsonValue(next, jsonPath[1].trim());
		}
		if (jsonPath[0].length() > 1) {
			return null;
		}
		return json;
	}
}
