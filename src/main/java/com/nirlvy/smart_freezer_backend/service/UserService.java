package com.nirlvy.smart_freezer_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nirlvy.smart_freezer_backend.entity.User;
import com.nirlvy.smart_freezer_backend.mapper.UserMapper;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public Integer save(User user) {
        if (user.getId() == null)
            return userMapper.insert(user);
        else
            return userMapper.update(user);
    }
}
