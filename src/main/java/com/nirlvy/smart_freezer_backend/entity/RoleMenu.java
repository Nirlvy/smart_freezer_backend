package com.nirlvy.smart_freezer_backend.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author nirlvy
 * @since 2023-01-19
 */
@Getter
@Setter
@TableName("roleMenu")
public class RoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    private String role;

    private Integer menuId;
}
