package com.yang.dao;

import com.yang.pojo.Teacher;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface TeacherMapper {
    @Select("Select * from teacher where id = #{tid}")
    Teacher getTeacher(@Param("tid") int id);
}
