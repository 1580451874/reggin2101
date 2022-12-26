package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Dish;
import com.itheima.reggie.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DishController {

    @Autowired
    private DishService dishService;

    //分页查询
    @GetMapping("/dish/page")
    public ResultInfo findByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            String name) {

        Page page = dishService.findByPage(pageNum,pageSize,name);
        return ResultInfo.success(page);
    }

    //新增
    @PostMapping("/dish")
    public ResultInfo save(@RequestBody Dish dish){
        dishService.save(dish);
        return ResultInfo.success(null);
    }

    //根据id查询菜品信息
    @GetMapping("/dish/{id}")
    public ResultInfo findById(@PathVariable("id") Long id){
        Dish dish = dishService.findById(id);
        return ResultInfo.success(dish);
    }

    //修改
    @PutMapping("/dish")
    public ResultInfo update(@RequestBody Dish dish){
        dishService.update(dish);
        return ResultInfo.success(null);
    }

    //批量删除
    @DeleteMapping("/dish")
    public ResultInfo deleteByIds(@RequestParam("ids") List<Long> ids){
        dishService.deleteByIds(ids);
        return ResultInfo.success(null);
    }

    //查询菜品列表
    @GetMapping("/dish/list")
    public ResultInfo findList(Long categoryId) {
        List<Dish> dishList = dishService.findList(categoryId);
        return ResultInfo.success(dishList);
    }
}
