package com.itheima.reggie.common;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//全局异常处理器
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    //处理唯一值重复的异常
    @ExceptionHandler(DuplicateKeyException.class)
    public ResultInfo handlerDuplicateKeyException(Exception e) {
        //1. 打印日志
        e.printStackTrace();

        if (e.getMessage().contains("idx_username")) {
            return ResultInfo.error("员工账号重复");
        }

        //2. 给前端提示
        return ResultInfo.error("填写的值重复");
    }

    //处理自定义异常
    @ExceptionHandler(CustomException.class)
    public ResultInfo handlerCustomException(Exception e) {
        //1. 打印日志
        e.printStackTrace();

        //2. 给前端提示
        return ResultInfo.error(e.getMessage());
    }

    //非预期异常
    @ExceptionHandler(Exception.class)
    public ResultInfo handlerException(Exception e) {
        //1. 打印日志
        e.printStackTrace();

        //2. 给前端提示
        return ResultInfo.error("服务器开小差了,请联系管理员");
    }
}
