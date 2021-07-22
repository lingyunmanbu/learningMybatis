package com.yang.dao;

import com.yang.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    //查询全部用户
    List<User> getUserList();

    //根据ID查询用户
    User getUserById(int id);
    //分页
    List<User> getUserByLimit(Map<String,Integer> map);
    List<User> getUserByRowBounds();
    //insert 一个用户
    int addUser(User user);

    //修改用户
    int UpdateUser(User user);

    //删除用户
    int DeleteUser(int id);
}


