package com.skrein.app1.config;

import com.skrein.app1.interceptor.LoginFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 2020/5/6 14:21
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean testFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new LoginFilter());
        registration.addUrlPatterns("/*");
        registration.setName("loginFilter");
        return registration;
    }
}
