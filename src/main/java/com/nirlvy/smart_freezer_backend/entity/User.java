package com.nirlvy.smart_freezer_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import cn.hutool.core.annotation.Alias;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author nirlvy
 * @since 2023-01-07
 */
@Getter
@Setter
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Alias("ID")
    private Integer id;

    @Alias("用户名")
    private String userName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Alias("密码")
    private String password;

    @Alias("上架数")
    private Integer shelves;

    @Alias("售出数")
    private Integer sold;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Alias("注册时间")
    private LocalDateTime createTime;

    @Alias("头像")
    private String img;
}
