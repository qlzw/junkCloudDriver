package org.cloud.driver.config;

import org.cloud.driver.resolver.LoginUserHandlerMethodArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @Classname WebConfig
 * @Description TODO
 * @Date 2020/6/9 11:34
 * @Created by 87454
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 添加resolver
     * @param resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserHandlerMethodArgumentResolver());
    }
    @Bean
    public LoginUserHandlerMethodArgumentResolver loginUserHandlerMethodArgumentResolver(){
        return new LoginUserHandlerMethodArgumentResolver();
    }
}
