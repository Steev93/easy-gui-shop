package pers.zhangyang.easyguishop.dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easyguishop.meta.IconMeta;
import pers.zhangyang.easylibrary.base.DaoBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class IconDao extends DaoBase {


    public int init() {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "CREATE TABLE IF NOT EXISTS icon(" +
                    "uuid TEXT   ," +
                    "`name` TEXT   ," +
                    "create_time BIGINT   ," +
                    "stock INT   ," +
                    "icon_item_stack TEXT   ," +
                    "`system` BIT   ," +
                    "limit_time INT ," +
                    "currency_item_stack TEXT  ," +
                    "item_price INT ," +
                    "vault_price DOUBLE ," +
                    "player_points_price INT " +
                    ")");
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public int insert(@NotNull IconMeta iconMeta) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "INSERT INTO icon (uuid,`name`,create_time,stock,icon_item_stack,limit_time," +
                    "item_price,vault_price,player_points_price,currency_item_stack,`system`)" +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, iconMeta.getUuid());
            ps.setString(2, iconMeta.getName());
            ps.setLong(3, iconMeta.getCreateTime());
            ps.setInt(4, iconMeta.getStock());
            ps.setString(5, iconMeta.getIconItemStack());

            ps.setObject(6, iconMeta.getLimitTime());
            ps.setObject(7, iconMeta.getItemPrice());
            ps.setObject(8, iconMeta.getVaultPrice());
            ps.setObject(9, iconMeta.getPlayerPointsPrice());
            ps.setString(10, iconMeta.getCurrencyItemStack());
            ps.setBoolean(11, iconMeta.isSystem());
            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int deleteByUuid(@NotNull String uuid) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "DELETE FROM icon where uuid=? " +
                    "");
            ps.setString(1, uuid);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public List<IconMeta> list() {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "SELECT * FROM icon " +
                    "");
            ResultSet rs = ps.executeQuery();

            return multipleTransform(rs, IconMeta.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public IconMeta getByUuid(@NotNull String uuid) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "SELECT * FROM icon WHERE uuid=? " +
                    "");
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();

            return singleTransform(rs, IconMeta.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public IconMeta getByName(@NotNull String name) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "SELECT * FROM icon WHERE `name` =? " +
                    "");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            return singleTransform(rs, IconMeta.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
