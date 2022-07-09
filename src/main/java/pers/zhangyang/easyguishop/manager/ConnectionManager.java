package pers.zhangyang.easyguishop.manager;

import pers.zhangyang.easyguishop.yaml.SettingYaml;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    public static final ConnectionManager INSTANCE = new ConnectionManager();
    private final ThreadLocal<Connection> t = new ThreadLocal<>();

    private ConnectionManager() {
    }

    /**
     * return 返回当前线程的Connection对象
     *
     * @throws SQLException 数据库异常
     */
    public Connection getConnection() throws SQLException {
        Connection connection = t.get();
        if (connection == null || connection.isClosed()) {
            SettingYaml settingYamlManager = SettingYaml.INSTANCE;
            connection = DriverManager.getConnection(settingYamlManager.getStringDefault("setting.database.url"),
                    settingYamlManager.getStringDefault("setting.database.username"),
                    settingYamlManager.getStringDefault("setting.database.password"));
            connection.setAutoCommit(false);
            t.set(connection);
        }
        return connection;
    }

}
