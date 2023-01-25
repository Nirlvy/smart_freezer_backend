package com.nirlvy.smart_freezer_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nirlvy.smart_freezer_backend.entity.Goods;
import com.nirlvy.smart_freezer_backend.service.IGoodsService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author nirlvy
 * @since 2023-01-25
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IGoodsService goodsService;

    @GetMapping
    public List<Goods> goods() {
        return goodsService.list();
    }
}
