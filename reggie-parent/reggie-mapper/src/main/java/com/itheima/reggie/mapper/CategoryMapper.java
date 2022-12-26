package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.domain.Category;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMapper extends BaseMapper<Category> {
    //统计指定分类下的菜品数量
    @Select("select count(1) from dish where category_id = #{id}")
    Integer countDishByCategoryId(Long id);

    //统计指定分类下的套餐数量
    @Select("select count(1) from setmeal where category_id = #{id}")
    Integer countSetmealByCategoryId(Long id);
}
