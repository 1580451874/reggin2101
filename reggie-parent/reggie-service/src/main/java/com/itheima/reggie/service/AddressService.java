package com.itheima.reggie.service;

import com.itheima.reggie.domain.Address;

import java.util.List;

public interface AddressService {
    //新增
    void save(Address address);

    //查询地址列表
    List<Address> findList();

    //主键查询
    Address findById(Long id);

    //根据主键更新
    void updateById(Address address);

    //批量删除
    void deleteByIds(List<Long> ids);

    //设置默认地址
    void setDefault(Long id);

    //查询默认地址
    Address getDefault();
}
