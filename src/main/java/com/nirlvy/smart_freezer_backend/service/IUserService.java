package com.nirlvy.smart_freezer_backend.service;

import com.nirlvy.smart_freezer_backend.entity.Ulogin;
import com.nirlvy.smart_freezer_backend.entity.User;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author nirlvy
 * @since 2023-01-07
 */
public interface IUserService extends IService<User> {

    Ulogin login(Ulogin ulogin);

    boolean export(HttpServletResponse response) throws Exception;

    boolean imp(MultipartFile file) throws Exception;

    IPage<User> findPage(Integer id, String userName, String createTime, Integer shelves, Integer sold, Integer pageNum,
            Integer pageSize);

    User register(Ulogin ulogin);

    boolean sOu(User user);

}
