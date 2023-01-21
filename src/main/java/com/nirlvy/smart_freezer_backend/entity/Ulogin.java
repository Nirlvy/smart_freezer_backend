package com.nirlvy.smart_freezer_backend.entity;

import java.util.List;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

@Data
public class Ulogin {

    private Integer id;
    @Alias("用户名")
    private String userName;

    @Alias("密码")
    private String password;

    private String token;

    @Alias("头像")
    private String img;
    
    private List<Integer> menus;
}
