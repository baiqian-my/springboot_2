package com.example.week02.service;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.week02.constant.GenderEnum;
import com.example.week02.dto.StudentAddDTO;
import com.example.week02.dto.StudentUpdateDTO;
import com.example.week02.vo.StudentVO;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
class StudentServiceTest {
    @Resource
    private StudentService studentService;

    @Test
    @Order(1)
    void getAllStudents() {
        List<StudentVO> allStudents = studentService.getAllStudents();
        allStudents.forEach(studentVO -> log.info("{}", studentVO));
    }

    @Test
    @Order(1)
    void addStudent() {
        studentService.addStudent(StudentAddDTO.builder()
                .name("example")
                .mobile("12345678901")
                .gender(GenderEnum.MALE)
                .avatar("https://example.top/avatar.jpg")
                .birthday(LocalDate.of(1999, 1, 1))
                .build());
        log.info("添加成功");
        List<StudentVO> allStudents = studentService.getAllStudents();
        allStudents.forEach(studentVO -> log.info("{}", studentVO));
    }

    @Test
    @Order(1)
    void getStudent() {
        StudentVO studentVO = studentService.getStudent(1001L);
        log.info("{}", studentVO);
    }

    @Test
    @Order(1)
    void getStudentByName() {
        List<StudentVO> studentVO = studentService.getStudentByName("张");
        studentVO.forEach(studentVO1 -> log.info("{}", studentVO1));
    }

    @Test
    @Order(1)
    void updateStudent() {
        studentService.updateStudent(1001L, StudentUpdateDTO.builder()
                .name("张三111")
                .mobile("12345678901")
                .avatar("https://example.top/new.jpg")
                .build());
        log.info("修改成功");
        StudentVO studentVO = studentService.getStudent(1001L);
        log.info("{}", studentVO);
    }

    @Test
    @Order(2)
    void deleteStudent() {
        studentService.deleteStudent(1001L);
        log.info("已成功删除");
        List<StudentVO> allStudents = studentService.getAllStudents();
        allStudents.forEach(studentVO -> log.info("{}", studentVO));
    }
}