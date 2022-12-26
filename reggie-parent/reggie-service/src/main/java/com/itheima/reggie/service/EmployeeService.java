package com.itheima.reggie.service;

import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Employee;

import java.util.List;

public interface EmployeeService {
    //登录
    ResultInfo login(String username, String password);

    //根据name查询
    List<Employee> findByName(String name);

    //保存
    void save(Employee employee);

    //根据id查询
    Employee findById(Long id);

    //修改
    void update(Employee employee);
}
