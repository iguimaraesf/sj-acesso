package com.ivini.saidasjuntas.fixture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonFixture {
	private JsonFixture() {
	}
	
	public static String toJson(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		return mapper.writeValueAsString(obj);
	}
}
