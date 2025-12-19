package com.onlinevoting.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.onlinevoting.interceptor.Handler;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final Handler handler;

    @Autowired
    public WebMvcConfig(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Register the handler interceptor for all paths (adjust patterns if needed)
        registry.addInterceptor(handler).addPathPatterns("/**");
    }
}
