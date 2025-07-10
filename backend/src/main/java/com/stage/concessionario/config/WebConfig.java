package com.stage.concessionario.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:5173", "http://localhost:5174")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/", "classpath:/frontend/build/static/");
        
        registry.addResourceHandler("/*.js", "/*.json", "/*.ico")
                .addResourceLocations("classpath:/frontend/build/");
        
        registry.addResourceHandler("/")
                .addResourceLocations("classpath:/frontend/build/index.html");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Redirige tutte le richieste non API all'app React
        registry.addViewController("/login").setViewName("forward:/index.html");
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.addViewController("/utenti").setViewName("forward:/index.html");
        registry.addViewController("/veicoli").setViewName("forward:/index.html");
        registry.addViewController("/movimenti").setViewName("forward:/index.html");
        registry.addViewController("/configurazioni").setViewName("forward:/index.html");
    }
}
