package com.itheima.reggie.controller;

import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    //发送短信
    @PostMapping("/user/sendMsg")
    public ResultInfo sendSms(@RequestBody Map<String, String> map) {
        //1. 接收参数
        String phone = map.get("phone");

        //2. 调用service
        userService.sendSms(phone);

        return ResultInfo.success("发送短信成功");
    }

    //登录注册
    @PostMapping("/user/login")
    public ResultInfo login(@RequestBody Map<String, String> map) {
        //1. 获取参数
        String phone = map.get("phone");
        String code = map.get("code");

        //2. 调用service进行登录
        ResultInfo resultInfo = userService.login(phone, code);

        //3. 返回结果
        return resultInfo;
    }

    //退出
    //@RequestHeader("key") 可以从请求头中获取任意键的值
    @PostMapping("/user/logout")
    public ResultInfo logout(@RequestHeader("Authorization") String token){
        //1. 获取token
        token= token.replace("Bearer","").trim();
        //2. 调用service清理redis中当前token信息
        userService.remove(token);
        //3. 返回结果
        return ResultInfo.success(null);
    }
}
