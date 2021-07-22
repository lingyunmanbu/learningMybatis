package com.yang.dao;

import com.yang.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {
    //查询全部用户
    @Select("select * from user")
    List<User> getUserList();

    //根据ID查询用户
    @Select("select * from user where id = #{id} and name = #{name}")
    User getUserById(@Param("id") int id,@Param("name") String name);

    //insert 一个用户
    int addUser(User user);

    //修改用户
    int UpdateUser(User user);

    //删除用户
    int DeleteUser(int id);
}


