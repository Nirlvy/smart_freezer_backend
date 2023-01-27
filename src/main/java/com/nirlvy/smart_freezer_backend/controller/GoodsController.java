package com.nirlvy.smart_freezer_backend.controller;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nirlvy.smart_freezer_backend.common.Result;
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

    @GetMapping("/up")
    public Result up(@RequestParam String name) {
        return goodsService.up(name);
    }

    @DeleteMapping("/del")
    public Result del(@RequestBody List<Integer> ids) {
        return goodsService.del(ids);
    }

    @PostMapping("/change")
    public Result change(@RequestBody List<Goods> goods) {
        return goodsService.change(goods);
    }
}
