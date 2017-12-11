package com.kaishengit.service;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created by hoyt on 2017/12/8.
 */

public class App {
    public static void main(String[] args) throws IOException {
        //ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-dubbo-provider.xml");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        context.start();
        System.out.println("ProductService Provider start....");
        System.in.read();
    }
}
