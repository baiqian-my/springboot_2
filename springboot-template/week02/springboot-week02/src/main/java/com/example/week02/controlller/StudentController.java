package com.example.week02.controlller;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import com.example.week02.vo.StudentVO;
import com.example.week02.service.StudentService;
import com.example.week02.dto.StudentUpdateDTO;
import com.example.week02.dto.StudentAddDTO;

import java.util.List;
@RestController
@RequestMapping("/api/vl/students")
public class StudentController {
    @Resource
    private StudentService studentService;

    @GetMapping("/all")
    public List<StudentVO> getAllStudents(){
        return studentService.getAllStudents();
    }


    @GetMapping("/{id}")
    public StudentVO getStudentById(@PathVariable Long id){
        return studentService.getStudent(id);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id){
        studentService.deleteStudent(id);
    }

    @GetMapping
    public List<StudentVO> getStudentByName(@RequestParam String name){
        return studentService.getStudentByName(name);
    }


    @PostMapping
    public String addStudent(@RequestBody StudentAddDTO studentAddDTO){
        studentService.addStudent(studentAddDTO);
        return "添加成功";
    }



    @PutMapping("/{id}")
//    public String updateStudent(@RequestBody Long id,@RequestBody StudentUpdateDTO studentUpdateDTO){
//        studentService.updateStudent(id, studentUpdateDTO);
//        return "修改成功";
//    }
    public void updateStudent(@PathVariable Long id, @RequestBody StudentUpdateDTO studentUpdateDTO) {
        studentService.updateStudent(id, studentUpdateDTO);
    }
}
