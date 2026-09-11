package com.inditex.application.service;

import com.inditex.domain.service.PriceResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public PriceResolver priceResolver() {
        return new PriceResolver();
    }
}
