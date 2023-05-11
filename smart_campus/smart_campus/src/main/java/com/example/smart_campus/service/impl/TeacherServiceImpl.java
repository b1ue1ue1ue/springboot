package com.example.smart_campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.smart_campus.pojo.Admin;
import com.example.smart_campus.pojo.Student;
import com.example.smart_campus.pojo.Teacher;
import com.example.smart_campus.service.TeacherService;
import com.example.smart_campus.mapper.TeacherMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author blueluelue
* @description 针对表【tb_teacher】的数据库操作Service实现
* @createDate 2023-05-09 11:46:32
*/
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher>
    implements TeacherService{

    @Resource
    private TeacherMapper teacherMapper;
    @Override
    public Teacher selectTeacherByUsernameAndPwd(String username, String password){
        return teacherMapper.selectOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getName,username).eq(Teacher::getPassword,password));
    }

    @Override
    public Teacher selectTeacherById(Long userId) {
        return teacherMapper.selectOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getId,userId));
    }


}




