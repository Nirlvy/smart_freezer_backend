package com.nirlvy.smart_freezer_backend.entity;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

@Data
public class Ulogin {
    @Alias("用户名")
    private String userName;

    @Alias("密码")
    private String password;

    private String token;

    @Alias("头像")
    private String img;
}
