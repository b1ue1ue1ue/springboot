package com.example.smart_campus.controller;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebAppConfigurer implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/upload/**").addResourceLocations
                ("file:"+System.getProperty("user.dir")+"/upload/");

        //System.out.println("file:"+System.getProperty("user.dir")+"/upload/");
    }
}
