package com.nirlvy.smart_freezer_backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private List<Freezer> freezerinfo(Integer id) {
        QueryWrapper<Freezer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", id);
        return freezerService.list(queryWrapper);
    }

    @GetMapping("/home")
    public Result homeinfo(@RequestParam Integer id) {
        List<Freezer> list = freezerinfo(id);
        Integer totalfreezer = list.size();
        Long runfreezer = list.stream().filter(item -> item.getEnable() == true).count();
        Long needfreezer = list.stream().filter(item -> item.getNeed() == true).count();
        Integer[] freezerId = list.stream().map(Freezer::getId).toArray(Integer[]::new);
        Map<String, Object> result = new HashMap<>();
        result.put("totalfreezer", totalfreezer);
        result.put("runfreezer", runfreezer);
        result.put("needfreezer", needfreezer);
        result.put("freezerId", freezerId);
        return Result.success(result);
    }

}
