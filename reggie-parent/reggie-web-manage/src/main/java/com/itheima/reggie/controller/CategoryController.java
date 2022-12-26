package com.itheima.reggie.controller;

import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Category;
import com.itheima.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //查询所有
    @GetMapping("/category/findAll")
    public ResultInfo findAll(){
        List<Category> categoryList = categoryService.findAll();
        return ResultInfo.success(categoryList);
    }

    //新增分类
    @PostMapping("/category")
    public ResultInfo save(@RequestBody Category category) {
        categoryService.save(category);
        return ResultInfo.success(null);
    }

    //修改分类
    @PutMapping("/category")
    public ResultInfo update(@RequestBody Category category){
        categoryService.update(category);
        return ResultInfo.success(null);
    }

    //删除分类
    @DeleteMapping("/category")
    public ResultInfo delete(Long id){
        return categoryService.delete(id);
    }

    //根据type查询分类信息
    @GetMapping("/category/list")
    public ResultInfo findByType(Integer type){
        List<Category> categoryList = categoryService.findByType(type);
        return ResultInfo.success(categoryList);
    }
}
