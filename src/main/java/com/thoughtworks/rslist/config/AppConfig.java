package com.thoughtworks.rslist.config;

import com.thoughtworks.rslist.service.RsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public RsService rsService() {
        return new RsService();
    }
}
