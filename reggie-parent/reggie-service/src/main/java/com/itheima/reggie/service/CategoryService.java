package com.itheima.reggie.service;

import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Category;

import java.util.List;

public interface CategoryService {

    //查询所有
    List<Category> findAll();

    //保存
    void save(Category category);

    //修改
    void update(Category category);

    //删除分类
    ResultInfo delete(Long id);

    //根据type查询分类
    List<Category> findByType(Integer type);
}
