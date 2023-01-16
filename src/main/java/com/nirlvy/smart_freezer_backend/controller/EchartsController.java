package com.nirlvy.smart_freezer_backend.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nirlvy.smart_freezer_backend.common.Result;
import com.nirlvy.smart_freezer_backend.entity.ShelvesLog;
import com.nirlvy.smart_freezer_backend.entity.User;
import com.nirlvy.smart_freezer_backend.mapper.ShelvesLogMapper;
import com.nirlvy.smart_freezer_backend.service.IUserService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import jakarta.annotation.Resource;

@RestController
@RequestMapping("/echarts")
public class EchartsController {
    @Autowired
    private IUserService userService;

    @Resource
    private ShelvesLogMapper shelvesLogMapper;

    // @GetMapping("/example")
    // public Result get() {
    // Map<String, Object> map = new HashMap<>();
    // map.put("x", CollUtil.newArrayList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat",
    // "Sun"));
    // map.put("y", CollUtil.newArrayList(150, 230, 224, 218, 135, 147, 260));
    // return Result.success(map);
    // }

    @GetMapping("/members")
    public Result members() {
        List<User> list = userService.list();
        int q1 = 0;
        int q2 = 0;
        int q3 = 0;
        int q4 = 0;
        for (User user : list) {
            Date createTime = Date.from(user.getCreateTime().atZone(ZoneId.systemDefault()).toInstant());
            Quarter quater = DateUtil.quarterEnum(createTime);
            switch (quater) {
                case Q1:
                    q1 += 1;
                    break;
                case Q2:
                    q2 += 1;
                    break;
                case Q3:
                    q3 += 1;
                    break;
                case Q4:
                    q4 += 1;
                    break;
                default:
                    break;
            }
        }
        return Result.success(CollUtil.newArrayList(q1, q2, q3, q4));
    }

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

}
