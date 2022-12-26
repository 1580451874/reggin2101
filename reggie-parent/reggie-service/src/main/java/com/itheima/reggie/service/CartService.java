package com.itheima.reggie.service;

import com.itheima.reggie.domain.Cart;

import java.util.List;

public interface CartService {
    //添加购物车
    Cart add(Cart cart);

    //查询指定用户的购物车列表
    List<Cart> findList();

    //修改购物车
    Cart update(Cart cartPram);

    //清空购物车
    void clean();
}
