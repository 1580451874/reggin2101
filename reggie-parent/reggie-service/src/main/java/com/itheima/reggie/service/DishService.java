package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.domain.Dish;

import java.util.List;

public interface DishService {

    //根据name分页查询
    Page findByPage(Integer pageNum, Integer pageSize, String name);

    //保存
    void save(Dish dish);

    //根据id查询
    Dish findById(Long id);
    //修改
    void update(Dish dish);

    //批量删除
    void deleteByIds(List<Long> ids);

    //查询菜品列表
    List<Dish> findList(Long categoryId);

    //查询菜品列表
    List<Dish> findList(Long categoryId, String name);

}
