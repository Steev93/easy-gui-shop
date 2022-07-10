package pers.zhangyang.easyguishop.dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easyguishop.manager.ConnectionManager;
import pers.zhangyang.easyguishop.meta.IconMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IconDao {

    public static final IconDao INSTANCE = new IconDao();


    public int init() throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
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

    }

    public int insert(@NotNull IconMeta iconMeta) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "INSERT INTO icon (uuid,name,create_time,stock,icon_item_stack,limit_time," +
                "item_price,vault_price,player_points_price,currency_item_stack,system )" +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?)");
        ps.setString(1, iconMeta.getUuid());
        ps.setString(2, iconMeta.getName());
        ps.setLong(3, iconMeta.getCreateTime());
        ps.setLong(4, iconMeta.getStock());
        ps.setString(5, iconMeta.getIconItemStack());
        ps.setObject(6, iconMeta.getLimitTime());
        ps.setObject(7, iconMeta.getItemPrice());
        ps.setObject(8, iconMeta.getVaultPrice());
        ps.setObject(9, iconMeta.getPlayerPointsPrice());
        ps.setObject(10, iconMeta.getCurrencyItemStack());
        ps.setObject(11, iconMeta.isSystem());
        return ps.executeUpdate();
    }

    public int deleteByUuid(@NotNull String uuid) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "DELETE FROM icon where uuid=? " +
                "");
        ps.setString(1, uuid);
        return ps.executeUpdate();
    }

    @NotNull
    public List<IconMeta> list() throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "SELECT * FROM icon " +
                "");
        ResultSet rs = ps.executeQuery();
        List<IconMeta> shopCollectorMetaList = new ArrayList<>();
        while (rs.next()) {
            shopCollectorMetaList.add(transform(rs));
        }
        return shopCollectorMetaList;
    }

    @Nullable
    public IconMeta getByUuid(@NotNull String uuid) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "SELECT * FROM icon WHERE uuid=? " +
                "");
        ps.setString(1, uuid);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return transform(rs);
        }
        return null;
    }

    @Nullable
    public IconMeta getByName(@NotNull String name) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "SELECT * FROM icon WHERE name =? " +
                "");
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return transform(rs);
        }
        return null;
    }

    @NotNull
    private IconMeta transform(@NotNull ResultSet rs) throws SQLException {
        IconMeta iconMeta = new IconMeta(rs.getString("uuid"), rs.getString("name"), rs.getLong("create_time"),
                rs.getInt("stock"), rs.getString("icon_item_stack"), rs.getBoolean("system"));
        iconMeta.setLimitTime((Integer) rs.getObject("limit_time"));
        iconMeta.setItemPrice((Integer) rs.getObject("item_price"));
        iconMeta.setVaultPrice((Double) rs.getObject("vault_price"));
        iconMeta.setPlayerPointsPrice((Integer) rs.getObject("player_points_price"));
        iconMeta.setCurrencyItemStack(rs.getString("currency_item_stack"));
        return iconMeta;
    }

}
