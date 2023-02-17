package com.nirlvy.smart_freezer_backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nirlvy.smart_freezer_backend.common.Result;
import com.nirlvy.smart_freezer_backend.entity.ShelvesLog;
import com.nirlvy.smart_freezer_backend.service.IFreezerService;
import com.nirlvy.smart_freezer_backend.service.IShelvesLogService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author nirlvy
 * @since 2023-01-24
 */
@RestController
@RequestMapping("/shelvesLog")
public class ShelvesLogController {

    @Autowired
    private IShelvesLogService shelvesLogService;

    @Autowired
    private IFreezerService freezerService;

    public static class Param {
        public Integer[] freezerId;
        public String[] name;
    }

    @PostMapping("/page")
    public IPage<ShelvesLog> findPage(
            @RequestBody Param datas, @RequestParam(required = false) Boolean state,
            @RequestParam(required = false) String upTime,
            @RequestParam(required = false) String downTime,
            @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        return shelvesLogService.findPage(datas.freezerId, datas.name, state, upTime, downTime, pageNum, pageSize);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return shelvesLogService.removeById(id);
    }

    @DeleteMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return shelvesLogService.removeBatchByIds(ids);
    }

    @PostMapping("/update")
    public boolean update(@RequestBody ShelvesLog shelvesLog) {
        return shelvesLogService.updateById(shelvesLog);
    }

    @PostMapping("/up")
    public Result up(@RequestParam Integer id, @RequestParam String name, @RequestParam Integer num) {
        return shelvesLogService.up(id, name, num);
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response, @RequestParam Integer id) throws Exception {
        Map<String, Object> homeinfoResult = (Map<String, Object>) freezerService.homeinfo(id);
        Integer[] freezerId = (Integer[]) homeinfoResult.get("freezerId");
        shelvesLogService.export(response,freezerId);
    }

    @GetMapping("/freezer")
    public Result freezer(@RequestParam Integer id) {
        return shelvesLogService.freezer(id);
    }

    @PostMapping("/homeinfo")
    public Result homeinfo(@RequestBody Integer[] freezerId) {
        return shelvesLogService.homeinfo(freezerId);
    }
}
