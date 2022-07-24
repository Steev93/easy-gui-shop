package pers.zhangyang.easyguishop.dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easyguishop.meta.ShopCollectorMeta;
import pers.zhangyang.easylibrary.base.DaoBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ShopCollectorDao extends DaoBase {


    public int init() {
        try {

            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "CREATE TABLE IF NOT EXISTS shop_collector(" +
                    "shop_uuid TEXT   ," +
                    "collector_uuid TEXT   " +
                    ")");
            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int insert(@NotNull ShopCollectorMeta shopCollectorMeta) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "INSERT INTO shop_collector (shop_uuid,collector_uuid)" +
                    "VALUES(?,?)");
            ps.setString(1, shopCollectorMeta.getShopUuid());
            ps.setString(2, shopCollectorMeta.getCollectorUuid());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int deleteByCollectorAndShopUuid(@NotNull String collectorUuid, @NotNull String shopUuid) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "DELETE FROM shop_collector where shop_uuid=? and collector_uuid=?" +
                    "");
            ps.setString(1, shopUuid);
            ps.setString(2, collectorUuid);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public ShopCollectorMeta getByCollectorAndShopUuid(@NotNull String collectorUuid, @NotNull String shopUuid) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "SELECT * FROM shop_collector where shop_uuid=? and collector_uuid=?" +
                    "");
            ps.setString(1, shopUuid);
            ps.setString(2, collectorUuid);
            ResultSet rs = ps.executeQuery();

            return singleTransform(rs, ShopCollectorMeta.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public List<ShopCollectorMeta> listByCollectorUuid(@NotNull String collectorUuid) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "SELECT * FROM shop_collector WHERE collector_uuid = ?" +
                    "");
            ps.setString(1, collectorUuid);
            ResultSet rs = ps.executeQuery();

            return multipleTransform(rs, ShopCollectorMeta.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
