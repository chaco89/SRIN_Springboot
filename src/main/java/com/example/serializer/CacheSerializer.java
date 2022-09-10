package com.example.serializer;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;


public class CacheSerializer<T> implements RedisSerializer<Object> {

	final ObjectMapper mapper = new ObjectMapper();
	final Class<T> typeParameterClass;

	public CacheSerializer(Class<T> typeParameterClass) {
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

		T dto = (T) obj;

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
			return mapper.readValue(bytes, typeParameterClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
