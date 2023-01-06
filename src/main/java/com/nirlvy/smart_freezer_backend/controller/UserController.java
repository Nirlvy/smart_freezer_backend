package com.nirlvy.smart_freezer_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nirlvy.smart_freezer_backend.entity.User;
import com.nirlvy.smart_freezer_backend.service.UserService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public boolean saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping
    public List<User> findAll() {
        return userService.list();
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return userService.removeById(id);
    }

    @GetMapping("/page")
    public IPage<User> findPage(@RequestParam(required = false) Integer id,
            @RequestParam(defaultValue = "") String userName, @RequestParam(defaultValue = "") String createTime,
            @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        IPage<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (id != null)
            queryWrapper.eq("id", id);
        queryWrapper.like("userName", userName);
        queryWrapper.like("createTime", createTime);
        return userService.page(page, queryWrapper);
    }

    // @GetMapping("/page")
    // public Map<String, Object> findPage(@RequestParam Integer id, @RequestParam
    // String userName,
    // @RequestParam String create_time, @RequestParam Integer pageNum,
    // @RequestParam Integer pageSize) {
    // return userService.findPage(id, userName, create_time, pageNum, pageSize);
    // }
}
