import com.hql.lightning.core.ServerInit;
import com.hql.lightning.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

import com.hql.test.dao.Users;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 数据库测试
 */
public class MyBatisTest {

    private static final Logger logger = Logger.getLogger(MyBatisTest.class);

    public static void main(String[] args) throws Exception {
        ServerInit.getInstance().initConfPath("lightning");
        ServerInit.getInstance().initLog4j();
        SqlSession session = MyBatisUtil.getInstance().getSession(true);

        try {
            //查询单条记录
            /*Users user = (Users) session.selectOne("com.hql.test.dao.selectUserByID", 1);
            logger.info(user.getId() + "");
            logger.info(user.getName());*/

            //插入记录并返回自增id
            /*Users user = new Users();
            user.setId(0);
            user.setName("leehui1983");

            session.insert("com.hql.test.dao.insertAndGetId", user);
            logger.info("insertId:"+user.getId());*/

            //查询多条记录
            /*List<Users> users = session.selectList("com.hql.test.dao.selectUsers", "%leehui%");
            for (Users u : users) {
                logger.info(u.getId()+":"+u.getName());
            }*/

            //更新记录
            /*Users user = new Users();
            user.setId(5);
            user.setName("辉爷");
            int updateNum = session.update("com.hql.test.dao.updateUser", user);
            logger.info("updateNum:"+updateNum);*/

            //删除记录
            int delNum = session.update("com.hql.test.dao.deleteUser", 5);
            logger.info("delNum:" + delNum);
        } finally {
            MyBatisUtil.getInstance().closeSession();
        }
    }
}

