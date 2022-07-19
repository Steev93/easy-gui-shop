package pers.zhangyang.easyguishop.service;

import java.sql.SQLException;

public interface BaseService {
    void initDatabase() throws SQLException;

    void transform2_0_0() throws SQLException;

    void transform2_0_4() throws SQLException;
}
