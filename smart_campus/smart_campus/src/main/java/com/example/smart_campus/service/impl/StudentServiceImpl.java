package com.example.smart_campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.smart_campus.pojo.Admin;
import com.example.smart_campus.pojo.Student;
import com.example.smart_campus.service.StudentService;
import com.example.smart_campus.mapper.StudentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author blueluelue
* @description 针对表【tb_student】的数据库操作Service实现
* @createDate 2023-05-09 11:46:30
*/
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student>
    implements StudentService{

    @Resource
    private StudentMapper studentMapper;
    @Override
    public Student selectStuentByUsernameAndPwd(String username, String password){
        return studentMapper.selectOne(new LambdaQueryWrapper<Student>().eq(Student::getName,username).eq(Student::getPassword,password));
    }

    @Override
    public Student selectStudentById(Long userId) {
        return studentMapper.selectOne(new LambdaQueryWrapper<Student>().eq(Student::getId,userId));
    }


}




