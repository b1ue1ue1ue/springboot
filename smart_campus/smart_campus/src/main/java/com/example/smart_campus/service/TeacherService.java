package com.example.smart_campus.service;

import com.example.smart_campus.pojo.Student;
import com.example.smart_campus.pojo.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author blueluelue
* @description 针对表【tb_teacher】的数据库操作Service
* @createDate 2023-05-09 11:46:32
*/
public interface TeacherService extends IService<Teacher> {


    Teacher selectTeacherByUsernameAndPwd(String username, String password);


    Teacher selectTeacherById(Long userId);
}
