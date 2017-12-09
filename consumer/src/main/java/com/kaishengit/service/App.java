package com.kaishengit.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.List;

/**
 * Created by hoyt on 2017/12/8.
 */

public class App {

    public static void main(String[] args) throws IOException {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-dubbo-consumer.xml");
        ProductService productService = (ProductService) context.getBean("rpcProductService");
        List<String> names = productService.findAllNames();
        for (String name : names) {
            System.out.println(name);
        }

        System.in.read();
    }
}
