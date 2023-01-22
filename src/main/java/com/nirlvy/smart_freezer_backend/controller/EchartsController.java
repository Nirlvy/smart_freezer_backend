package com.nirlvy.smart_freezer_backend.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nirlvy.smart_freezer_backend.common.Result;
import com.nirlvy.smart_freezer_backend.entity.ShelvesLog;
import com.nirlvy.smart_freezer_backend.mapper.ShelvesLogMapper;
import cn.hutool.core.collection.CollUtil;
import jakarta.annotation.Resource;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Resource
    private ShelvesLogMapper shelvesLogMapper;

    @PostMapping("/months")
    public Result months(@RequestBody Integer[] freezerId) {
        Long[] shelves = new Long[12];
        Long[] sold = new Long[12];
        for (int i = 1; i <= 12; i++) {
            LocalDateTime start = LocalDate.of(LocalDate.now().getYear(), i, 1).atStartOfDay();
            LocalDateTime end = LocalDate.of(LocalDate.now().getYear(), i, 1).with(TemporalAdjusters.lastDayOfMonth())
                    .atTime(23, 59, 59);
            shelves[i - 1] = shelvesLogMapper
                    .selectCount(new QueryWrapper<ShelvesLog>().in("freezerId", (Object[]) freezerId)
                            .between("upTime", start, end));
            sold[i - 1] = shelvesLogMapper
                    .selectCount(new QueryWrapper<ShelvesLog>().in("freezerId", (Object[]) freezerId).eq("state", 0)
                            .between("upTime", start, end));
        }
        return Result.success(CollUtil.newArrayList(shelves, sold));
    }

    @PostMapping("/sold")
    public Result sold(@RequestBody Integer[] freezerId) {
        List<ShelvesLog> log = shelvesLogMapper
                .selectList(
                        new QueryWrapper<ShelvesLog>().select("distinct name").in("freezerId", (Object[]) freezerId));
        List<String> name = log.stream()
                .map(ShelvesLog::getName)
                .collect(Collectors.toList());
        List<Long> sold = new ArrayList<>();
        for (String n : name) {
            sold.add(shelvesLogMapper.selectCount(
                    new QueryWrapper<ShelvesLog>().in("freezerId", (Object[]) freezerId).eq("name", n).eq("state", 0)));
        }
        return Result.success(CollUtil.newArrayList(name, sold));
    }

}
