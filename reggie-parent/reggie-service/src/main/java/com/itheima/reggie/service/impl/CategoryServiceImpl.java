package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Category;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> findAll() {
        return categoryMapper.selectList(null);
    }

    @Override
    public void save(Category category) {
        categoryMapper.insert(category);
    }

    @Override
    public void update(Category category) {
        categoryMapper.updateById(category);
    }

    @Override
    public ResultInfo delete(Long id) {
        //1. 根据分类id查询当前分类下是否有菜品,如果有,返回错误
        Integer num1 = categoryMapper.countDishByCategoryId(id);
        if (num1 > 0) {
            return ResultInfo.error("当前分类下有菜品,不能删除");
        }

        //2. 根据分类id查询当前分类下是否有套餐,如果有,返回错误
        Integer num2 = categoryMapper.countSetmealByCategoryId(id);
        if (num2 > 0) {
            return ResultInfo.error("当前分类下有套餐,不能删除");
        }

        //3. 执行删除分类
        categoryMapper.deleteById(id);
        return ResultInfo.success(null);
    }

    @Override
    public List<Category> findByType(Integer type) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getType,type);

        return categoryMapper.selectList(wrapper);
    }
}
