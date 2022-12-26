package com.itheima.reggie.controller;

import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Address;
import com.itheima.reggie.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AddressController {
    @Autowired
    private AddressService addressService;

    //保存
    @PostMapping("/address")
    public ResultInfo save(@RequestBody Address address) {
        addressService.save(address);
        return ResultInfo.success(null);
    }

    //查询列表
    @GetMapping("/address/list")
    public ResultInfo findList() {
        List<Address> addressList = addressService.findList();
        return ResultInfo.success(addressList);
    }

    //主键查询
    @GetMapping("/address/{id}")
    public ResultInfo findById(@PathVariable("id") Long id) {
        Address address = addressService.findById(id);
        return ResultInfo.success(address);
    }

    //根据主键更新
    @PutMapping("/address")
    public ResultInfo updateById(@RequestBody Address address) {
        addressService.updateById(address);
        return ResultInfo.success(null);
    }
    //批量删除
    @DeleteMapping("/address")
    public ResultInfo deleteByIds(@RequestParam("ids") List<Long> ids){
        addressService.deleteByIds(ids);
        return ResultInfo.success(null);
    }

    //设置默认地址
    @PutMapping("/address/default")
    public ResultInfo setDefault(@RequestBody Address address){
        addressService.setDefault(address.getId());
        return ResultInfo.success(null);
    }

    //查询默认地址
    @GetMapping("/address/default")
    public ResultInfo setDefault(){
        Address address = addressService.getDefault();
        if (address != null){
            return ResultInfo.success(address);
        }else{
            return ResultInfo.error("当前用户未设置默认地址");
        }
    }
}
