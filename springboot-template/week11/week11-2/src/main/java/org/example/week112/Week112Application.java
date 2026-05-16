package org.example.week112;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.example.mapper")
public class Week112Application {

    public static void main(String[] args) {
        SpringApplication.run(Week112Application.class, args);
    }

}
