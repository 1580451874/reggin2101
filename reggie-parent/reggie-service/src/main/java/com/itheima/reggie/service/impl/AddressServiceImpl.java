package com.itheima.reggie.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.UserHolder;
import com.itheima.reggie.domain.Address;
import com.itheima.reggie.mapper.AddressMapper;
import com.itheima.reggie.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public void save(Address address) {
        //1. 补齐参数
        address.setUserId(UserHolder.get().getId());
        address.setIsDefault(0);//0 表示非默认地址

        //2. 执行保存
        addressMapper.insert(address);
    }

    @Override
    public List<Address> findList() {
        //1. 设置查询条件
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId,UserHolder.get().getId());

        //2. 执行查询
        return addressMapper.selectList(wrapper);
    }

    @Override
    public Address findById(Long id) {
        return addressMapper.selectById(id);
    }

    @Override
    public void updateById(Address address) {
        addressMapper.updateById(address);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        if (CollectionUtil.isNotEmpty(ids)) {
            addressMapper.deleteBatchIds(ids);
        }
    }

    @Override
    public void setDefault(Long id) {
        //1. 将当前用户的所有地址设置为非默认
        //update address_book set is_default = 0 where user_id = #{登录用户}
        //1-1 设置更新内容(set)
        Address address = new Address();
        address.setIsDefault(0);

        //1-2 设置更新条件(where)
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, UserHolder.get().getId());

        //1-3 执行更新
        addressMapper.update(address, wrapper);

        //2. 将指定id的地址设置为默认
        //update address_book set is_default = 1 where id = #{传入的地址}
        Address address1 = new Address();
        address1.setIsDefault(1);
        address1.setId(id);
        addressMapper.updateById(address1);
    }

    @Override
    public Address getDefault() {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId,UserHolder.get().getId())
                .eq(Address::getIsDefault,1);//1 默认地址
        return addressMapper.selectOne(wrapper);
    }
}
