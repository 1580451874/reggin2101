package com.itheima.reggie.controller;

import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Cart;
import com.itheima.reggie.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    //新增购物车
    @PostMapping("/cart/add")
    public ResultInfo add(@RequestBody Cart cart) {
        Cart CartFromDb = cartService.add(cart);

        //返回操作之后的购物车数据, 是因为前端需要读取里面的number值
        return ResultInfo.success(CartFromDb);
    }

    //查询指定用户的购物车列表
    @GetMapping("/cart/list")
    public ResultInfo findList() {
        List<Cart> cartList = cartService.findList();
        return ResultInfo.success(cartList);
    }

    //修改购物车
    @PostMapping("/cart/sub")
    public ResultInfo update(@RequestBody Cart cartPram) {
        Cart cart = cartService.update(cartPram);
        return ResultInfo.success(cart);
    }

    //清空购物车
    @DeleteMapping("/cart/clean")
    public ResultInfo clean() {
        cartService.clean();
        return ResultInfo.success(null);
    }
}
