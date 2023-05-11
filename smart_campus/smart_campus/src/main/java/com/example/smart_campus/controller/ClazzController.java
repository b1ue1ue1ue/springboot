package com.example.smart_campus.controller;

import com.example.smart_campus.pojo.Clazz;
import com.example.smart_campus.service.ClazzService;
import com.example.smart_campus.utils.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/sms/clazzController")
public class ClazzController {
    @Resource
    private ClazzService clazzService;


    @ApiOperation("根据分页带条件查询班级信息")
    @GetMapping("/getClazzsByOpr/{pn}/{pageSize}")
    public Result<Object> getClazzsByOpr(@ApiParam("当前页码") @PathVariable("pn") Integer pn,
                                         @ApiParam("每页显示记录数") @PathVariable("pageSize") Integer pageSize,
                                         @ApiParam("请求参数中模糊查询的条件封装") Clazz clazz) {
        String gradeName=clazz.getGradeName();
        String name=clazz.getName();
        LambdaQueryWrapper<Clazz> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StrUtil.isNotBlank(gradeName), Clazz::getGradeName, gradeName).
                like(StrUtil.isNotBlank(name), Clazz::getName, name).orderByDesc(Clazz::getId);
        Page<Clazz> page = clazzService.page(new Page<>(pn, pageSize), lambdaQueryWrapper);
        return Result.ok(page);
    }

    @PostMapping("/saveOrUpdateClazz")
    public Result<Object>saveOrUpdateClazz(@RequestBody Clazz clazz){
        Integer id=clazz.getId();
        if(id==null){  //说明没有设置过id,当前是要添加操作
            clazzService.save(clazz);
        }
        else{
            clazzService.update(clazz,new LambdaQueryWrapper<Clazz>().eq(Clazz::getId,id));
        }
        return Result.ok();
    }

    @DeleteMapping("/deleteClazz")
    public Result<Object>deleteClazz(@RequestBody List<Integer> ids){
        if(ids.size()==1){
            clazzService.removeById(ids.get(0));
        }
        else{
            clazzService.removeBatchByIds(ids);
        }
        return Result.ok();
    }


    @GetMapping("/getClazzs")
    public Result<Object>getClazz(){
        List<Clazz>clazzes= clazzService.list(null);
        return Result.ok(clazzes);
    }
}
