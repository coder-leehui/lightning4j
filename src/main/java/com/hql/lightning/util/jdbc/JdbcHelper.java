package com.hql.lightning.util.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * jdbc助手类
 */
public class JdbcHelper {

    private static Connection conn = null;

    private static PreparedStatement preparedStatement = null;

    private static CallableStatement callableStatement = null;

    /**
     * 用于查询，返回结果集
     *
     * @param sql
     *            sql语句
     * @return 结果集
     * @throws java.sql.SQLException
     */
    @SuppressWarnings("rawtypes")
    public static List query(String sql) throws SQLException {

        ResultSet rs = null;
        try {
            getPreparedStatement(sql);
            rs = preparedStatement.executeQuery();

            return ResultToListMap(rs);
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free(rs);
        }

    }

    /**
     * 用于带参数的查询，返回结果集
     *
     * @param sql
     *            sql语句
     * @param paramters
     *            参数集合
     * @return 结果集
     * @throws java.sql.SQLException
     */
    @SuppressWarnings("rawtypes")
    public static List query(String sql, Object... paramters)
            throws SQLException {

        ResultSet rs = null;
        try {
            getPreparedStatement(sql);

            for (int i = 0; i < paramters.length; i++) {
                preparedStatement.setObject(i + 1, paramters[i]);
            }
            rs = preparedStatement.executeQuery();
            return ResultToListMap(rs);
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free(rs);
        }
    }

    /**
     * 返回单个结果的值，如count\min\max等等
     *
     * @param sql
     *            sql语句
     * @return 结果集
     * @throws java.sql.SQLException
     */
    public static Object getSingle(String sql) throws SQLException {
        Object result = null;
        ResultSet rs = null;
        try {
            getPreparedStatement(sql);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                result = rs.getObject(1);
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free(rs);
        }

    }

    /**
     * 返回单个结果值，如count\min\max等
     *
     * @param sql
     *            sql语句
     * @param paramters
     *            参数列表
     * @return 结果
     * @throws java.sql.SQLException
     */
    public static Object getSingle(String sql, Object... paramters)
            throws SQLException {
        Object result = null;
        ResultSet rs = null;
        try {
            getPreparedStatement(sql);

            for (int i = 0; i < paramters.length; i++) {
                preparedStatement.setObject(i + 1, paramters[i]);
            }
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                result = rs.getObject(1);
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free(rs);
        }
    }

    /**
     * 用于增删改
     *
     * @param sql
     *            sql语句
     * @return 影响行数
     * @throws java.sql.SQLException
     */
    public static int update(String sql) throws SQLException {

        try {
            getPreparedStatement(sql);

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free();
        }
    }

    /**
     * 用于增删改（带参数）
     *
     * @param sql
     *            sql语句
     * @param paramters
     *            sql语句
     * @return 影响行数
     * @throws java.sql.SQLException
     */
    public static int update(String sql, Object... paramters)
            throws SQLException {
        try {
            getPreparedStatement(sql);

            for (int i = 0; i < paramters.length; i++) {
                preparedStatement.setObject(i + 1, paramters[i]);
            }
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free();
        }
    }

