package com.nirlvy.smart_freezer_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nirlvy.smart_freezer_backend.common.Result;
import com.nirlvy.smart_freezer_backend.entity.Freezer;
import com.nirlvy.smart_freezer_backend.service.IFreezerService;

@RestController
@RequestMapping("/freezer")
public class FreezerController {

    @Autowired
    private IFreezerService freezerService;

    @GetMapping("/home")
    public Result homeinfo(@RequestParam Integer id) {
        return Result.success(freezerService.homeinfo(id));
    }

    @GetMapping("/list")
    public List<Freezer> list(@RequestParam Integer id) {
        QueryWrapper<Freezer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", id);
        return freezerService.list(queryWrapper);
    }
}
