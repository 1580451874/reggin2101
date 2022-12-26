package com.itheima.reggie.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.UserHolder;
import com.itheima.reggie.domain.Address;
import com.itheima.reggie.domain.Cart;
import com.itheima.reggie.domain.Order;
import com.itheima.reggie.domain.OrderDetail;
import com.itheima.reggie.mapper.OrderDetailMapper;
import com.itheima.reggie.mapper.OrderMapper;
import com.itheima.reggie.service.AddressService;
import com.itheima.reggie.service.CartService;
import com.itheima.reggie.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private AddressService addressService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void submit(Order orderParam) {

        //1. 准备数据备用
        //1-1 根据地址id查询地址信息
        Address address = addressService.findById(orderParam.getAddressId());

        //1-2 查询当前用户的购物车信息
        List<Cart> cartList = cartService.findList();

        //2. 收到生成一个订单id
        Long orderId = IdWorker.getId();

        //double sum = 0;
        BigDecimal sum = new BigDecimal(0);

        //3. 生成订单详情
        for (Cart cart : cartList) {
            //每一个cart对象就应该生成一个orderDetail
            OrderDetail orderDetail = new OrderDetail();
            //拷贝cart中大部分属性到orderDetail
            BeanUtils.copyProperties(cart, orderDetail);

            //单独设置id和orderid
            orderDetail.setId(IdWorker.getId());
            orderDetail.setOrderId(orderId);
            System.out.println(orderDetail);

            //sum += amount * number
            sum = sum.add(cart.getAmount().multiply(new BigDecimal(cart.getNumber())));

            //将订单详情保存数据库
            orderDetailMapper.insert(orderDetail);
        }


        //4. 生成订单
        orderParam.setId(orderId); //一定用上面提前生成好的
        orderParam.setNumber(orderId + "");//根主键一致
        orderParam.setStatus(1);//待付款
        orderParam.setUserId(UserHolder.get().getId());
        orderParam.setOrderTime(new Date());
        orderParam.setCheckoutTime(new Date());
        orderParam.setAmount(sum);//总金额 =  每个菜品单价 * 数量 再相加
        orderParam.setUserName(UserHolder.get().getName());//登录用户名字
        orderParam.setPhone(address.getPhone());
        orderParam.setAddress(address.getDetail());
        orderParam.setConsignee(address.getConsignee());
        orderMapper.insert(orderParam);

        //5. 清空购物车
        cartService.clean();
    }

    @Override
    public Page<Order> findByPage(Integer pageNum, Integer pageSize) {
        //1. 设置分页条件
        Page<Order> page = new Page<>(pageNum, pageSize);

        //2. 设置业务条件
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, UserHolder.get().getId());
        wrapper.orderByDesc(Order::getOrderTime);// order by order_time desc

        //3. 执行查询
        page = orderMapper.selectPage(page, wrapper);

        //4. 获取每个订单
        if (CollectionUtil.isNotEmpty(page.getRecords())) {
            for (Order order : page.getRecords()) {
                //1. 根据订单id从订单详情表中查询每个订单的详情列表
                LambdaQueryWrapper<OrderDetail> wrapper1 = new LambdaQueryWrapper<>();
                wrapper1.eq(OrderDetail::getOrderId,order.getId());
                List<OrderDetail> orderDetailList = orderDetailMapper.selectList(wrapper1);
                order.setOrderDetails(orderDetailList);
            }
        }
        return page;
    }

    @Override
    public Page<Order> findByPageForManage(Integer pageNum, Integer pageSize, String number, Date beginTime, Date endTime) {
        //1. 设置分页条件
        Page<Order> page = new Page<>(pageNum, pageSize);

        //2. 设置业务条件
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(number), Order::getNumber, number)
                .between(beginTime != null, Order::getOrderTime, beginTime, endTime);

        return orderMapper.selectPage(page, wrapper);
    }
}
