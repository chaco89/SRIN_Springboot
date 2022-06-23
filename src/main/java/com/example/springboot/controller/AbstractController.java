package com.example.springboot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.springboot.dto.ResourceDto;
import com.example.springboot.dto.ResourcesDto;
import com.example.springboot.util.HttpUtils;


public abstract class AbstractController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected ResponseEntity<Void> sendCreateResponse(String path) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Location", HttpUtils.toUrl(path));
        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }
    
    protected ResponseEntity<ResourcesDto> sendReadListResponse(ResourcesDto response) {
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    protected ResponseEntity<ResourceDto> sendReadResponse(ResourceDto response) {
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    protected ResponseEntity<Void> sendUpdateResponse() {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    protected ResponseEntity<Void> sendDeleteResponse() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
