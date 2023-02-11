package com.nirlvy.smart_freezer_backend.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nirlvy.smart_freezer_backend.common.Constants;
import com.nirlvy.smart_freezer_backend.common.Result;
import com.nirlvy.smart_freezer_backend.controller.FreezerController.Param;
import com.nirlvy.smart_freezer_backend.entity.Freezer;
import com.nirlvy.smart_freezer_backend.mapper.FreezerMapper;
import com.nirlvy.smart_freezer_backend.service.IFreezerService;

@Service
public class FreezerServiceImpl extends ServiceImpl<FreezerMapper, Freezer> implements IFreezerService {

    @Override
    public Map<String, Object> homeinfo(Integer id) {
        QueryWrapper<Freezer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", id);
        List<Freezer> list = list(queryWrapper);
        Integer totalfreezer = list.size();
        Long runfreezer = list.stream().filter(item -> item.getEnable() == true).count();
        Long needfreezer = list.stream().filter(item -> item.getNeed() == true).count();
        Integer[] freezerId = list.stream().map(Freezer::getId).toArray(Integer[]::new);
        Map<String, Object> result = new HashMap<>();
        result.put("totalfreezer", totalfreezer);
        result.put("runfreezer", runfreezer);
        result.put("needfreezer", needfreezer);
        result.put("freezerId", freezerId);
        return result;
    }

    @Override
    public Result upmarker(Param freezerinfo) {
        Freezer freezer = new Freezer();
        freezer.setId(freezerinfo.id);
        freezer.setPosition(freezerinfo.position);
        freezer.setLocation(freezerinfo.location);
        try {
            updateById(freezer);
        } catch (Exception e) {
            return Result.error(Constants.CODE_500, e.toString());
        }
        return Result.success();
    }

}
