package com.eter.executor.config;

import com.eter.executor.apps.ALSApplication;
import com.eter.executor.apps.KMeanAgeApplication;
import com.eter.executor.apps.KMeanGenderApplication;
import com.eter.executor.apps.SalesAnalysis;
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

    @Bean
    public SalesAnalysis salesAnalysis() {
        return new SalesAnalysis();
    }

    @Bean
    public KMeanGenderApplication kMeanGenderApplication() {
        return new KMeanGenderApplication();
    }
}
