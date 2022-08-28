package com.nashtech.rookies.AssetManagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration 
{
    @Bean
    public WebMvcConfigurer corsConfigurer() 
    {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("https://happy-hill-07f55ef10.1.azurestaticapps.net/","http://localhost:3000/","https://asset-assignment-fe.azurewebsites.net");
            }
        };
    }
}
