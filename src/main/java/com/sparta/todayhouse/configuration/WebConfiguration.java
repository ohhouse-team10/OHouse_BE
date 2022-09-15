package com.sparta.todayhouse.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "https://oh-ouse-fe.vercel.app")
                .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE", "HEAD")
                .allowedHeaders("*")
                .exposedHeaders("*")
                //pre-flight 리퀘스트를 캐싱
                .maxAge(3000);
    }
}
