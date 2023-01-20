package com.nirlvy.smart_freezer_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nirlvy.smart_freezer_backend.common.Result;
import com.nirlvy.smart_freezer_backend.service.IRoleMenuService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author nirlvy
 * @since 2023-01-19
 */
@RestController
@RequestMapping("/roleMenu")
public class RoleMenuController {

    @Autowired
    private IRoleMenuService roleMenuService;

    @PostMapping("/{role}")
    public Result roleMenu(@PathVariable String role, @RequestBody List<Integer> menu_id) {
        roleMenuService.setRoleMenu(role, menu_id);
        return Result.success();
    }

    @GetMapping("/{role}")
    public Result getRoleMenu(@PathVariable String role) {

        return Result.success(roleMenuService.getRoleMenu(role));
    }
}
