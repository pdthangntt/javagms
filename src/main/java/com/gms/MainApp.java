package com.gms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@EnableAutoConfiguration
@EnableCaching
@PropertySources({
    @PropertySource("classpath:application.properties")
    , @PropertySource("classpath:application-${spring.profiles.active}.properties")
})

public class MainApp extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MainApp.class); //.resourceLoader(new JarResourceLoader());
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }
}
