package com.example.smart_campus.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.smart_campus.pojo.Student;
import com.example.smart_campus.pojo.Teacher;
import com.example.smart_campus.service.StudentService;
import com.example.smart_campus.utils.Result;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/sms/studentController")
public class StudentController {
    @Resource
    StudentService studentService;
    @GetMapping("/getStudentByOpr/{pn}/{pageSize}")
    public Result<Object> getStudentByOpr(@PathVariable("pn")Integer pn, @PathVariable("pageSize")Integer pageSize, Student student){
        String name=student.getName();
        String clazzName=student.getClazzName();
        Page<Student> page=studentService.page(new Page<>(pn,pageSize),
                new LambdaQueryWrapper<Student>().like(StrUtil.isNotBlank(name),Student::getName,name).like(
                        StrUtil.isNotBlank(clazzName),Student::getClazzName,clazzName
                ).orderByDesc(Student::getId));
        return Result.ok(page);
    }


    @DeleteMapping("/deleteStudent")
    public Result<Object>deleteTeacher(@RequestBody List<Integer> ids){
        if(ids.size()==1){
            studentService.removeById(ids.get(0));
        }
        else{
            studentService.removeBatchByIds(ids);
        }
        return Result.ok();
    }
    @PostMapping("/saveOrUpdateStudent")
    public Result<Object>saveOrUpdateTeacher(@RequestBody Student student){
        Integer id=student.getId();
        if(id==null){
            studentService.save(student);
        }
        else{
            studentService.update(student,new LambdaQueryWrapper<Student>().eq(Student::getId,id));
        }
        return Result.ok();
    }

}
