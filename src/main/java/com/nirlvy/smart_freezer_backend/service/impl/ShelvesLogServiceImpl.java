package com.nirlvy.smart_freezer_backend.service.impl;

import com.nirlvy.smart_freezer_backend.common.Result;
import com.nirlvy.smart_freezer_backend.common.ResultCode;
import com.nirlvy.smart_freezer_backend.entity.ShelvesLog;
import com.nirlvy.smart_freezer_backend.exception.ServiceException;
import com.nirlvy.smart_freezer_backend.mapper.ShelvesLogMapper;
import com.nirlvy.smart_freezer_backend.service.IShelvesLogService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            throw new ServiceException(ResultCode.STSTEM_ERROR, e);
        }
        return Result.success();
    }

    @Override
    public void export(HttpServletResponse response, Integer[] freezerId) throws Exception {
        QueryWrapper<ShelvesLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("freezerId", (Object[]) freezerId);
        List<ShelvesLog> list = list();
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=data.xlsx");
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(out);
    }

    @Override
    public Result freezer(Integer id) {
        List<String> names = list(new QueryWrapper<ShelvesLog>().select("distinct name").eq("freezerId", id))
                .stream()
                .map(ShelvesLog::getName)
                .collect(Collectors.toList());
        Integer[][] shelves = new Integer[names.size()][12];
        for (int i = 0; i < shelves.length; i++) {
            for (int j = 0; j < shelves[i].length; j++) {
                shelves[i][j] = 0;
            }
        }
        Integer[][] sold = new Integer[names.size()][12];
        for (int i = 0; i < sold.length; i++) {
            for (int j = 0; j < sold[i].length; j++) {
                sold[i][j] = 0;
            }
        }
        try {
            List<ShelvesLog> allLogs = list(new QueryWrapper<ShelvesLog>().eq("freezerId", id));
            for (int i = 0; i < allLogs.size(); i++) {
                int month = allLogs.get(i).getUpTime().getMonthValue();
                int index = names.indexOf(allLogs.get(i).getName());
                shelves[index][month - 1]++;
                if (allLogs.get(i).getState() == false) {
                    sold[index][month - 1]++;
                }
            }
        } catch (Exception e) {
            throw new ServiceException(ResultCode.STSTEM_ERROR, e);
        }
        return Result.success(CollUtil.newArrayList(names, shelves, sold));
    }

    @Override
    public Result homeinfo(Integer[] freezerId) {
        Map<String, Object> result = new HashMap<>();
        Map<Integer, Long[]> monthsCharts = new HashMap<>();
        Long[] zeroArray = new Long[12];
        Arrays.fill(zeroArray, 0L);
        monthsCharts.put(0, zeroArray);
        monthsCharts.put(1, zeroArray.clone());
        try {
            list(new QueryWrapper<ShelvesLog>().in("freezerId", (Object[]) freezerId))
                    .stream().collect(Collectors.groupingBy(log -> log.getUpTime().getMonthValue()))
                    .forEach((month, logs) -> {
                        monthsCharts.get(0)[month - 1] = logs.stream()
                                .filter(log -> log.getFreezerId() != null)
                                .count();
                        monthsCharts.get(1)[month - 1] = logs.stream().filter(log -> log.getState() == false)
                                .count();
                    });
        } catch (Exception e) {
            throw new ServiceException(ResultCode.STSTEM_ERROR, e);
        }
        result.put("monthsCharts", monthsCharts);
        List<ShelvesLog> log = list(
                new QueryWrapper<ShelvesLog>().select("distinct name").in("freezerId",
                        (Object[]) freezerId));
        List<String> name = log.stream()
                .map(ShelvesLog::getName)
                .collect(Collectors.toList());
        List<Long> sold = new ArrayList<>();
        for (String n : name) {
            sold.add(count(
                    new QueryWrapper<ShelvesLog>().in("freezerId", (Object[]) freezerId)
                            .eq("name", n).eq("state", 0)));
        }
        result.put("soldCharts", CollUtil.newArrayList(name, sold));
        Long[] shelves = new Long[freezerId.length];
        try {
            for (int i = 0; i < freezerId.length; i++) {
                shelves[i] = count(new QueryWrapper<ShelvesLog>().eq("freezerId", freezerId[i]));
            }
        } catch (Exception e) {
            throw new ServiceException(ResultCode.STSTEM_ERROR, e);
        }
        result.put("shelves", shelves);
        return Result.success(result);
    }
}
