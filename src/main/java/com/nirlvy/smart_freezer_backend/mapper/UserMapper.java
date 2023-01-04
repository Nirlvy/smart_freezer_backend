package com.nirlvy.smart_freezer_backend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.nirlvy.smart_freezer_backend.entity.User;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user")
    List<User> findAll();

    @Insert("INSERT into user (user_name,password) VALUES (#{user_name},#{password})")
    int insert(User user);

    int update(User user);

    @Delete("DELETE FROM user WHERE id = #{id}")
    Integer deleteById(@Param("id") Integer id);
}