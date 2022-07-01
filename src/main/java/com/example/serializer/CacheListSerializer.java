package com.example.serializer;

import java.util.List;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;


public class CacheListSerializer<T> implements RedisSerializer<Object> {

	final ObjectMapper mapper = new ObjectMapper();
	final Class<T> typeParameterClass;

	public CacheListSerializer(Class<T> typeParameterClass) {
		mapper.registerModule(new ParameterNamesModule()).registerModule(new Jdk8Module())
		.registerModule(new JavaTimeModule());

		this.typeParameterClass = typeParameterClass;
	}


	@Override
	@SuppressWarnings("unchecked")
	public byte[] serialize(Object obj) throws SerializationException {

		if (obj == null) {
			return null;
		}

		List<T> dto = (List<T>) obj;

		try {
			return mapper.writeValueAsBytes(dto);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Object deserialize(byte[] bytes) throws SerializationException {

		if (bytes == null) {
			return null;
		}

		try {
			JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, typeParameterClass);
			return mapper.readValue(bytes, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	public static <T> TypeReference<List<T>> list() {
	    return new TypeReference<List<T>>(){};
	}*/

}
