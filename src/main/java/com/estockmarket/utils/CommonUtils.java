package com.estockmarket.utils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

import com.estockmarket.domain.Company;
import com.estockmarket.exception.BusinessException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonUtils {

	private static final String FILE_NAME = "taskmate_config.json";

	public static Map<String, String> getHeaders() {
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("x-custom-header", "application/json");	
		headers.put("Access-Control-Allow-Origin", "*");
		headers.put("Access-Control-Allow-Credentials", "true");
		return headers;
	}
}
