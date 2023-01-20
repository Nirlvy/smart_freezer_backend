package com.nirlvy.smart_freezer_backend.mapper;

import com.nirlvy.smart_freezer_backend.entity.RoleMenu;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author nirlvy
 * @since 2023-01-19
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    @Select("SELECT menuId FROM roleMenu WHERE role = #{role}")
    List<Integer> selectByRole(@Param("role") String role);

}
