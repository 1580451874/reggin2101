package com.itheima.reggie.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.domain.Category;
import com.itheima.reggie.domain.Setmeal;
import com.itheima.reggie.domain.SetmealDish;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.mapper.SetmealDishMapper;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.SetmealService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//套餐
@Service
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Page<Setmeal> findByPage(Integer pageNum, Integer pageSize, String name) {
        //1. 分页查询套餐信息
        //1-1 设置分页条件
        Page<Setmeal> page = new Page<>(pageNum, pageSize);

        //1-2 设置业务条件
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(name), Setmeal::getName, name);

        //1-3 执行查询
        page = setmealMapper.selectPage(page,wrapper);

        //2. 变量列表,得到每个套餐
        if (CollectionUtil.isNotEmpty(page.getRecords())) {
            for (Setmeal setmeal : page.getRecords()) {
                //3. 根据套餐的category_id去category表中获取分类名称
                Category category = categoryMapper.selectById(setmeal.getCategoryId());
                setmeal.setCategoryName(category.getName());

//                //4. 根据套餐id去setmeal_dish中查询套餐信息
//                LambdaQueryWrapper<SetmealDish> wrapper1 = new LambdaQueryWrapper<>();
//                wrapper1.eq(SetmealDish::getSetmealId,setmeal.getId());
//                List<SetmealDish> setmealDishList = setmealDishMapper.selectList(wrapper1);
//                setmeal.setSetmealDishes(setmealDishList);
            }
        }

        return page;
    }

    @Override
    public void save(Setmeal setmeal) {
        //1. 保存基本信息到setmeal表
        setmealMapper.insert(setmeal);

        //2. 获取菜品列表
        List<SetmealDish> setmealDishList = setmeal.getSetmealDishes();
        if (CollectionUtil.isNotEmpty(setmealDishList)){
            int i = 0;
            for (SetmealDish setmealDish : setmealDishList) {
                setmealDish.setSetmealId(setmeal.getId());//设置套餐id
                setmealDish.setSort(i++);
                setmealDishMapper.insert(setmealDish);
            }
        }
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        //1. 判断ids中是否有status=1
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getStatus, 1)
                .in(Setmeal::getId, ids);
        Integer num = setmealMapper.selectCount(wrapper);
        if (num > 0) {
            throw new CustomException("有套餐正处于售卖状态,不允许删除");
        }

        //2. 删除setmeal_dish表中setmeal_id在ids中的(条件删除
        //构建删除条件 delete from setmeal_dish where setmeal_id in #{ids}
        LambdaQueryWrapper<SetmealDish> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.in(SetmealDish::getSetmealId, ids);
        setmealDishMapper.delete(wrapper1);

        //3. 再删除id在ids中  DELETE FROM setmeal WHERE id IN ( ? , ? )
        setmealMapper.deleteBatchIds(ids);
    }

    @Override
    public Setmeal findById(Long id) {
        //1. 从套餐表中根据id查询基本信息
        Setmeal setmeal = setmealMapper.selectById(id);
        //2. 根据套餐的category_id去category表中获取分类名称
        Category category = categoryMapper.selectById(setmeal.getCategoryId());
        setmeal.setCategoryName(category.getName());

        //3. 根据套餐id去setmeal_dish中查询套餐信息
        LambdaQueryWrapper<SetmealDish> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(SetmealDish::getSetmealId, setmeal.getId());
        List<SetmealDish> setmealDishList = setmealDishMapper.selectList(wrapper1);
        setmeal.setSetmealDishes(setmealDishList);

        return setmeal;
    }

    @Override
    public void update(Setmeal setmeal) {
        //1. 根据套餐id更新套餐的基本信息到setmeal表
        setmealMapper.updateById(setmeal);

        //2. 根据setmeal_id删除setmeal_dish中得相关信息(条件删除)
        //delete from setmeal_dish where setmeal_id = #{id}
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId,setmeal.getId());
        setmealDishMapper.delete(wrapper);

        //3. 重新添加
        List<SetmealDish> setmealDishList = setmeal.getSetmealDishes();
        if (CollectionUtil.isNotEmpty(setmealDishList)) {
            int i = 0;
            for (SetmealDish setmealDish : setmealDishList) {
                setmealDish.setSetmealId(setmeal.getId());//设置套餐id
                setmealDish.setSort(i++);
                setmealDishMapper.insert(setmealDish);
            }
        }
    }

    @Override
    public List<Setmeal> findList(Long categoryId, Integer status) {
        //1. 封装查询条件
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getCategoryId,categoryId).eq(Setmeal::getStatus,status);
        return setmealMapper.selectList(wrapper);
    }

}
