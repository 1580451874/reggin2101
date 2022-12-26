package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.UserHolder;
import com.itheima.reggie.domain.Cart;
import com.itheima.reggie.mapper.CartMapper;
import com.itheima.reggie.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    @Autowired
    private CartMapper cartMapper;

    @Override
    public Cart add(Cart cart) {
        //1. 先判断当前添加的菜品或者套餐是否在数据表中已存在
        //select * from shopping_cart where user_id = #{登录用户id} and dish_id = #{传入的菜品id}
        //select * from shopping_cart where user_id = #{登录用户id} and setmeal_id = #{传入的套餐id}
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, UserHolder.get().getId())
                .eq(cart.getDishId() != null, Cart::getDishId, cart.getDishId())
                .eq(cart.getSetmealId() != null, Cart::getSetmealId, cart.getSetmealId());
        Cart cartFromDB = cartMapper.selectOne(wrapper);

        if (cartFromDB != null){
            //2. 当前要添加的这个菜品.套餐,在数据库中已经存在,此时只要将其number+1,更新回数据库
            cartFromDB.setNumber(cartFromDB.getNumber()+1);
            cartMapper.updateById(cartFromDB);

            return cartFromDB;
        }else{
            //3. 当前要添加的这个菜品.套餐,在数据库中不存在,需要新建一个cart,保存到库中
            cart.setUserId(UserHolder.get().getId());
            cart.setNumber(1);
            cart.setCreateTime(new Date());
            cartMapper.insert(cart);
            return cart;
        }
    }

    @Override
    public List<Cart> findList() {
        //select * from shopping_cart where user_id = #{登录用户id}
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId,UserHolder.get().getId());

        return cartMapper.selectList(wrapper);
    }

    @Override
    public Cart update(Cart cartPram) {
        //1. 确定更新的购物车
        Long dishId = cartPram.getDishId();
        Long setmealId = cartPram.getSetmealId();

        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dishId != null, Cart::getDishId, dishId);
        wrapper.eq(setmealId != null, Cart::getSetmealId, cartPram.getSetmealId());
        wrapper.eq(Cart::getUserId, UserHolder.get().getId());

        Cart cart = cartMapper.selectOne(wrapper);

        //2. 执行更新/删除操作
        if (cart != null) {
            cart.setNumber(cart.getNumber() - 1);
            if (cart.getNumber() <= 0) {
                //删除
                cartMapper.deleteById(cart.getId());
            } else {
                //更新
                cartMapper.updateById(cart);
            }
        }
        return cart;
    }

    @Override
    public void clean() {
        //delete from shopping_cart where user_id =  = #{登录用户id}
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId,UserHolder.get().getId());
        cartMapper.delete(wrapper);
    }
}
