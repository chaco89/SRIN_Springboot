package com.example.springboot.config;

import java.time.LocalDate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import com.example.springboot.annotation.Json;
import com.example.springboot.dto.ResourceDto;
import com.example.springboot.dto.ResourceDto.ResourceDtoSerializer;
import com.example.springboot.dto.ResourcesDto;
import com.example.springboot.dto.ResourcesDto.ResourcesDtoSerializer;
import com.example.springboot.util.LocalDateTypeAdapter;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Configuration
public class AppConfig {
  
	
    @Bean
    public Gson gson() {
    	 GsonBuilder gsonBuilder = new GsonBuilder();
    	 gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter());
    	 gsonBuilder.registerTypeAdapter(ResourceDto.class, new ResourceDtoSerializer());
         gsonBuilder.registerTypeAdapter(ResourcesDto.class, new ResourcesDtoSerializer());
    	
         gsonBuilder.addSerializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                Json json = clazz.getAnnotation(Json.class);
                return json != null && !json.serialize();
            }
         });

         gsonBuilder.addDeserializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                Json json = clazz.getAnnotation(Json.class);
                return json != null && !json.deserialize();
            }
         });

         gsonBuilder.excludeFieldsWithoutExposeAnnotation();

         return gsonBuilder.create();
    }
    
    
    @Bean
    public GsonHttpMessageConverter gsonHttpMessageConverter(Gson gson) {
    	GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
        converter.setGson(gson);

        return converter;
    }
    
}
