package com.itheima.reggie.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Employee;
import com.itheima.reggie.mapper.EmployeeMapper;
import com.itheima.reggie.service.EmployeeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public ResultInfo login(String username, String password) {

        //1. 根据username查询,如果查不到,返回用户不存在
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername, username);
        Employee employee = employeeMapper.selectOne(wrapper);
        if (employee == null) {
            return ResultInfo.error("用户名不存在");
        }

        //2. 前端密码加密之后 VS 数据库查询到的密码  如果失败,返回密码错误
        String passwordWithMd5 = SecureUtil.md5(password);
        if (!StringUtils.equals(passwordWithMd5, employee.getPassword())) {
            return ResultInfo.error("密码错误");
        }

        //3. 校验用户状态,如果是禁用,返回禁用状态
        if (employee.getStatus() == Employee.STATUS_DISABLE) {
            return ResultInfo.error("当前用户位于禁用状态,不允许登录");
        }

        //4. 如果上面条件都通过,返回登录成功
        return ResultInfo.success(employee);
    }

    @Override
    public List<Employee> findByName(String name) {
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        return employeeMapper.selectList(wrapper);
    }

    @Override
    public void save(Employee employee) {
        //1. 补齐参数
        employee.setPassword(SecureUtil.md5("123"));//设置默认密码为123, 进行加密
        employee.setStatus(Employee.STATUS_ENABLE);//默认激活

//        //时间
//        employee.setCreateTime(new Date());
//        employee.setUpdateTime(new Date());
//
//        //todo 操作人
//        employee.setCreateUser(1L);
//        employee.setUpdateUser(1L);

        //2. 调用mapper
        employeeMapper.insert(employee);
    }

    @Override
    public void update(Employee employee) {
//        //1. 重新赋值参数
//        employee.setUpdateTime(new Date());
//        employee.setUpdateUser(1L);

        //2. 执行修改
        employeeMapper.updateById(employee);
    }

    @Override
    public Employee findById(Long id) {
        return employeeMapper.selectById(id);
    }

}
