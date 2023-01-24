package com.nirlvy.smart_freezer_backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nirlvy.smart_freezer_backend.entity.ShelvesLog;
import com.nirlvy.smart_freezer_backend.service.IFreezerService;
import com.nirlvy.smart_freezer_backend.service.IShelvesLogService;

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

    @PostMapping("/page")
    public IPage<ShelvesLog> findPage(@RequestParam Integer id, @RequestBody(required = false) Integer[] freezerId,
            @RequestParam(defaultValue = "") String name, @RequestParam(required = false) Boolean state,
            @RequestParam(required = false) String upTime,
            @RequestParam(required = false) String downTime,
            @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        if (freezerId == null || freezerId.length == 0) {
            Map<String, Object> homeinfoResult = freezerService.homeinfo(id);
            freezerId = (Integer[]) homeinfoResult.get("freezerId");
        }
        return shelvesLogService.findPage(freezerId, name, state, upTime, downTime, pageNum, pageSize);

    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return shelvesLogService.removeById(id);
    }

    @DeleteMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return shelvesLogService.removeBatchByIds(ids);
    }
}
