package com.yang.dao;

import com.yang.pojo.User;
import com.yang.utils.MybatisUtil;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

public class UserMapperTest {
     static Logger logger = Logger.getLogger(UserMapperTest.class);

    @Test
    public void test(){
        //第一步，获得SqlSession对象
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        //方式一：getMapper
        com.yang.dao.UserMapper mapper = sqlSession.getMapper(com.yang.dao.UserMapper.class);
        List<User> userList = mapper.getUserList();

        //方式二
//        List<User> userList = sqlSession.selectList("com.yang.dao.UserDao.getUserList");

        for (User user : userList) {
            System.out.println(user);
        }
        //关闭SqlSession
        sqlSession.close();
    }

    @Test
    public void getUserById(){
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        com.yang.dao.UserMapper mapper = sqlSession.getMapper(com.yang.dao.UserMapper.class);
        User user = mapper.getUserById(1);
        System.out.println(user);
        sqlSession.close();
    }
    @Test
    public void getUserByLimit(){
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        HashMap<String, Integer> map = new HashMap<>();
        map.put("startIndex",2);
        map.put("pageSize",2);
        List<User> userByLimit = mapper.getUserByLimit(map);
        for (User user : userByLimit) {
            System.out.println(user);
        }
        sqlSession.close();
    }
    @Test
    public void getUserByRowBounds(){
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        //RowBounds实现
        RowBounds rowBounds = new RowBounds(1, 2);
        List<User> userList = sqlSession.selectList("com.yang.dao.UserMapper.getUserByRowBounds",null,rowBounds);
        for (User user : userList) {
            System.out.println(user);
        }
        sqlSession.close();
    }

    @Test
    public void log4jtest(){
        logger.info("info:进入了log4jtest");
        logger.debug("debug:进入了log4jtest");
        logger.error("error:进入了log4jtest");
        logger.fatal("fatal:进入了log4jtest");
        logger.trace("trace:进入了log4jtest");
        logger.warn("warn:进入了log4jtest");
    }
//    增删改需要提交事务
    @Test
    public void addUser(){
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        com.yang.dao.UserMapper mapper = sqlSession.getMapper(com.yang.dao.UserMapper.class);
        int i = mapper.addUser(new User(4, "小光", "123123"));
        if(i>0) System.out.println("插入成功");
        // 提交事务
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void UpdateUser(){
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        com.yang.dao.UserMapper mapper = sqlSession.getMapper(com.yang.dao.UserMapper.class);
        int i = mapper.UpdateUser(new User(4, "明明", "123321"));
        if(i>0) System.out.println("修改成功");
        //提交事务
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void DeleteUser(){
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        com.yang.dao.UserMapper mapper = sqlSession.getMapper(com.yang.dao.UserMapper.class);
        int i = mapper.DeleteUser(4);
        if(i>0) {
            System.out.println("删除成功");
        }
        sqlSession.commit();
        sqlSession.close();

    }
}
