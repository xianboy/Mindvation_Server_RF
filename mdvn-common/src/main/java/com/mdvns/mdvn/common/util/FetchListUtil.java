package com.mdvns.mdvn.common.util;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class FetchListUtil {

    public static List fetch(RestTemplate restTemplate, String url, Object requestBody, ParameterizedTypeReference parameterizedTypeReference){
        ResponseEntity<List<Object>> responseEntity = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Object>(requestBody), parameterizedTypeReference);
        return responseEntity.getBody();
    }
}
