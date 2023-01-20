package com.nirlvy.smart_freezer_backend.service;

import com.nirlvy.smart_freezer_backend.entity.RoleMenu;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author nirlvy
 * @since 2023-01-19
 */
public interface IRoleMenuService extends IService<RoleMenu> {

    void setRoleMenu(String role, List<Integer> menu_id);

    List<Integer> getRoleMenu(String role);

}
