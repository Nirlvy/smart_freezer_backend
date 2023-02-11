package com.nirlvy.smart_freezer_backend.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@TableName("freezer")
public class Freezer {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private String position;
    private String location;
    private Integer capacity;
    private Integer shelves;
    private Boolean enable;
    private Boolean need;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime lastSupply;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime releaseTime;
}
