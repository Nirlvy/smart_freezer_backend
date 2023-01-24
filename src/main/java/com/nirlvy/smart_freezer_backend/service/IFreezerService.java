package com.nirlvy.smart_freezer_backend.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nirlvy.smart_freezer_backend.entity.Freezer;

public interface IFreezerService extends IService<Freezer> {

    Map<String, Object> homeinfo(Integer id);

}