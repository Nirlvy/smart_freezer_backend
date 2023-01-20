package com.nirlvy.smart_freezer_backend.service.impl;

import com.nirlvy.smart_freezer_backend.entity.RoleMenu;
import com.nirlvy.smart_freezer_backend.mapper.RoleMenuMapper;
import com.nirlvy.smart_freezer_backend.service.IRoleMenuService;

import jakarta.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author nirlvy
 * @since 2023-01-19
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {

    @Resource
    private RoleMenuMapper roleMenuMapper;
     
    @Transactional
    @Override
    public void setRoleMenu(String role, List<Integer> menu_id) {
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role", role);
        roleMenuMapper.delete(queryWrapper);
        for (Integer menuId : menu_id) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setMenuId(menuId);
            roleMenu.setRole(role);
            roleMenuMapper.insert(roleMenu);
        }
    }

    @Override
    public List<Integer> getRoleMenu(String role) {
        return roleMenuMapper.selectByRole(role);
    }

}
