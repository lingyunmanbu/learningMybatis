package com.yang.dao;

import com.yang.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

    User queryUserByid(@Param("id") int id);

    int updateUser(User user);

}
