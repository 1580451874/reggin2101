package com.itheima.reggie.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.domain.Category;
import com.itheima.reggie.domain.Dish;
import com.itheima.reggie.domain.DishFlavor;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.mapper.DishFlavorMapper;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Override
    public Page findByPage(Integer pageNum, Integer pageSize, String name) {
        //1. 分页查询菜品信息
        //1-1 设置分页条件
        Page<Dish> page = new Page<>(pageNum, pageSize);

        //1-2 设置业务条件
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);

        //1-3 执行查询
        page = dishMapper.selectPage(page, wrapper);

        //2. 遍历分页列表,得到每一个菜品
        List<Dish> dishList = page.getRecords();
        if (CollectionUtil.isNotEmpty(dishList)){
            for (Dish dish : dishList) {
                //3 根据菜品categoryId查询它的分类名称
                Category category = categoryMapper.selectById(dish.getCategoryId());
                dish.setCategoryName(category.getName());

//                //4 根据菜品的id查询它的口味
//                LambdaQueryWrapper<DishFlavor> wrapper1 = new LambdaQueryWrapper<>();
//                wrapper1.eq(DishFlavor::getDishId,dish.getId());
//                List<DishFlavor> dishFlavorList = dishFlavorMapper.selectList(wrapper1);
//                dish.setFlavors(dishFlavorList);
            }
        }

        System.out.println(page);

        return page;
    }

    @Override
    public void save(Dish dish) {
        //1. 保存菜品基本信息
        dishMapper.insert(dish);//主键可以自动返回

        //2. 获取口味列表,遍历
        if (CollectionUtil.isNotEmpty(dish.getFlavors())) {
            for (DishFlavor flavor : dish.getFlavors()) {
                //为口味设置上菜品id
                flavor.setDishId(dish.getId());
                //保存口味
                dishFlavorMapper.insert(flavor);
            }
        }
    }

    @Override
    public Dish findById(Long id) {
        //1. 根据id从菜品表进行主键查询,获取菜品信息
        Dish dish = dishMapper.selectById(id);

        //2 根据菜品categoryId查询它的分类名称
        Category category = categoryMapper.selectById(dish.getCategoryId());
        dish.setCategoryName(category.getName());

        //3 根据菜品的id查询它的口味
        LambdaQueryWrapper<DishFlavor> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> dishFlavorList = dishFlavorMapper.selectList(wrapper1);
        dish.setFlavors(dishFlavorList);

        return dish;
    }

    @Override
    public void update(Dish dish) {
        //1. 根据菜品id修改菜品表信息
        dishMapper.updateById(dish);

        //2. 根据菜品id删除口味的信息
        //delete from dish_flavor where dish_id = #{}
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId,dish.getId());
        dishFlavorMapper.delete(wrapper);

        //3. 遍历前端传入的口味列表,重新保存
        if (CollectionUtil.isNotEmpty(dish.getFlavors())) {
            for (DishFlavor flavor : dish.getFlavors()) {
                //为口味设置上菜品id
                flavor.setDishId(dish.getId());
                //保存口味
                dishFlavorMapper.insert(flavor);
            }
        }
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        //1. 如果其中一个是在售, 就全部不允许删除
        //select count(1) from dish where status = '1' and id in #{ids}
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getStatus, 1);//在售
        wrapper.in(Dish::getId, ids);//id in #{ids}
        Integer count = dishMapper.selectCount(wrapper);
        if (count > 0) {
            throw new CustomException("当前菜品有处于在售状态的,不能删除");
        }

        if (CollectionUtil.isNotEmpty(ids)) {
            for (Long id : ids) {
                //2. 删除当前菜品的口味列表
                LambdaQueryWrapper<DishFlavor> wrapper1 = new LambdaQueryWrapper<>();
                wrapper1.eq(DishFlavor::getDishId, id);
                dishFlavorMapper.delete(wrapper1);

                //3. 根据菜品id删除当前菜品信息
                dishMapper.deleteById(id);
            }
        }
    }

    @Override
    public List<Dish> findList(Long categoryId) {
        //1. 创建条件封装器
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getStatus, 1)//status = 1表示在售
                .eq(Dish::getCategoryId, categoryId);//分类id

        //2. 执行查询
        return dishMapper.selectList(wrapper);
    }

    @Override
    public List<Dish> findList(Long categoryId, String name) {
        //1. 创建条件封装器
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getStatus, 1)//status = 1表示在售
                .eq(categoryId != null, Dish::getCategoryId, categoryId)//分类id
                .like(StringUtils.isNotEmpty(name), Dish::getName, name);//菜品名称

        //2. 执行查询
        List<Dish> dishList = dishMapper.selectList(wrapper);

        //=====================添加一段代码,用于查询每个菜品的口味信息==================================//
        if (CollectionUtil.isNotEmpty(dishList)) {
            for (Dish dish : dishList) {
                LambdaQueryWrapper<DishFlavor> wrapper1 = new LambdaQueryWrapper<>();
                wrapper1.eq(DishFlavor::getDishId, dish.getId());
                List<DishFlavor> dishFlavorList = dishFlavorMapper.selectList(wrapper1);
                dish.setFlavors(dishFlavorList);
            }
        }
        return dishList;
    }
}
