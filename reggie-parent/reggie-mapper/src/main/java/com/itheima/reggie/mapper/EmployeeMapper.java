package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.domain.Employee;
import org.springframework.stereotype.Repository;

//持久层: 按照要求继承BaseMapper接口
@Repository
public interface EmployeeMapper extends BaseMapper<Employee> {

}
