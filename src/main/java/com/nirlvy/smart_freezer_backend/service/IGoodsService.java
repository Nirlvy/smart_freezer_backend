package com.nirlvy.smart_freezer_backend.service;

import com.nirlvy.smart_freezer_backend.common.Result;
import com.nirlvy.smart_freezer_backend.entity.Goods;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author nirlvy
 * @since 2023-01-25
 */
public interface IGoodsService extends IService<Goods> {

    Result up(String name);

    Result del(List<Integer> ids);

    Result change(List<Goods> goods);

}
