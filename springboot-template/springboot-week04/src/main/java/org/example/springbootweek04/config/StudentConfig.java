package org.example.springbootweek04.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.example.springbootweek04.entity.Student;

@Configuration
public class StudentConfig {
    @Bean
    public Student student() {
        Student student = new Student();
        student.setId(1L);
        student.setName("lly");
        return student;
    }
}