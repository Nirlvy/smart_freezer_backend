package com.nirlvy.smart_freezer_backend.service.impl;

import com.nirlvy.smart_freezer_backend.common.Result;
import com.nirlvy.smart_freezer_backend.common.ResultCode;
import com.nirlvy.smart_freezer_backend.entity.Ulogin;
import com.nirlvy.smart_freezer_backend.entity.User;
import com.nirlvy.smart_freezer_backend.exception.ServiceException;
import com.nirlvy.smart_freezer_backend.mapper.RoleMenuMapper;
import com.nirlvy.smart_freezer_backend.mapper.UserMapper;
import com.nirlvy.smart_freezer_backend.service.IUserService;
import com.nirlvy.smart_freezer_backend.utils.TokenUtils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author nirlvy
 * @since 2023-01-07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private RoleMenuMapper roleMenuMapper;

    private User getuserinfo(Ulogin ulogin) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userName", ulogin.getUserName());
        queryWrapper.eq("password", ulogin.getPassword());
        User one;
        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.STSTEM_ERROR, e);
        }
        return one;
    }

    @Override
    public Result login(Ulogin ulogin) {
        String userName = ulogin.getUserName();
        String password = ulogin.getPassword();
        if (StrUtil.isBlank(userName) || StrUtil.isBlank(password)) {
            throw new ServiceException(ResultCode.USERNAME_OR_PASSWORD_IS_BLANK, null);
        }
        User one = getuserinfo(ulogin);
        if (one != null) {
            BeanUtil.copyProperties(one, ulogin, true);
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword().toString());
            ulogin.setToken(token);
            String role = one.getRole();
            List<Integer> menu = roleMenuMapper.selectByRole(role);
            ulogin.setMenus(menu);
            return Result.success(ulogin);
        } else {
            throw new ServiceException(ResultCode.USERNAME_OR_PASSWORD_INCORRECT, null);
        }
    }

    @Override
    public Result register(Ulogin ulogin) {
        String userName = ulogin.getUserName();
        String password = ulogin.getPassword();
        if (StrUtil.isBlank(userName) || StrUtil.isBlank(password)) {
            throw new ServiceException(ResultCode.USERNAME_OR_PASSWORD_IS_BLANK, null);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userName", ulogin.getUserName());
        User one;
        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.STSTEM_ERROR, e);
        }
        if (one == null) {
            one = new User();
            BeanUtil.copyProperties(ulogin, one, true);
            save(one);
            User userinfo = getuserinfo(ulogin);
            String token = TokenUtils.genToken(userinfo.getId().toString(), one.getPassword().toString());
            ulogin.setToken(token);
            List<Integer> menu = roleMenuMapper.selectByRole(userinfo.getRole().toString());
            ulogin.setMenus(menu);
        } else {
            throw new ServiceException(ResultCode.USERNAME_ALREADY_EXISTS, null);
        }
        return Result.success(ulogin);
    }

    @Override
    public boolean export(HttpServletResponse response) throws Exception {
        List<User> list = list();
        ExcelWriter writer = ExcelUtil.getWriter(true);

        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;chatset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
        return true;
    }

    @Override
    public boolean imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<User> list = reader.readAll(User.class);
        saveOrUpdateBatch(list);
        return true;
    }

    @Override
    public IPage<User> findPage(Integer id, String userName, String createTime, Integer shelves, Integer sold,
            Integer pageNum, Integer pageSize) {
        IPage<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (id != null)
            queryWrapper.eq("id", id);
        queryWrapper.like("userName", userName);
        if (!"null".equals(createTime))
            queryWrapper.like("createTime", createTime);
        if (shelves != null)
            queryWrapper.between("shelves", shelves - 100, shelves + 100);
        if (sold != null)
            queryWrapper.between("sold", sold - 100, sold + 100);
        return page(page, queryWrapper);
    }

    @Override
    public boolean sOu(User user) {
        if (user.getPassword() == "") {
            user.setPassword(null);
        }
        return saveOrUpdate(user);
    }

}
