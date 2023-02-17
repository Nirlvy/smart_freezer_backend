package com.nirlvy.smart_freezer_backend.service;

import com.nirlvy.smart_freezer_backend.common.Result;
import com.nirlvy.smart_freezer_backend.entity.ShelvesLog;

import jakarta.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author nirlvy
 * @since 2023-01-24
 */
public interface IShelvesLogService extends IService<ShelvesLog> {
    IPage<ShelvesLog> findPage(Integer[] freezerId, String[] name, Boolean state, String upTime,
            String downTime, Integer pageNum, Integer pageSize);

    Result up(Integer id, String name, Integer num);

    void export(HttpServletResponse response, Integer[] freezerId) throws Exception;

    Result freezer(Integer id);

    Result homeinfo(Integer[] freezerId);
}
