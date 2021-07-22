import com.yang.dao.TeacherMapper;
import com.yang.pojo.Teacher;
import com.yang.utils.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.validator.PublicClassValidator;

import java.util.List;

public class Mytest {
    @Test
    public void test(){
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        TeacherMapper teacherMapper = sqlSession.getMapper(TeacherMapper.class);
        List<Teacher> teacherList = teacherMapper.getTeacher();
        for (Teacher teacher : teacherList) {
            System.out.println(teacher);
        }
        sqlSession.close();
    }
    @Test
    public void getTeacher1(){
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        TeacherMapper teacherMapper = sqlSession.getMapper(TeacherMapper.class);
        Teacher teacherList = teacherMapper.getTeacher1(1);
            System.out.println(teacherList);
            //Teacher(id=1,
        // name=秦老师,
        // students=[Student(id=1, name=小明, tid=0),
        // Student(id=2, name=小红, tid=0),
        // Student(id=3, name=小张, tid=0),
        // Student(id=4, name=小李, tid=0),
        // Student(id=5, name=小王, tid=0)])
        sqlSession.close();
    }

    @Test
    public void getTeacher2(){
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        TeacherMapper teacherMapper = sqlSession.getMapper(TeacherMapper.class);
        Teacher teacher = teacherMapper.getTeacher2(1);
        System.out.println(teacher);
        sqlSession.close();
    }
}
