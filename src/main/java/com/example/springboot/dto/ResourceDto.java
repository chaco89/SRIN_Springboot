package com.example.springboot.dto;

import java.lang.reflect.Type;

import com.example.springboot.annotation.Json;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Json(deserialize = false)
public class ResourceDto {
    private final Object dto;

    public ResourceDto(Object dto) {
        this.dto = dto;
    }

    @SuppressWarnings("unchecked")
    public <T> T unwrap() {
        return (T) dto;
    }

    public static class ResourceDtoSerializer implements JsonSerializer<ResourceDto> {
        @Override
        public JsonElement serialize(ResourceDto dto, Type typeOfSrc, JsonSerializationContext context) {
            if (dto.dto == null) {
                return JsonNull.INSTANCE;
            }

            return context.serialize(dto.dto).getAsJsonObject();
        }
    }

    public ResourceDto getResourceDto(Object dto) {
    	return this;
    }

}
