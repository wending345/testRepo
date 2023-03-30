package com.tongji.enso.mybatisdemo.controller.online;


import com.tongji.enso.mybatisdemo.entity.online.Student;
import com.tongji.enso.mybatisdemo.service.online.StudentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 查询全部学生信息
     */
    @GetMapping("/findAll")
    @ApiOperation(value = "查询全部学生", notes = "查询全部学生")
    public List<Student> findAll(){
        return studentService.findAllStudent();
    }

    /**
     * 根据ID查询学生
     * @param: id;
     * @return: Student.
     */
    @GetMapping("/findById/{id}")
    @ApiOperation(value = "根据ID查询学生", notes = "根据ID查询学生")
    public Student findById(@PathVariable int id){
        return studentService.findById(id);
    }
}
