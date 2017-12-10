package com.kaishengit.crm;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created by hoyt on 2017/12/10.
 */

public class App {
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:applicationContext*.xml");
        context.start();
        System.in.read();
    }
}
