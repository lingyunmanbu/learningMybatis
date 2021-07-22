import com.yang.dao.UserMapper;
import com.yang.pojo.User;
import com.yang.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

public class Mytesy {

    @Test
    public void test(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        SqlSession sqlSession2 = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        UserMapper mapper2 = sqlSession2.getMapper(UserMapper.class);
        User user1 = mapper.queryUserByid(1);
        System.out.println(user1);
        sqlSession.close();
        //System.out.println("================================");
        //sqlSession.clearCache(); //手动清理缓存
        //System.out.println("==============================");
        User user2 = mapper2.queryUserByid(1);
        System.out.println(user2);
        System.out.println(user1==user2);
        sqlSession2.close();
    }






}
