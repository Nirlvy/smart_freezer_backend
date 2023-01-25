package com.nirlvy.smart_freezer_backend.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@TableName("shelvesLog")
@AllArgsConstructor
public class ShelvesLog {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer freezerId;
    private String name;
    private Boolean state;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime upTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime downTime;
}