    /**
     * 插入值后返回主键值
     *
     * @param sql
     *            插入sql语句
     * @return 返回结果
     * @throws Exception
     */
    public static Object insertWithReturnPrimeKey(String sql)
            throws SQLException {
        ResultSet rs = null;
        Object result = null;
        try {
            conn = JdbcUnits.getConnection();
            preparedStatement = conn.prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.execute();
            rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                result = rs.getObject(1);
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    /**
     * 插入值后返回主键值
     *
     * @param sql
     *            插入sql语句
     * @param paramters
     *            参数列表
     * @return 返回结果
     * @throws java.sql.SQLException
     */
    public static Object insertWithReturnPrimeKey(String sql,
                                                  Object... paramters) throws SQLException {
        ResultSet rs = null;
        Object result = null;
        try {
            conn = JdbcUnits.getConnection();
            preparedStatement = conn.prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < paramters.length; i++) {
                preparedStatement.setObject(i + 1, paramters[i]);
            }
            preparedStatement.execute();
            rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                result = rs.getObject(1);
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException(e);
        }

    }

    /**
     * 调用存储过程执行查询
     *
     * @param procedureSql
     *            存储过程
     * @return
     * @throws java.sql.SQLException
     */
    @SuppressWarnings("rawtypes")
    public static List callableQuery(String procedureSql) throws SQLException {
        ResultSet rs = null;
        try {
            getCallableStatement(procedureSql);
            rs = callableStatement.executeQuery();
            return ResultToListMap(rs);
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free(rs);
        }
    }

    /**
     * 调用存储过程（带参数）,执行查询
     *
     * @param procedureSql
     *            存储过程
     * @param paramters
     *            参数表
     * @return
     * @throws java.sql.SQLException
     */
    @SuppressWarnings("rawtypes")
    public static List callableQuery(String procedureSql, Object... paramters)
            throws SQLException {
        ResultSet rs = null;
        try {
            getCallableStatement(procedureSql);

            for (int i = 0; i < paramters.length; i++) {
                callableStatement.setObject(i + 1, paramters[i]);
            }
            rs = callableStatement.executeQuery();
            return ResultToListMap(rs);
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free(rs);
        }
    }

    /**
     * 调用存储过程，查询单个值
     *
     * @param procedureSql
     * @return
     * @throws java.sql.SQLException
     */
    public static Object callableGetSingle(String procedureSql)
            throws SQLException {
        Object result = null;
        ResultSet rs = null;
        try {
            getCallableStatement(procedureSql);
            rs = callableStatement.executeQuery();
            while (rs.next()) {
                result = rs.getObject(1);
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free(rs);
        }
    }

    /**
     * 调用存储过程(带参数)，查询单个值
     *
     * @param procedureSql
     * @param paramters
     * @return
     * @throws java.sql.SQLException
     */
    public static Object callableGetSingle(String procedureSql,
                                           Object... paramters) throws SQLException {
        Object result = null;
        ResultSet rs = null;
        try {
            getCallableStatement(procedureSql);

            for (int i = 0; i < paramters.length; i++) {
                callableStatement.setObject(i + 1, paramters[i]);
            }
            rs = callableStatement.executeQuery();
            while (rs.next()) {
                result = rs.getObject(1);
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free(rs);
        }
    }

    /**
     * 调用存储过程
     *
     * @param procedureSql
     * @return
     * @throws java.sql.SQLException
     */
    public static Object callableWithParamters(String procedureSql)
            throws SQLException {
        try {
            getCallableStatement(procedureSql);
            callableStatement.registerOutParameter(0, Types.OTHER);
            callableStatement.execute();
            return callableStatement.getObject(0);

        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free();
        }

    }

    /**
     * 调用存储过程，执行增删改
     *
     * @param procedureSql
     *            存储过程
     * @return 影响行数
     * @throws java.sql.SQLException
     */
    public static int callableUpdate(String procedureSql) throws SQLException {
        try {
            getCallableStatement(procedureSql);
            return callableStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free();
        }
    }

    /**
     * 调用存储过程（带参数），执行增删改
     *
     * @param procedureSql
     *            存储过程
     * @param parameters
     * @return 影响行数
     * @throws java.sql.SQLException
     */
    public static int callableUpdate(String procedureSql, Object... parameters)
            throws SQLException {
        try {
            getCallableStatement(procedureSql);
            for (int i = 0; i < parameters.length; i++) {
                callableStatement.setObject(i + 1, parameters[i]);
            }
            return callableStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free();
        }
    }

    /**
     * 批量更新数据
     *
     * @param sqlList
     *            一组sql
     * @return
     */
    public static int[] batchUpdate(List<String> sqlList) {

        int[] result = new int[] {};
        Statement statenent = null;
        try {
            conn = JdbcUnits.getConnection();
            conn.setAutoCommit(false);
            statenent = conn.createStatement();
            for (String sql : sqlList) {
                statenent.addBatch(sql);
            }
            result = statenent.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                throw new ExceptionInInitializerError(e1);
            }
            throw new ExceptionInInitializerError(e);
        } finally {
            free(statenent, null);
        }
        return result;
    }

    /**
     * 结果集映射为hash
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static List ResultToListMap(ResultSet rs) throws SQLException {
        List list = new ArrayList();
        while (rs.next()) {
            ResultSetMetaData md = rs.getMetaData();
            Map map = new HashMap();
            for (int i = 1; i <= md.getColumnCount(); i++) {
                map.put(md.getColumnLabel(i), rs.getObject(i));
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 获取PreparedStatement
     *
     * @param sql
     * @throws java.sql.SQLException
     */
    private static void getPreparedStatement(String sql) throws SQLException {
        conn = JdbcUnits.getConnection();
        preparedStatement = conn.prepareStatement(sql);
    }

    /**
     * 获取CallableStatement
     *
     * @param procedureSql
     * @throws java.sql.SQLException
     */
    private static void getCallableStatement(String procedureSql)
            throws SQLException {
        conn = JdbcUnits.getConnection();
        callableStatement = conn.prepareCall(procedureSql);
    }

    /**
     * 释放资源
     *
     * @param rs
     *            结果集
     */
    public static void free(ResultSet rs) {

        JdbcUnits.free(conn, preparedStatement, rs);
    }

    /**
     * 释放资源
     *
     * @param statement
     * @param rs
     */
    public static void free(Statement statement, ResultSet rs) {
        JdbcUnits.free(conn, statement, rs);
    }

    /**
     * 释放资源
     */
    public static void free() {
        free(null);
    }
}
