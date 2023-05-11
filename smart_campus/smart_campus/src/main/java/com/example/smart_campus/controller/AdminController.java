package com.example.smart_campus.controller;

import com.example.smart_campus.pojo.Admin;
import com.example.smart_campus.service.AdminService;
import com.example.smart_campus.utils.MD5;
import com.example.smart_campus.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;



//http://localhost:8080/sms/adminController/getAllAdmin/1/3
@RequestMapping("sms/adminController")
@RestController
public class AdminController {
    @Resource
    private AdminService adminService;


    @GetMapping("/getAllAdmin/{pn}/{pageSize}")
    public Result<Object>getAllAdmin(@PathVariable("pn")Integer pn,@PathVariable("pageSize")Integer pageSize,
                                     String adminName){
        Page<Admin>page=adminService.page(new Page<>(pn,pageSize),new LambdaQueryWrapper<Admin>().like(
                StrUtil.isNotBlank(adminName),Admin::getName,adminName).orderByDesc(Admin::getId));
        return Result.ok(page);
    }

    @DeleteMapping("/deleteAdmin")
    public Result<Object>deleteAdmin(@RequestBody List<Integer> ids) {
        if (ids.size() == 1) {
            adminService.removeById(ids.get(0));
        } else {
            adminService.removeBatchByIds(ids);
        }
        return Result.ok();
    }
    @PostMapping("/saveOrUpdateAdmin")
    public Result<Object>saveOrUpdateAdmin(@RequestBody Admin admin){
        Integer id=admin.getId();
        if(id==null){
            admin.setPassword(MD5.encrypt(admin.getPassword()));
            adminService.save(admin);
        }
        else{
            adminService.update(admin,new LambdaQueryWrapper<Admin>().eq(
                    Admin::getId,id
            ));
        }
        return Result.ok();
    }

}
