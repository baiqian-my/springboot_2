package org.example.springbootweek04.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class Student {
    private Long id;
    private String name;

    public void study() {
        log.info("{} 正在学习...", name);
    }

    public static void main(String[] args) {
        Student student = new Student();
        student.study();
    }
}