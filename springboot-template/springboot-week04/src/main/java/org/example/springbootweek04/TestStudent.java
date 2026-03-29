package org.example.springbootweek04;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.example.springbootweek04.config.StudentConfig;
import org.example.springbootweek04.entity.Student;


public class TestStudent {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(StudentConfig.class);
        Student sss = context.getBean("student", Student.class);
        sss.study();
    }
}