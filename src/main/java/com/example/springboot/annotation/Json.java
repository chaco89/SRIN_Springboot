package com.example.springboot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

/**
 * This annotation controls the JSON serialization and deserialization process.<br>
 * The {@code serialize} and {@code deserialize} attribute takes precedence over the {@code transient} modifier and the {@link Expose} annotation.
 *
 * @see {@link AppConfig#gson()}
 *      {@link GsonBuilder#addDeserializationExclusionStrategy(com.google.gson.ExclusionStrategy)}
 *      {@link GsonBuilder#addSerializationExclusionStrategy(com.google.gson.ExclusionStrategy)}
 */
@Target(ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Json {
    boolean serialize() default true;

    boolean deserialize() default true;
}
