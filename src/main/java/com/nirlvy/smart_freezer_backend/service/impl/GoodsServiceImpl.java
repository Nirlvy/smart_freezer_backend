package com.nirlvy.smart_freezer_backend.service.impl;

import com.nirlvy.smart_freezer_backend.common.Result;
import com.nirlvy.smart_freezer_backend.common.ResultCode;
import com.nirlvy.smart_freezer_backend.entity.Goods;
import com.nirlvy.smart_freezer_backend.exception.ServiceException;
import com.nirlvy.smart_freezer_backend.mapper.GoodsMapper;
import com.nirlvy.smart_freezer_backend.service.IGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author nirlvy
 * @since 2023-01-25
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Override
    public Result up(String name) {
        Goods goods = new Goods();
        goods.setName(name);
        goods.setDisable(false);
        try {
            save(goods);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.STSTEM_ERROR, e);
        }
        return Result.success();
    }

    @Override
    public Result del(List<Integer> ids) {
        try {
            removeBatchByIds(ids);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.STSTEM_ERROR, e);
        }
        return Result.success();
    }

    @Override
    public Result change(List<Goods> goods) {
        try {
            List<Goods> goodsList = list();
            goodsList.stream().map(obj -> {
                obj.setDisable(false);
                return obj;
            }).collect(Collectors.toList());
            updateBatchById(goodsList);
            updateBatchById(goods);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.STSTEM_ERROR, e);
        }
        return Result.success();
    }

}
