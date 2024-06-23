package com.github.valentina810.weekplannerformarusia.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<CurlLoggingFilter> loggingFilter() {
        FilterRegistrationBean<CurlLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CurlLoggingFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}