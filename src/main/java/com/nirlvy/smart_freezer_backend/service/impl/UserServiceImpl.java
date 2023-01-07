package com.nirlvy.smart_freezer_backend.service.impl;

import com.nirlvy.smart_freezer_backend.entity.User;
import com.nirlvy.smart_freezer_backend.mapper.UserMapper;
import com.nirlvy.smart_freezer_backend.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author nirlvy
 * @since 2023-01-07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
