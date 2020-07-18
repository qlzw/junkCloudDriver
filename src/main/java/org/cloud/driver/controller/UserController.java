package org.cloud.driver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.cloud.driver.Utils.VerifyUtil;
import org.cloud.driver.model.User;
import org.cloud.driver.request.UserRequest;
import org.cloud.driver.response.Response;
import org.cloud.driver.response.ResultCode;
import org.cloud.driver.service.UserService;
import org.cloud.driver.vo.LoginVo;
import org.cloud.driver.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@Api(value = "/user",tags = "用户模块", description = "注册登陆接口")
@RequestMapping("/cd/user")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;

    @ApiOperation(value = "用户登陆",notes = "暂无",httpMethod = "POST")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Response<String> login(HttpServletRequest request, @RequestBody LoginVo loginVo){
        String token = userService.longin(request,loginVo);
        return Response.SUCCESS(ResultCode.USER_LOGIN_SUCESS,token);
    }

    @ApiOperation(value = "用户注销",notes = "暂无",httpMethod = "GET")
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public Response<String> logout(){
        return Response.SUCCESS(ResultCode.USER_LOGIN_SUCESS,"退出成功！");
    }

    @ApiOperation(value = "用户注册",notes = "暂无",httpMethod = "POST")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Response<String> register(HttpServletRequest request, @RequestBody UserRequest userRequest){
        return userService.register(request,userRequest);
    }

    @ApiOperation(value = "更改密码",notes = "暂无",httpMethod = "POST")
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public Response changePassword(User user, @RequestBody UserVo userVo){
        userService.changePassword(user, userVo);
        return Response.SUCCESS(ResultCode.SUCCESS);
    }

    @ApiOperation(value = "获取邮箱验证码",notes = "暂无",httpMethod = "GET")
    @RequestMapping(value = "/getCode/{email}", method = RequestMethod.GET)
    public Response getCode(HttpServletRequest request, @PathVariable("email") String email){
        return userService.sendCode(request, email);
    }

    @ApiOperation(value = "验证邮箱是否重复",notes = "暂无",httpMethod = "GET")
    @RequestMapping(value ="/validEmail/{email}", method = RequestMethod.GET)
    public Response validEmail(HttpServletRequest request, @PathVariable("email") String email){
        User user  = userService.getByEmail(email);
        if(user == null){
            return Response.SUCCESS(ResultCode.SUCCESS);
        }
        return Response.SUCCESS(ResultCode.USER_EMAIL_EXIST);
    }

    @ApiOperation(value = "验证用户名是否重复",notes = "暂无",httpMethod = "GET")
    @RequestMapping(value ="/validUsername/{username}", method = RequestMethod.GET)
    public Response validUsername(HttpServletRequest request, @PathVariable("username") String username){
        User user  = userService.getByUsername(username);
        if(user == null){
            return Response.SUCCESS(ResultCode.SUCCESS);
        }
        return Response.SUCCESS(ResultCode.USER_USERNAME_EXIST);
    }

    @ApiOperation(value = "获取图形验证码",notes = "有效时间1分钟",httpMethod = "GET")
    @RequestMapping(value = "/createCode", method = RequestMethod.GET)
    public void createCode(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        VerifyUtil verifyUtil = new VerifyUtil();
        verifyUtil.getRandCode(request, response);
        String code = (String) request.getSession().getAttribute(VerifyUtil.RANDOMCODEKEY);
        log.info("图形验证码是:{}",code);
    }

    @ApiOperation(value = "获取自己的用户名",notes = "暂无",httpMethod = "GET")
    @RequestMapping(value ="/getUsername", method = RequestMethod.GET)
    public Response<String> validEmail(User user){
        return Response.SUCCESS(ResultCode.SUCCESS,user.getUsername());
    }
}
