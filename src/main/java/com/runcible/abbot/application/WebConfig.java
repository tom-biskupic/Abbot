package com.runcible.abbot.application;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer 
{
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) 
    {
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .resourceChain(false);

         registry.addResourceHandler("/css/**") // URL pattern to access CSS files
                .addResourceLocations("classpath:/static/css/"); // Location of your CSS files
    }
    
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
            .mediaType("css", MediaType.parseMediaType("text/css")) // Custom mapping for .yaml
            .defaultContentType(MediaType.APPLICATION_OCTET_STREAM); // Fallback default
        configurer
            .mediaType("js", MediaType.parseMediaType("text/javascript")) // Custom mapping for .yaml
            .defaultContentType(MediaType.APPLICATION_OCTET_STREAM); // Fallback default
            
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:8080") // Replace with your Angular URL
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("Content-Type", "X-XSRF-TOKEN")
            .allowCredentials(true); // Must be true to allow cookies
    }
}
