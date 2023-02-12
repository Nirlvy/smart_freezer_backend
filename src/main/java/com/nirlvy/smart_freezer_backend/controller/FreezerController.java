package com.nirlvy.smart_freezer_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nirlvy.smart_freezer_backend.common.Result;
import com.nirlvy.smart_freezer_backend.common.ResultCode;
import com.nirlvy.smart_freezer_backend.entity.Freezer;
import com.nirlvy.smart_freezer_backend.exception.ServiceException;
import com.nirlvy.smart_freezer_backend.service.IFreezerService;

@RestController
@RequestMapping("/freezer")
public class FreezerController {

    @Autowired
    private IFreezerService freezerService;

    public static class Param {
        public Integer id;
        public String position;
        public String location;
    }

    @GetMapping("/home")
    public Result homeinfo(@RequestParam Integer id) {
        return freezerService.home(id);
    }

    @GetMapping("/list")
    public List<Freezer> list(@RequestParam Integer id) {
        return freezerService.list(new QueryWrapper<Freezer>().eq("userId", id));
    }

    @PostMapping("/upmarker")
    public Result upmarker(@RequestBody Param freezer) {
        return freezerService.upmarker(freezer);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Freezer freezer) {
        try {
            freezerService.updateById(freezer);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.STSTEM_ERROR, e);
        }
        return Result.success();
    }
}
