package com.itheima.reggie.controller;

import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Employee;
import com.itheima.reggie.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    //登录
    @PostMapping("/employee/login")
    public ResultInfo login(HttpSession session, @RequestBody Map<String, String> map) {
        //1. 接收参数
        String username = map.get("username");
        String password = map.get("password");

        //2. 调用service登录
        ResultInfo resultInfo = employeeService.login(username, password);

        //3. 将员工信息,保存到session
        if (resultInfo.getCode() == 1) {
            Employee employee = (Employee) resultInfo.getData();
            session.setAttribute("SESSION_EMPLOYEE", employee);
        }

        //4. 返回结果
        return resultInfo;
    }

    //退出
    @PostMapping("/employee/logout")
    public ResultInfo logout(HttpSession session) {
        //1. 注销session
        session.invalidate();

        //2. 返回成功标识
        return ResultInfo.success(null);
    }

    //列表查询
    @GetMapping("/employee/find")
    public ResultInfo findList(String name) {
        //1. 调用service查询
        List<Employee> employeeList = employeeService.findByName(name);

        //2. 结果返回
        return ResultInfo.success(employeeList);
    }

    //保存
    //POST  PUT通过请求体提交的参数,后台要使用@RequestBody接收
    @PostMapping("/employee")
    public ResultInfo save(@RequestBody Employee employee) {
        //1. 调用service保存
        employeeService.save(employee);

        //2. 返回结果
        return ResultInfo.success(null);
    }

    //根据主键查询
    @GetMapping("/employee/{id}")
    public ResultInfo findById(@PathVariable("id") Long id) {
        Employee employee = employeeService.findById(id);
        return ResultInfo.success(employee);
    }

    //修改
    @PutMapping("/employee")
    public ResultInfo update(@RequestBody Employee employee) {
        employeeService.update(employee);
        return ResultInfo.success(null);
    }
}
