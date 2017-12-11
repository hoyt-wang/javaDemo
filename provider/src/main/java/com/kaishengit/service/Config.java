package com.kaishengit.service;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by hoyt on 2017/12/11.
 */

@Configuration
@DubboComponentScan(basePackages = "com.kaishengit.service.impl")
public class Config {

    @Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("ProductService");
        return applicationConfig;
    }

    @Bean
    public ProtocolConfig protocolConfig () {
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setHost("192.168.1.85");
        protocolConfig.setPort(20880);
        protocolConfig.setName("dubbo");
        return protocolConfig;
    }

    @Bean
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("zookeeper://127.0.0.1:2181");
        return registryConfig;
    }
}
