package com.nirlvy.smart_freezer_backend.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nirlvy.smart_freezer_backend.common.Result;
import com.nirlvy.smart_freezer_backend.entity.Freezer;

public interface IFreezerService extends IService<Freezer> {

    public Map<String, Object> homeinfo(Integer id);

    Result home(Integer id);

    Result capacity(Integer id, Integer capacity);

}