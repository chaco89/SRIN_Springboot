package com.example.springboot.dto;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.example.springboot.annotation.Json;
import com.example.springboot.constant.JsonKey;

@Json(deserialize = false)
public class ResourcesDto  {
	private final List<? extends ResourceDto> list;

    public ResourcesDto(List<? extends ResourceDto> list) {
        this.list = list;
    }

	public static class ResourcesDtoSerializer implements JsonSerializer<ResourcesDto> {
    	@Override
        public JsonElement serialize(ResourcesDto dto, Type typeOfSrc, JsonSerializationContext context) {
    		JsonObject json = new JsonObject();
            json.add(JsonKey.EMPLOYEES, context.serialize(dto.list));
           
            return json;
        }
    }

}
