package com.example.springboot.config;


import java.time.Duration;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

import com.example.serializer.CacheListSerializer;
import com.example.serializer.CacheSerializer;
import com.example.springboot.dto.EmployeeDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RedisConfig {

	final ObjectMapper mapper = new ObjectMapper();
	final int CACHE_LENGTH = 10;

	@Bean
	public RedisCacheConfiguration cacheConfiguration() {
		return RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofMinutes(60))
				.disableCachingNullValues()
				.serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
	}


	@Bean
	public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {

		CacheListSerializer<EmployeeDto> empListSerializer = new CacheListSerializer<>(EmployeeDto.class);
		CacheSerializer<EmployeeDto> empSerializer = new CacheSerializer<>(EmployeeDto.class);

		RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(CACHE_LENGTH));

		return (builder) -> builder.withCacheConfiguration("employeeCache", defaultConfig.serializeValuesWith(SerializationPair.fromSerializer(empListSerializer)))
								   .withCacheConfiguration("employeeCacheId", defaultConfig.serializeValuesWith(SerializationPair.fromSerializer(empSerializer)));
	}

}
