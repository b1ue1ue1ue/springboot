package com.example.smart_campus.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.smart_campus.pojo.Grade;
import com.example.smart_campus.service.GradeService;
import com.example.smart_campus.utils.Result;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags="年级控制层")
@RestController
@RequestMapping("sms/gradeController")
public class GradeController {
    @Resource
    private GradeService gradeService;

    /**
     *
     * 带条件的分页查询功能
     * @param pn 当前页码
     * @param pageSize 每页显示的记录数
     * @param gradeName 模糊查询的条件
     * @return 返回查询结果封装的Page对象
     */
    @ApiOperation("带条件的分页查询功能")
    @RequestMapping("/getGrades/{pn}/{pageSize}")
    public Result<Object> getGrades(@ApiParam("当前页码") @PathVariable Integer pn,
                                    @ApiParam("每页显示的记录数")@PathVariable Integer pageSize,
                                    @ApiParam("模糊查询的条件")@RequestParam(value = "gradeName",required = false)String gradeName){
        Page<Grade>page=new Page<Grade>(pn,pageSize);  //查询到第几页,每页大小

        LambdaQueryWrapper<Grade>queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(gradeName),Grade::getName,gradeName).orderByDesc(Grade::getId); //查询的条件


        IPage iPage=gradeService.getBaseMapper().selectPage(page,queryWrapper);
        return Result.ok(iPage);
    }

    /**
     * 单条记录和批量删除功能
     * @param ids 请求体中是要删除的记录
     * @return 返回result.ok()
     */

    @ApiOperation("单挑记录和批量删除的功能")
    @DeleteMapping("/deleteGrade")
    public Result<Object>deleteGrade(@RequestBody List<Integer> ids){
        if(ids.size()==1){
            //单条记录的删除
            gradeService.removeById(ids.get(0));
        }
        else{
            //批量删除
            gradeService.removeByIds(ids);
        }
        return Result.ok();
    }

    /**
     * 添加或者修改年级信息
     * @param grade
     * @return 返回result.ok()
     */
    @ApiOperation("添加或者修改年级信息")
    @PostMapping("saveOrUpdateGrade")
    public Result<Object>saveOrUpdateGrade(@RequestBody Grade grade){

        //判断请求体中是否有id，如果有就修改，否则就添加
        Integer id=grade.getId();
        if(id!=null){
            gradeService.update(grade,new LambdaQueryWrapper<Grade>().eq(Grade::getId,id));
        }
        else{
            gradeService.save(grade);
        }
        return Result.ok();

    }


    @GetMapping("/getGrades")
    public Result<Object>getGrades(){
        List<Grade>grades=gradeService.list(null);
        return Result.ok(grades);
    }


}
