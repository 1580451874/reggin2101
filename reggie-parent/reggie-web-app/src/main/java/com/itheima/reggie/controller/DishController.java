package com.itheima.reggie.controller;

import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Dish;
import com.itheima.reggie.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DishController {

    @Autowired
    private DishService dishService;

    //查询指定分类下菜品
    @GetMapping("/dish/list")
    public ResultInfo findList(Long categoryId, Integer status) {
        List<Dish> dishList = dishService.findList(categoryId, null);
        return ResultInfo.success(dishList);
    }
}
