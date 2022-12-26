package com.itheima.reggie.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

// 员工
@Data
public class Employee implements Serializable {

    //状态标识常量
    public static final Integer STATUS_DISABLE = 0;
    public static final Integer STATUS_ENABLE = 1;

    private Long id;//主键

    private String username;//用户名

    private String name;//姓名

    private String password;//密码

    private String phone;//手机号

    private String sex;//性别

    private String idNumber;//身份证号

    private Integer status;//状态 0:禁用 1:正常

    //@TableField(fill = FieldFill.INSERT)//新增的时候填充
    //@TableField(fill = FieldFill.UPDATE)//修改的时候填充
    //@TableField(fill = FieldFill.INSERT_UPDATE)//新增和修改的时候填充

    @TableField(fill = FieldFill.INSERT)//新增的时候填充
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)//新增和修改的时候填充
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;//更新时间

    @TableField(fill = FieldFill.INSERT)//新增的时候填充
    private Long createUser;//创建用户

    @TableField(fill = FieldFill.INSERT_UPDATE)//新增和修改的时候填充
    private Long updateUser;//更新用户
}
