package com.kaishengit.service;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by hoyt on 2017/12/11.
 */
@Configuration
@DubboComponentScan(basePackages = "com.kaishengit.service")
public class Config {

    @Bean
    public ApplicationConfig applicationConfig () {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("ProductServiceConsumer");
        return applicationConfig;
    }

    @Bean
    public RegistryConfig registryConfig () {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("zookeeper://192.168.1.85:2181");
        return registryConfig;
    }
}
