package com.nirlvy.smart_freezer_backend.service.impl;

import com.nirlvy.smart_freezer_backend.common.Constants;
import com.nirlvy.smart_freezer_backend.common.Result;
import com.nirlvy.smart_freezer_backend.entity.ShelvesLog;
import com.nirlvy.smart_freezer_backend.mapper.ShelvesLogMapper;
import com.nirlvy.smart_freezer_backend.service.IShelvesLogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
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
        List<ShelvesLog> list = Stream
                .generate(() -> new ShelvesLog(null, id, name, true, LocalDateTime.now(), null))
                .limit(num)
                .collect(Collectors.toList());
        if (saveBatch(list)) {
            return Result.success();
        } else {
            return Result.error(Constants.CODE_500, "上架失败,请稍后再试");
        }

    }

}
