package com.hql.lightning.util;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;


/**
 * C3P0连接池工具类
 *
 * @author lee
 *         2015-2-5
 */
public class C3P0Util extends UnpooledDataSourceFactory {

    public C3P0Util() {
        this.dataSource = new ComboPooledDataSource();
    }
}
