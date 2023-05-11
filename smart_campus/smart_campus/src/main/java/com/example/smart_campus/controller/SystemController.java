package com.example.smart_campus.controller;

import com.example.smart_campus.pojo.Admin;
import com.example.smart_campus.pojo.LoginForm;
import com.example.smart_campus.pojo.Student;
import com.example.smart_campus.pojo.Teacher;
import com.example.smart_campus.service.AdminService;
import com.example.smart_campus.service.StudentService;
import com.example.smart_campus.service.TeacherService;
import com.example.smart_campus.utils.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jdk.nashorn.internal.parser.Token;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.awt.image.ImageWatched;


import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/sms/system")
public class SystemController {
    @Resource
    private AdminService adminService;
    @Resource
    private StudentService studentService;
    @Resource
    private TeacherService teacherService;
    /**
     *
     * 获取验证码图片返回给浏览器
     * @param session
     * @param response
     * @throws IOException
     */
    @RequestMapping("/getVerifiCodeImage")
    public void getVeriCodeImage(HttpSession session, HttpServletResponse response)throws IOException {
        //使用工具类CrateVerifiCodeIamge 获取验证码图片
        BufferedImage verifiCodeImage= CreateVerifiCodeImage.getVerifiCodeImage();
        String code=new String(CreateVerifiCodeImage.getVerifiCode());
        //将验证码的值保存到session中,用于登录时校验
        session.setAttribute("code",code);
        //将验证码图片返回给浏览器
        ImageIO.write(verifiCodeImage,"JPG",response.getOutputStream());
    }

    /**
     * 登录功能 进行验证码的校验和用户名和密码的校验
     * 登录成功时 生成token返回给浏览器
     * @param loginForm 生成的登录信息表单
     * @param session session域
     * @return
     */
    @PostMapping("/login")
    public Result<Object> login(@RequestBody LoginForm loginForm,HttpSession session){
        //先获取session域中的验证码值判断验证码是否成功
        String code=(String)session.getAttribute("code");
        //判断session域是否失效
        if(code==null||"".equals(code)){
            return Result.fail().message("验证码失效，请重新输入验证码");
        }
        else{
            //获取用户输入的验证码
            String userInputCode=loginForm.getVerifiCode();
            if(!userInputCode.equalsIgnoreCase(code)){
                return Result.fail().message("验证码输入有误");
            }
            else{
                //销毁session域中的验证码的值
                session.removeAttribute("code");
                //获取用户类型 根据用户类型去数据相应表中做用户名和密码的校验
                Integer userType=loginForm.getUserType();
                String username=loginForm.getUsername();
                String password=loginForm.getPassword();
                Map<String,Object> map=new LinkedHashMap<>();
                System.out.println("userType"+userType);
                System.out.println("username"+username);
                System.out.println("password"+password);
                password= MD5.encrypt(password);
                if(userType==1){
                    Admin admin=adminService.selectAdminByUsernameAndPwd(username,password);
                    //判断是否输入的用户名和密码存在于数据库中
                    if(admin!=null){
                        // 登入成功后 需要根据用户id和用户类型生成token
                        String token=JwtHelper.createToken(admin.getId().longValue(),userType);
                        map.put("token",token);
                        //需要将用户类型返回给浏览器
                        map.put("userType",userType);
                        return Result.ok(map);
                    }
                    else{
                        return Result.fail().message("用户名或密码错误");
                    }
                }
                else if(userType==2){
                    Student student=studentService.selectStuentByUsernameAndPwd(username,password);
                    if(student!=null){
                        String token=JwtHelper.createToken(student.getId().longValue(),userType);
                        map.put("token",token);
                        //需要将用户类型返回给浏览器
                        map.put("userType",userType);
                        return Result.ok(map);
                    }
                    else{
                        return Result.fail().message("用户名或密码错误");
                    }
                }
                else{
                    Teacher teacher=teacherService.selectTeacherByUsernameAndPwd(username,password);
                    if(teacher!=null){
                        String token=JwtHelper.createToken(teacher.getId().longValue(),userType);
                        map.put("token",token);
                        //需要将用户类型返回给浏览器
//                        map.put("userType",userType);
                        return Result.ok(map);
                    }
                    else{
                        return Result.fail().message("用户名或密码错误");
                    }
                }
            }
        }
    }

    @GetMapping("/getInfo")
    public Result<Object>getInfo(@RequestHeader("token")String token){
        //先判断token是否有效
        if(JwtHelper.isExpiration(token)){
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        else {
            //解析token 获取用户id和用户类型将用户类型返回给浏览器
            Long userId=JwtHelper.getUserId(token);
            Integer userType=JwtHelper.getUserType(token);
            Map<String,Object>map=new LinkedHashMap<>();
            map.put("userType",userType);
            if(userType==1){
                Admin admin=adminService.selectAdminById(userId);
                map.put("user",admin);
            }
            else if(userType==2){
                Student student=studentService.selectStudentById(userId);
                map.put("user",student);
            }
            else{
                Teacher teacher=teacherService.selectTeacherById(userId);
                map.put("user",teacher);
            }
            return Result.ok(map);
        }
    }

    /**
     * 上传头像功能存储到项目运行目录下的upload目录下
     * @param multipartFile
     * @return
     * @throws IOException
     */

    @ApiOperation("上传头像")
    @PostMapping("/headerImgUpload")
    public Result<Object>headerImgUpload(@ApiParam("封装请求体重的图片二进制数据")MultipartFile multipartFile)throws IOException{
        String originalFilename=multipartFile.getOriginalFilename();
        assert originalFilename!=null;
        String photoName= UUID.randomUUID().toString().replace("-","").toLowerCase().concat(originalFilename.substring(originalFilename.lastIndexOf(".")));
        String path=System.getProperty("user.dir")+"/upload/".concat(photoName);
        saveFile(multipartFile,path);
        return Result.ok("upload/".concat(photoName));


    }
    public void saveFile(MultipartFile multipartFile,String path)throws IOException{
        File dir=new File(path);
        if(!dir.exists()){
            dir.mkdir();
        }
        File file=new File(path);
        multipartFile.transferTo(file);
    }





}
