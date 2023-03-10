package com.nirlvy.smart_freezer_backend.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nirlvy.smart_freezer_backend.common.Result;
import com.nirlvy.smart_freezer_backend.common.ResultCode;
import com.nirlvy.smart_freezer_backend.entity.Freezer;
import com.nirlvy.smart_freezer_backend.exception.ServiceException;
import com.nirlvy.smart_freezer_backend.mapper.FreezerMapper;
import com.nirlvy.smart_freezer_backend.service.IFreezerService;

@Service
public class FreezerServiceImpl extends ServiceImpl<FreezerMapper, Freezer> implements IFreezerService {

    @Override
    public Map<String, Object> homeinfo(Integer id) {
        List<Freezer> list = list(new QueryWrapper<Freezer>().eq("userId", id));
        Integer totalfreezer = list.size();
        Long runfreezer = list.stream().filter(item -> item.getDisabled() == false).count();
        Long needfreezer = list.stream().filter(item -> item.getNeed() == true).count();
        Integer[] freezerId = list.stream().map(Freezer::getId).toArray(Integer[]::new);
        Boolean[] disabled = list.stream().map(Freezer::getDisabled).toArray(Boolean[]::new);
        Map<String, Object> result = new HashMap<>();
        result.put("totalfreezer", totalfreezer);
        result.put("runfreezer", runfreezer);
        result.put("needfreezer", needfreezer);
        result.put("freezerId", freezerId);
        result.put("disabled", disabled);
        return result;
    }

    @Override
    public Result home(Integer id) {
        return Result.success(homeinfo(id));
    }

    @Override
    public Result capacity(Integer id, Integer capacity) {
        Freezer freezer = new Freezer();
        freezer.setUserId(id);
        freezer.setCapacity(capacity);
        try {
            save(freezer);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.STSTEM_ERROR, e);
        }
        return Result.success();
    }

}
