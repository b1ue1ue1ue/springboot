package com.example.smart_campus.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.smart_campus.mapper.TeacherMapper;
import com.example.smart_campus.pojo.Admin;
import com.example.smart_campus.pojo.Teacher;
import com.example.smart_campus.service.TeacherService;
import com.example.smart_campus.utils.Result;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/sms/teacherController")
@RestController
public class TeacherController {

    @Resource
    TeacherService teacherService;

    @GetMapping("/getTeachers/{pn}/{pageSize}")
    public Result<Object>getTeachersByOpr(@PathVariable("pn") Integer pn,@PathVariable("pageSize")Integer pageSize,Teacher teacher){
        String name=teacher.getName();
        String clazzName=teacher.getClazzName();
        Page<Teacher>page=teacherService.page(new Page<>(pn,pageSize),
                new LambdaQueryWrapper<Teacher>().like(StrUtil.isNotBlank(name),Teacher::getName,name).like(StrUtil.isNotBlank(clazzName),Teacher::getClazzName,clazzName).orderByDesc(Teacher::getId));
        return Result.ok(page);
    }
    @DeleteMapping("/deleteTeacher")
    public Result<Object>deleteTeacher(@RequestBody List<Integer> ids){
        if(ids.size()==1){
            teacherService.removeById(ids.get(0));
        }
        else{
            teacherService.removeBatchByIds(ids);
        }
        return Result.ok();
    }
    @PostMapping("/saveOrUpdateTeacher")
    public Result<Object>saveOrUpdateTeacher(@RequestBody Teacher teacher){
        Integer id=teacher.getId();
        if(id==null){
            teacherService.save(teacher);
        }
        else{
            teacherService.update(teacher,new LambdaQueryWrapper<Teacher>().eq(Teacher::getId,id));
        }
        return Result.ok();
    }
}
