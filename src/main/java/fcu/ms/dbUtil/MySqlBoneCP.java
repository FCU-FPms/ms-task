package fcu.ms.dbUtil;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class MySqlBoneCP {
    private static MySqlBoneCP instance = new MySqlBoneCP();
    private static BoneCP connectionPool = null;

    private MySqlBoneCP() {
        try {
            Class.forName("com.mysql.jdbc.Driver"); // load the DB driver
            BoneCPConfig config = getBoneCPConfig();
            if(connectionPool == null) {
                connectionPool = new BoneCP(config);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MySqlBoneCP getInstance() {
        return instance;
    }

    public Connection getConnection() {

        Connection connection = null;

        if(connectionPool == null) {
            BoneCPConfig config = getBoneCPConfig();
            try {
                connectionPool = new BoneCP(config);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                throw new NullPointerException();
            }
        }


        try {
            connection = connectionPool.getConnection();
        } catch (SQLException throwables) {
            connectionPool = null; // 如果沒拿到連線, 就直接重啟poll
            throwables.printStackTrace();
            throw new NullPointerException();
        }

        return connection;
    }

    private BoneCPConfig getBoneCPConfig() {
        BoneCPConfig config = new BoneCPConfig();

        Properties dbUrl = get_db_properties("/jdbc-url.properties");

        Properties dbSecret = get_db_properties("/jdbc.properties");

        config.setJdbcUrl(dbUrl.getProperty("db.url") + "?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=utf-8");
        config.setUsername(dbSecret.getProperty("db.user"));
        config.setPassword(dbSecret.getProperty("db.password"));

        config.setPartitionCount(2);
        config.setMinConnectionsPerPartition(1);
        config.setMaxConnectionsPerPartition(4);

        return config;

    }



    private static Properties get_db_properties(String file_path) {
        Properties props = new Properties();
        try {
            InputStream in = MySqlBoneCP.class.getResourceAsStream(file_path);
            props.load(in);
            return props;
        } catch (Exception ex) {
            System.out.println("Error: "+ex);
        }

        return props;
    }

}
