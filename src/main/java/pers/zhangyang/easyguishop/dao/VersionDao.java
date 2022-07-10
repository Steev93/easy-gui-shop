package pers.zhangyang.easyguishop.dao;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easyguishop.manager.ConnectionManager;
import pers.zhangyang.easyguishop.meta.VersionMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VersionDao {

    public static final VersionDao INSTANCE = new VersionDao();

    public int init() throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "CREATE TABLE IF NOT EXISTS version (" +
                "  big INT   ," +
                "  middle INT   ," +
                "  small INT  " +
                ")");
        return ps.executeUpdate();

    }

    @Nullable
    public VersionMeta get() throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("select * from version");
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return transform(rs);
        }
        return null;
    }

    public int insert(@NotNull VersionMeta version) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("insert into version (big,middle,small)" +
                "values(?,?,?)");
        ps.setInt(1, version.getBig());
        ps.setInt(2, version.getMiddle());
        ps.setInt(3, version.getSmall());
        return ps.executeUpdate();
    }

    public int delete() throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("delete from version ");
        return ps.executeUpdate();
    }

    @NotNull
    private VersionMeta transform(@NotNull ResultSet rs) throws SQLException {
        return new VersionMeta(rs.getInt("big"), rs.getInt("middle"), rs.getInt("small"));
    }
}
