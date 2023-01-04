package com.nirlvy.smart_freezer_backend.entity;

import java.sql.Date;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String user_name;
    private String password;
    private Date create_time;
}
