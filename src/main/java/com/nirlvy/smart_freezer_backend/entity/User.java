package com.nirlvy.smart_freezer_backend.entity;

import java.sql.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@TableName(value = "user")
public class User {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String userName;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Integer shelves;
    private Integer sold;
    private Date createTime;
}
