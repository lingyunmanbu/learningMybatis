package com.yang.dao;

import com.yang.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    //查询全部用户
    List<User> getUserList();
    //模糊查询
    List<User> getUserLike(String name);
    //根据ID查询用户
    User getUserById(int id);

    User getUserById2(Map<String,Object> map);
    //insert 一个用户
    int addUser(User user);

    int addUser2(Map<String,Object> map);

    //修改用户
    int UpdateUser(User user);

    //删除用户
    int DeleteUser(int id);
}
