package com.eter.executor.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rusifer on 5/13/17.
 */
@Configuration
public class RESTConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder.build();
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        //converters.add(new MappingJackson2HttpMessageConverter());
        //restTemplate.setMessageConverters(converters);
        return restTemplate;
    }
}
