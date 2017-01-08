package com.carlosmecha.diary.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Main web configuration
 *
 * Created by Carlos on 12/20/16.
 */
@Configuration
public class MvcConfiguration extends WebMvcConfigurerAdapter {


    /**
     * Defines the views for the different routes.
     * @param registry View registry.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/notebooks").setViewName("notebooks");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/error").setViewName("error");
    }
}
