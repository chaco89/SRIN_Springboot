package com.example.springboot.util;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.springframework.lang.Nullable;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class LocalDateTypeAdapter extends TypeAdapter<LocalDate> {
    @Override
    public void write(JsonWriter out, @Nullable LocalDate localDate) throws IOException {
        if (localDate != null) {
            out.value(localDate.toString());
        } else {
            out.nullValue();
        }
    }

    @Nullable
    @Override
    public LocalDate read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        String date = in.nextString();
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
        	
        }
        
        return null;
    }
}
