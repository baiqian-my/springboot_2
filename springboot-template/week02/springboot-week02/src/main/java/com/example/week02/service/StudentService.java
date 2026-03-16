package com.example.week02.service;

import org.springframework.stereotype.Service;
import com.example.week02.constant.GenderEnum;
import com.example.week02.dto.StudentAddDTO;
import com.example.week02.dto.StudentUpdateDTO;
import com.example.week02.entity.Student;
import com.example.week02.vo.StudentVO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class StudentService {
    private static final Map<Long, Student> STUDENT_DATA = new ConcurrentHashMap<>();


    static {
        Student student1 = Student.builder()
                .id(1001L)
                .name("张三")
                .mobile("13888888888")
                .gender(GenderEnum.MALE)
                .avatar("https://example.top/1.png")
                .enabled(true)
                .birthday(LocalDate.of(1990, 1, 1))
                .createTime(LocalDateTime.now())
                .build();
        Student student2 = Student.builder()
                .id(1002L)
                .name("张三丰")
                .mobile("13888888889")
                .gender(GenderEnum.FEMALE)
                .avatar("https://example.top/2.png")
                .enabled(true)
                .birthday(LocalDate.of(1990, 1, 1))
                .createTime(LocalDateTime.now())
                .build();
        STUDENT_DATA.put(student1.getId(), student1);
        STUDENT_DATA.put(student2.getId(), student2);
    }


    public List<StudentVO> getAllStudents() {
        List<StudentVO> list = new ArrayList<>();
        STUDENT_DATA.values().forEach(student -> {
            list.add(StudentVO.builder()
                    .id(student.getId())
                    .name(student.getName())
                    .mobile(student.getMobile())
                    .gender(student.getGender())
                    .createTime(student.getCreateTime())
                    .build());
        });
        return list;
    }


    public void addStudent(StudentAddDTO studentAddDTO) {
        Student student = Student.builder()
                // id自增，这里使用系统时间
                .id(System.currentTimeMillis())
                .name(studentAddDTO.getName())
                .mobile(studentAddDTO.getMobile())
                .gender(studentAddDTO.getGender())
                .avatar(studentAddDTO.getAvatar())
                .enabled(true)
                .birthday(studentAddDTO.getBirthday())
                .createTime(LocalDateTime.now())
                .build();
        STUDENT_DATA.put(student.getId(), student);
    }


    public StudentVO getStudent(Long id) {
        Student student = STUDENT_DATA.get(id);
        return StudentVO.builder()
                .id(student.getId())
                .name(student.getName())
                .mobile(student.getMobile())
                .gender(student.getGender())
                .createTime(student.getCreateTime())
                .build();
    }


    public List<StudentVO> getStudentByName(String name) {
        List<StudentVO> list = new ArrayList<>();
        STUDENT_DATA.values().forEach(student -> {
            if (student.getName().contains(name)) {
                list.add(StudentVO.builder()
                        .id(student.getId())
                        .name(student.getName())
                        .mobile(student.getMobile())
                        .gender(student.getGender())
                        .createTime(student.getCreateTime())
                        .build());
            }
        });
        return list;
    }


    public void updateStudent(Long id, StudentUpdateDTO studentUpdateDTO) {
        Student student = STUDENT_DATA.get(id);
        student.setName(studentUpdateDTO.getName());
        student.setMobile(studentUpdateDTO.getMobile());
        student.setAvatar(studentUpdateDTO.getAvatar());
    }


    public void deleteStudent(Long id) {
        STUDENT_DATA.remove(id);
    }
}