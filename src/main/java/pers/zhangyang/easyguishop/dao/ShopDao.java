package pers.zhangyang.easyguishop.dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easylibrary.base.DaoBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ShopDao extends DaoBase {


    public int init() {
        try {

            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @NotNull
    public int insert(@NotNull ShopMeta shopMeta) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public List<ShopMeta> listByOwnerUuid(@NotNull String ownerUuid) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "SELECT * FROM shop WHERE owner_uuid = ?" +
                    "");
            ps.setString(1, ownerUuid);
            ResultSet rs = ps.executeQuery();

            return multipleTransform(rs, ShopMeta.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public List<ShopMeta> listByIconUuid(@NotNull String iconUuid) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "SELECT * FROM shop WHERE icon_uuid = ?" +
                    "");
            ps.setString(1, iconUuid);
            ResultSet rs = ps.executeQuery();

            return multipleTransform(rs, ShopMeta.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public List<ShopMeta> list() {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "SELECT * FROM shop" +
                    "");
            ResultSet rs = ps.executeQuery();

            return multipleTransform(rs, ShopMeta.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Nullable
    public ShopMeta getByName(@NotNull String name) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "SELECT * FROM shop WHERE `name` = ?" +
                    "");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            return singleTransform(rs, ShopMeta.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Nullable
    public ShopMeta getByUuid(@NotNull String uuid) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "SELECT * FROM shop WHERE uuid = ?" +
                    "");
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();

            return singleTransform(rs, ShopMeta.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @NotNull
    public int deleteByUuid(@NotNull String uuid) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "DELETE FROM shop WHERE uuid = ?" +
                    "");
            ps.setString(1, uuid);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
