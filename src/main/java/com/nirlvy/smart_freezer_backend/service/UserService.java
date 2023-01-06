package com.nirlvy.smart_freezer_backend.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nirlvy.smart_freezer_backend.entity.User;
import com.nirlvy.smart_freezer_backend.mapper.UserMapper;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    public boolean saveUser(User user) {
        return saveOrUpdate(user);
    }

    // @Autowired
    // private UserMapper userMapper;

    // public Integer save(User user) {
    // if (user.getId() == null)
    // return userMapper.insert(user);
    // else
    // return userMapper.update(user);
    // }

    // public Map<String, Object> findPage(Integer id, String userName, String
    // create_time, Integer pageNum,
    // Integer pageSize) {
    // pageNum = (pageNum - 1) * pageSize;
    // userName = "%" + userName + "%";
    // if (create_time == null)
    // create_time = "%";
    // Integer total = userMapper.selectTotal(id, userName, create_time);
    // List<User> data = userMapper.selectPage(id, userName, create_time, pageNum,
    // pageSize);
    // Map<String, Object> res = new HashMap<>();
    // res.put("data", data);
    // res.put("total", total);
    // return res;
    // }

}
