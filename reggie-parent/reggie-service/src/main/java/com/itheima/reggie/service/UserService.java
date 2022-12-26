package com.itheima.reggie.service;

import com.itheima.reggie.common.ResultInfo;

public interface UserService {

    //发送短信
    void sendSms(String phone);

    //登录注册
    ResultInfo login(String phone, String code);

    //清理token
    void remove(String token);
}
