package com.nirlvy.smart_freezer_backend.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nirlvy.smart_freezer_backend.common.Result;
import com.nirlvy.smart_freezer_backend.entity.Ulogin;
import com.nirlvy.smart_freezer_backend.entity.User;
import com.nirlvy.smart_freezer_backend.service.IUserService;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author nirlvy
 * @since 2023-01-07
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody Ulogin ulogin) {
        return userService.login(ulogin);
    }

    @PostMapping("/register")
    public Result register(@RequestBody Ulogin ulogin) {
        return userService.register(ulogin);
    }

    @PostMapping
    public boolean saveUser(@RequestBody User user) {
        return userService.sOu(user);
    }

    @GetMapping
    public List<User> findAll() {
        return userService.list();
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return userService.removeById(id);
    }

    @DeleteMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return userService.removeBatchByIds(ids);
    }

    @GetMapping("/page")
    public IPage<User> findPage(@RequestParam(required = false) Integer id,
            @RequestParam(defaultValue = "") String userName, @RequestParam(defaultValue = "") String createTime,
            @RequestParam(required = false) Integer shelves, @RequestParam(required = false) Integer sold,
            @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        return userService.findPage(id, userName, createTime, shelves, sold, pageNum, pageSize);
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        userService.export(response);
    }

    @PostMapping("/import")
    public boolean imp(MultipartFile file) throws Exception {
        return userService.imp(file);
    }
}
