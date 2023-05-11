package com.example.smart_campus.service;

import com.example.smart_campus.mapper.AdminMapper;
import com.example.smart_campus.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.smart_campus.pojo.Student;

import javax.annotation.Resource;

/**
* @author blueluelue
* @description 针对表【tb_admin】的数据库操作Service
* @createDate 2023-05-09 11:45:42
*/
public interface AdminService extends IService<Admin> {

    Admin selectAdminByUsernameAndPwd(String username, String password);

    Admin selectAdminById(Long userId);

}
