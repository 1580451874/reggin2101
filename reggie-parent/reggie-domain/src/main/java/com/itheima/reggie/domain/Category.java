package com.itheima.reggie.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

//分类
@Data
public class Category implements Serializable {

    private Long id;//主键

    private Integer type;//类型 1 菜品分类 2 套餐分类

    private String name;//分类名称

    private Integer sort; //顺序

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
