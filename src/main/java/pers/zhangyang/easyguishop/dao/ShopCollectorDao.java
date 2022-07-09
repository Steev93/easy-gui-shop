package pers.zhangyang.easyguishop.dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easyguishop.manager.ConnectionManager;
import pers.zhangyang.easyguishop.meta.ShopCollectorMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShopCollectorDao {

    public static final ShopCollectorDao INSTANCE = new ShopCollectorDao();


    public int init() throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "CREATE TABLE IF NOT EXISTS shop_collector(" +
                "shop_uuid String NOT NULL ," +
                "collector_uuid String NOT NULL ," +
                "PRIMARY KEY (shop_uuid,collector_uuid)" +
                ")");
        return ps.executeUpdate();
    }

    public int insert(@NotNull ShopCollectorMeta shopCollectorMeta) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "INSERT INTO shop_collector (shop_uuid,collector_uuid)" +
                "VALUES(?,?)");
        ps.setString(1, shopCollectorMeta.getShopUuid());
        ps.setString(2, shopCollectorMeta.getCollectorUuid());
        return ps.executeUpdate();
    }

    public int deleteByCollectorAndShopUuid(@NotNull String collectorUuid, @NotNull String shopUuid) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "DELETE FROM shop_collector where shop_uuid=? and collector_uuid=?" +
                "");
        ps.setString(1, shopUuid);
        ps.setString(2, collectorUuid);
        return ps.executeUpdate();
    }

    @Nullable
    public ShopCollectorMeta getByCollectorAndShopUuid(@NotNull String collectorUuid, @NotNull String shopUuid) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "SELECT * FROM shop_collector where shop_uuid=? and collector_uuid=?" +
                "");
        ps.setString(1, shopUuid);
        ps.setString(2, collectorUuid);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return transform(rs);
        }
        return null;
    }

    @NotNull
    public List<ShopCollectorMeta> listByCollectorUuid(@NotNull String collectorUuid) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "SELECT * FROM shop_collector WHERE collector_uuid = ?" +
                "");
        ps.setString(1, collectorUuid);
        ResultSet rs = ps.executeQuery();
        List<ShopCollectorMeta> shopCollectorMetaList = new ArrayList<>();
        while (rs.next()) {
            shopCollectorMetaList.add(transform(rs));
        }
        return shopCollectorMetaList;
    }

    @NotNull
    private ShopCollectorMeta transform(@NotNull ResultSet rs) throws SQLException {
        return new ShopCollectorMeta(rs.getString("shop_uuid"), rs.getString("collector_uuid"));
    }

}
