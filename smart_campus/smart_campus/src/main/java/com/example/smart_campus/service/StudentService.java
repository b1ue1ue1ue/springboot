package com.example.smart_campus.service;

import com.example.smart_campus.pojo.Student;
import com.baomidou.mybatisplus.extension.service.IService;


/**
* @author blueluelue
* @description 针对表【tb_student】的数据库操作Service
* @createDate 2023-05-09 11:46:30
*/
public interface StudentService extends IService<Student> {


    Student selectStuentByUsernameAndPwd(String username, String password);

    Student selectStudentById(Long userId);
}
