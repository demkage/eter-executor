package com.eter.executor.config;

import com.eter.executor.apps.ALSApplication;
import com.eter.executor.apps.KMeanAgeApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by rusifer on 5/13/17.
 */
@Configuration
public class ApplicationsConfig {

    @Bean
    public ALSApplication alsApplication() {
        return new ALSApplication();
    }

    @Bean
    public KMeanAgeApplication kMeanAgeApplication() {
        return new KMeanAgeApplication();
    }
}
