package com.hql.lightning.util;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * MyBatis工具类
 *
 * @author lee
 *         2015-2-6
 */
public class MyBatisUtil {

    private SqlSessionFactory sqlSessionFactory;

    /**
     * 数据库会话
     */
    private SqlSession sqlSession;

    private static MyBatisUtil instance = new MyBatisUtil();

    public static MyBatisUtil getInstance() {
        return instance;
    }

    /**
     * 取得会话
     *
     * @param autoCommit
     * @return
     */
    public SqlSession getSession(boolean autoCommit) {
        try {
            SqlSessionFactoryBuilder FactoryBuilder = new SqlSessionFactoryBuilder();
            sqlSessionFactory = FactoryBuilder.build(ProReaderUtil.getInstance().getMyBatisPro());
            sqlSession = sqlSessionFactory.openSession(autoCommit);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sqlSession;
    }

    /**
     * 关闭会话
     */
    public void closeSession() {
        if (sqlSession != null)
            sqlSession.close();
    }
}
