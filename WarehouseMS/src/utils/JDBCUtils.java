package utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Druid 连接池的工具类
 */
public class JDBCUtils {
    //1、定义一个成员变量 DataSource
    private static DataSource ds;

    static {
        try {
            //2、加载配置文件
            Properties pro = new Properties();
            pro.load(JDBCUtils.class.getClassLoader().getResourceAsStream("config/druid.properties"));
            //3、获取DataSource
            ds = DruidDataSourceFactory.createDataSource(pro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 从连接池中获取连接对象
     * @return
     * @throws SQLException
     */

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    /**
     * 获取连接池对象
     * @return
     */

    public static DataSource getDataSource() {
        return ds;
    }


    /**
     * 归还连接对象
     * @param rs
     * @param stmt
     * @param conn
     */

    public static void close(ResultSet rs, Statement stmt, Connection conn) {
        if(rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                rs = null;
            }
        }
        if(stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                stmt = null;
            }
        }
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                conn = null;
            }
        }
    }
}
