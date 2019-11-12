package com.learn.java.util;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestService {

    private final RestTemplate restTemplate;

    public RestService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    // Create
    public Object postRequest(String url, Object body, Object... uriVariables) {
        return this.restTemplate.postForEntity(url, body, Object.class, uriVariables);
    }

    // Read
    public Object getRequest(String url, Object... uriVariables) {
        return this.restTemplate.getForEntity(url, Object.class, uriVariables);
    }

    // Update
    public void putRequest(String url, Object body, Object... uriVariables) {
        this.restTemplate.put(url, body, uriVariables);
    }

    // Delete
    public void deleteRequest(String url, Object body, Object... uriVariables) {
        this.restTemplate.delete(url, body, uriVariables);
    }

    public RestTemplate getRestTemplate () {
        return this.restTemplate;
    }

}