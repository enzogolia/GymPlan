package model;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.TimeZone;

public class ConPool {
    private static DataSource ds;

    public static Connection getConnection() throws SQLException {
        if (ds == null) {
            PoolProperties p = new PoolProperties();
            p.setUrl("jdbc:mysql://127.0.0.1:3306/dbprogetto_is?serverTimezone=" + TimeZone.getDefault().getID());
            p.setDriverClassName("com.mysql.cj.jdbc.Driver");
            p.setUsername("root");
            p.setPassword("root");
            p.setMaxActive(100);
            p.setInitialSize(10);
            p.setMinIdle(10);
            p.setRemoveAbandonedTimeout(60);
            p.setRemoveAbandoned(true);
            ds = new DataSource();
            ds.setPoolProperties(p);
        }
        return ds.getConnection();
    }
}
