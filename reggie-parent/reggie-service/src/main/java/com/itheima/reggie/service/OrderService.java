package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.domain.Order;

import java.util.Date;

public interface OrderService {

    //提交订单
    void submit(Order orderParam);

    //分页查询
    Page<Order> findByPage(Integer pageNum, Integer pageSize);

    //分页条件查询
    Page<Order> findByPageForManage(Integer pageNum, Integer pageSize, String number, Date beginTime, Date endTime);
}
