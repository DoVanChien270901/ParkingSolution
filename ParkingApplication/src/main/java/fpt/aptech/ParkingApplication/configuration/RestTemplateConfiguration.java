/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApplication.configuration;

import java.util.Collections;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author CHIEN
 */
@Service
public class RestTemplateConfiguration {

    public HttpEntity setRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return new HttpEntity(headers);
    }

    public HttpEntity setRequest(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        if (token != null) {
            headers.set("Authorization", "Bearer " + token);
        }
        return new HttpEntity(headers);
    }

    public HttpEntity setRequest(Object ob) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return new HttpEntity(ob, headers);
    }

    public HttpEntity setRequest(String token, Object ob) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        if (token != null) {
            headers.set("Authorization", "Bearer " + token);
        }
        return new HttpEntity(ob, headers);
    }

    public ResponseEntity<?> excuteRequest(String uri, HttpMethod method, HttpEntity request, Class t) {
        try {
            RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(uri, method, request, t);
        } catch (RestClientResponseException e) {
            return ResponseEntity
                    .status(e.getRawStatusCode())
                    .body(e.getResponseBodyAsString());
        }
    }

}
