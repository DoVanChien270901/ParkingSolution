package fpt.aptech.parkinggo.configuration;

import android.os.AsyncTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.*;

import java.util.Collections;

public class RestTemplateConfiguration extends RestTemplate{
    public static HttpEntity setRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return new HttpEntity(headers);
    }

    public static HttpEntity setRequest(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        if (token != null) {
            headers.set("Authorization", "Bearer " + token);
        }
        return new HttpEntity(headers);
    }

    public static HttpEntity setRequest(Object ob) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return new HttpEntity(ob, headers);
    }

    public static HttpEntity setRequest(String token, Object ob) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        if (token != null) {
            headers.set("Authorization", "Bearer " + token);
        }
        return new HttpEntity(ob, headers);
    }

    public static ResponseEntity<?> excuteRequest(String uri, HttpMethod method, HttpEntity request, Class t) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
            return restTemplate.exchange(uri, method, request, t);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity( e.getMessage() ,e.getStatusCode());
        }
    }

}
