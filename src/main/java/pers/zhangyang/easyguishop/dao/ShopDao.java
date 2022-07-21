package pers.zhangyang.easyguishop.dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easyguishop.manager.ConnectionManager;
import pers.zhangyang.easyguishop.meta.ShopMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShopDao {

    public static final ShopDao INSTANCE = new ShopDao();

    public int init() throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "CREATE TABLE IF NOT EXISTS shop(" +
                "uuid TEXT   ," +
                "`name` TEXT   ," +
                "owner_uuid TEXT   ," +
                "create_time BIGINT   ," +
                "collect_amount INT   ," +
                "page_view INT   ," +
                "popularity INT   ," +
                "icon_uuid TEXT  ," +
                "location TEXT  ," +
                "description TEXT " +
                ")");
        return ps.executeUpdate();

    }

    @NotNull
    public int insert(@NotNull ShopMeta shopMeta) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "INSERT INTO shop (uuid,`name`,owner_uuid,create_time,collect_amount,icon_uuid,location," +
                "popularity,page_view,description)" +
                "VALUES(?,?,?,?,?,?,?,?,?,?)");
        ps.setString(1, shopMeta.getUuid());
        ps.setString(2, shopMeta.getName());
        ps.setString(3, shopMeta.getOwnerUuid());
        ps.setLong(4, shopMeta.getCreateTime());
        ps.setInt(5, shopMeta.getCollectAmount());
        ps.setString(6, shopMeta.getIconUuid());
        ps.setString(7, shopMeta.getLocation());
        ps.setInt(8, shopMeta.getPopularity());
        ps.setInt(9, shopMeta.getPageView());
        ps.setString(10, shopMeta.getDescription());
        return ps.executeUpdate();
    }

    @NotNull
    public List<ShopMeta> listByOwnerUuid(@NotNull String ownerUuid) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "SELECT * FROM shop WHERE owner_uuid = ?" +
                "");
        ps.setString(1, ownerUuid);
        ResultSet rs = ps.executeQuery();
        List<ShopMeta> shopMetaList = new ArrayList<>();
        while (rs.next()) {
            shopMetaList.add(transform(rs));
        }
        return shopMetaList;
    }

    @NotNull
    public List<ShopMeta> listByIconUuid(@NotNull String iconUuid) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "SELECT * FROM shop WHERE icon_uuid = ?" +
                "");
        ps.setString(1, iconUuid);
        ResultSet rs = ps.executeQuery();
        List<ShopMeta> shopMetaList = new ArrayList<>();
        while (rs.next()) {
            shopMetaList.add(transform(rs));
        }
        return shopMetaList;
    }

    @NotNull
    public List<ShopMeta> list() throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "SELECT * FROM shop" +
                "");
        ResultSet rs = ps.executeQuery();
        List<ShopMeta> shopMetaList = new ArrayList<>();
        while (rs.next()) {
            shopMetaList.add(transform(rs));
        }
        return shopMetaList;

    }

    @Nullable
    public ShopMeta getByName(@NotNull String name) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "SELECT * FROM shop WHERE `name` = ?" +
                "");
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return transform(rs);
        }
        return null;

    }

    @Nullable
    public ShopMeta getByUuid(@NotNull String uuid) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "SELECT * FROM shop WHERE uuid = ?" +
                "");
        ps.setString(1, uuid);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return transform(rs);
        }
        return null;

    }

    @NotNull
    public int deleteByUuid(@NotNull String uuid) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "DELETE FROM shop WHERE uuid = ?" +
                "");
        ps.setString(1, uuid);
        return ps.executeUpdate();

    }

    @NotNull
    private ShopMeta transform(@NotNull ResultSet rs) throws SQLException {
        ShopMeta shopMeta = new ShopMeta(rs.getString("uuid"), rs.getString("name"),
                rs.getString("owner_uuid"), rs.getLong("create_time"),
                rs.getInt("collect_amount"),
                rs.getInt("popularity"), rs.getInt("page_view"));
        shopMeta.setIconUuid(rs.getString("icon_uuid"));
        shopMeta.setLocation(rs.getString("location"));
        shopMeta.setShopDescription(rs.getString("description"));
        return shopMeta;
    }

}
