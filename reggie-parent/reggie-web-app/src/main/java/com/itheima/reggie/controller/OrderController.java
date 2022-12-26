package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Order;
import com.itheima.reggie.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    //创建订单
    @PostMapping("/order/submit")
    public ResultInfo submit(@RequestBody Order orderParam) {
        orderService.submit(orderParam);
        return ResultInfo.success(null);
    }

    //分页查询
    @GetMapping("/order/userPage")
    public ResultInfo findByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize
    ) {
        Page<Order> page = orderService.findByPage(pageNum, pageSize);
        return ResultInfo.success(page);
    }
}
