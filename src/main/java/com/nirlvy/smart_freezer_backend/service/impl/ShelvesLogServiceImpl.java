package com.nirlvy.smart_freezer_backend.service.impl;

import com.nirlvy.smart_freezer_backend.common.Constants;
import com.nirlvy.smart_freezer_backend.common.Result;
import com.nirlvy.smart_freezer_backend.entity.ShelvesLog;
import com.nirlvy.smart_freezer_backend.mapper.ShelvesLogMapper;
import com.nirlvy.smart_freezer_backend.service.IShelvesLogService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author nirlvy
 * @since 2023-01-24
 */
@Service
public class ShelvesLogServiceImpl extends ServiceImpl<ShelvesLogMapper, ShelvesLog> implements IShelvesLogService {

    @Override
    public IPage<ShelvesLog> findPage(Integer[] freezerId, String[] name, Boolean state, String upTime,
            String downTime, Integer pageNum, Integer pageSize) {
        IPage<ShelvesLog> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ShelvesLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("freezerId", (Object[]) freezerId);
        if (name.length != 0)
            queryWrapper.in("name", (Object[]) name);
        if (state != null)
            queryWrapper.eq("state", state);
        if (!"null".equals(upTime))
            queryWrapper.like("upTime", upTime);
        if (downTime != "" && !"null".equals(downTime))
            queryWrapper.like("downTime", downTime);
        return page(page, queryWrapper);
    }

    @Override
    public boolean sold(ShelvesLog shelvesLog) {
        return saveOrUpdate(shelvesLog);
    }

    @Override
    public Result up(Integer id, String name, Integer num) {
        List<ShelvesLog> list = Stream.generate(() -> {
            ShelvesLog shelvesLog = new ShelvesLog();
            shelvesLog.setFreezerId(id);
            shelvesLog.setName(name);
            shelvesLog.setState(true);
            shelvesLog.setUpTime(LocalDateTime.now());
            return shelvesLog;
        }).limit(num).collect(Collectors.toList());
        try {
            saveBatch(list);
        } catch (Exception e) {
            return Result.error(Constants.CODE_500, "上架失败,请稍后再试:" + e);
        }
        return Result.success();
    }

    @Override
    public byte[] export(Integer[] freezerId) throws Exception {
        QueryWrapper<ShelvesLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("freezerId", (Object[]) freezerId);
        List<ShelvesLog> list = list();
        // 通过工具类创建writer，默认创建xls格式
        // ExcelWriter writer = ExcelUtil.getWriter();
        // 创建xlsx格式的
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(list, true);
        // out为OutputStream，需要写出到的目标流
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        writer.flush(stream);
        // 关闭writer，释放内存
        writer.close();
        return stream.toByteArray();
    }

    @Override
    public Result freezer(Integer id) {
        List<String> name = list(new QueryWrapper<ShelvesLog>().select("distinct name").eq("freezerId", id)).stream()
                .map(ShelvesLog::getName)
                .collect(Collectors.toList());
        Long[][] shelves = new Long[name.size()][12];
        Long[][] sold = new Long[name.size()][12];
        for (int i = 1; i <= 12; i++) {
            for (int j = 0; j < name.size(); j++) {
                LocalDateTime start = LocalDate.of(LocalDate.now().getYear(), i, 1).atStartOfDay();
                LocalDateTime end = LocalDate.of(LocalDate.now().getYear(), i, 1)
                        .with(TemporalAdjusters.lastDayOfMonth())
                        .atTime(23, 59, 59);
                shelves[j][i - 1] = count(new QueryWrapper<ShelvesLog>().eq("freezerId", id).eq("name", name.get(j))
                        .between("upTime", start, end));
                sold[j][i - 1] = count(
                        new QueryWrapper<ShelvesLog>().eq("freezerId", id).eq("name", name.get(j)).eq("state", 0)
                                .between("downTime", start, end));
            }
        }
        return Result.success(CollUtil.newArrayList(name, shelves, sold));
    }

    @Override
    public Result data(Integer id) {
        
        return null;
    }

}
