package com.nirlvy.smart_freezer_backend.service.impl;

import com.nirlvy.smart_freezer_backend.entity.Goods;
import com.nirlvy.smart_freezer_backend.mapper.GoodsMapper;
import com.nirlvy.smart_freezer_backend.service.IGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author nirlvy
 * @since 2023-01-25
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

}
