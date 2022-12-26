package com.itheima.reggie.controller;

import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Setmeal;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    //指定分类下的套餐列表
    @GetMapping("/setmeal/list")
    public ResultInfo findList(Long categoryId, Integer status) {
        List<Setmeal> setmealList = setmealService.findList(categoryId, status);
        return ResultInfo.success(setmealList);
    }
}
