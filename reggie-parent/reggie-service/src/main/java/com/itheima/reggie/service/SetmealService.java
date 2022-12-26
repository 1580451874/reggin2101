package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.domain.Setmeal;

import java.util.List;

//套餐
public interface SetmealService {
    //分页查询
    Page<Setmeal> findByPage(Integer pageNum, Integer pageSize, String name);

    //保存
    void save(Setmeal setmeal);

    //批量删除
    void deleteByIds(List<Long> ids);

    //主键查询
    Setmeal findById(Long id);

    //更新套餐
    void update(Setmeal setmeal);

    //查询指定分类下的套餐信息
    List<Setmeal> findList(Long categoryId, Integer status);
}
