package pers.zhangyang.easyguishop.dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easyguishop.meta.IconOwnerMeta;
import pers.zhangyang.easylibrary.base.DaoBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class IconOwnerDao extends DaoBase {


    public int init() {
        try {


            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "CREATE TABLE IF NOT EXISTS icon_owner(" +
                    "icon_uuid TEXT   ," +
                    "owner_uuid TEXT   " +
                    ")");
            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public List<IconOwnerMeta> listByOwnerUuid(@NotNull String ownerUuid) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "SELECT * FROM icon_owner WHERE owner_uuid = ?" +
                    "");
            ps.setString(1, ownerUuid);
            ResultSet rs = ps.executeQuery();

            return multipleTransform(rs, IconOwnerMeta.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int insert(@NotNull IconOwnerMeta iconOwnerMeta) {
        try {

            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "INSERT INTO icon_owner (icon_uuid,owner_uuid)" +
                    "VALUES(?,?)");
            ps.setString(1, iconOwnerMeta.getIconUuid());
            ps.setString(2, iconOwnerMeta.getOwnerUuid());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int deleteByIconUuid(@NotNull String iconUuid) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "DELETE FROM icon_owner where icon_uuid=? " +
                    "");
            ps.setString(1, iconUuid);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public IconOwnerMeta getByIconUuidAndOwnerUuid(@NotNull String iconUuid, @NotNull String ownerUuid) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "SELECT * FROM icon_owner where icon_uuid=? and owner_uuid=?" +
                    "");
            ps.setString(1, iconUuid);
            ps.setString(2, ownerUuid);
            ResultSet rs = ps.executeQuery();

            return singleTransform(rs, IconOwnerMeta.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
