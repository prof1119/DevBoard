package com.devboard;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * DevBoard Application - التطبيق الرئيسي
 * تطبيق لوحة كانبان تعاونية احترافية
 */
@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com.devboard")
public class DevBoardApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(DevBoardApplication.class, args);
    }
}
