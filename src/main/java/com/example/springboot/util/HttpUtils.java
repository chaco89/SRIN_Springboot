package com.example.springboot.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public final class HttpUtils {
    private HttpUtils() {}

    public static String getRemoteAddress(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        return xfHeader != null ? xfHeader.split(",")[0] : request.getRemoteAddr();
    }

    public static String toUrl(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(path).toUriString();
    }
}
