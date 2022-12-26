package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Setmeal;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    //分页查询
    @GetMapping("/setmeal/page")
    public ResultInfo findByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            String name
    ) {
        Page<Setmeal> page = setmealService.findByPage(pageNum, pageSize, name);
        return ResultInfo.success(page);
    }

    //保存
    @PostMapping("/setmeal")
    public ResultInfo save(@RequestBody Setmeal setmeal) {
        setmealService.save(setmeal);
        return ResultInfo.success(null);
    }

    //批量删除
    @DeleteMapping("/setmeal")
    public ResultInfo deleteByIds(@RequestParam("ids") List<Long> ids){
        setmealService.deleteByIds(ids);
        return ResultInfo.success(null);
    }

    //根据id查询套餐
    @GetMapping("/setmeal/{id}")
    public ResultInfo findById(@PathVariable("id") Long id) {
        Setmeal setmeal = setmealService.findById(id);
        return ResultInfo.success(setmeal);
    }
    //修改
    @PutMapping("/setmeal")
    public ResultInfo update(@RequestBody Setmeal setmeal){
        setmealService.update(setmeal);
        return ResultInfo.success(null);
    }
}
