package pers.zhangyang.easyguishop.dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easyguishop.manager.ConnectionManager;
import pers.zhangyang.easyguishop.meta.IconOwnerMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IconOwnerDao {

    public static final IconOwnerDao INSTANCE = new IconOwnerDao();


    public int init() throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "CREATE TABLE IF NOT EXISTS icon_owner(" +
                "icon_uuid String NOT NULL ," +
                "owner_uuid String NOT NULL ," +
                "PRIMARY KEY (icon_uuid,owner_uuid)" +
                ")");
        return ps.executeUpdate();

    }

    @NotNull
    public List<IconOwnerMeta> listByOwnerUuid(@NotNull String ownerUuid) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "SELECT * FROM icon_owner WHERE owner_uuid = ?" +
                "");
        ps.setString(1, ownerUuid);
        ResultSet rs = ps.executeQuery();
        List<IconOwnerMeta> shopCollectorMetaList = new ArrayList<>();
        while (rs.next()) {
            shopCollectorMetaList.add(transform(rs));
        }
        return shopCollectorMetaList;
    }

    public int insert(@NotNull IconOwnerMeta iconOwnerMeta) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "INSERT INTO icon_owner (icon_uuid,owner_uuid)" +
                "VALUES(?,?)");
        ps.setString(1, iconOwnerMeta.getIconUuid());
        ps.setString(2, iconOwnerMeta.getOwnerUuid());
        return ps.executeUpdate();
    }

    public int deleteByIconUuid(@NotNull String iconUuid) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "DELETE FROM icon_owner where icon_uuid=? " +
                "");
        ps.setString(1, iconUuid);
        return ps.executeUpdate();
    }

    @Nullable
    public IconOwnerMeta getByIconUuidAndOwnerUuid(@NotNull String iconUuid, @NotNull String ownerUuid) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "SELECT * FROM icon_owner where icon_uuid=? and owner_uuid=?" +
                "");
        ps.setString(1, iconUuid);
        ps.setString(2, ownerUuid);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return transform(rs);
        }
        return null;
    }

    @NotNull
    private IconOwnerMeta transform(@NotNull ResultSet rs) throws SQLException {
        return new IconOwnerMeta(rs.getString("icon_uuid"), rs.getString("owner_uuid"));
    }

}
