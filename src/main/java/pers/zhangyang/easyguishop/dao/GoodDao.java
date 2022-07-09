package pers.zhangyang.easyguishop.dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easyguishop.manager.ConnectionManager;
import pers.zhangyang.easyguishop.meta.GoodMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GoodDao {

    public static final GoodDao INSTANCE = new GoodDao();

    public int init() throws SQLException {
        PreparedStatement ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "CREATE TABLE IF NOT EXISTS good(" +
                "uuid String NOT NULL ," +
                "name String NOT NULL ," +
                "good_item_stack NOT NULL ," +
                "type String NOT NULL ," +
                "create_time long NOT NULL ," +
                "system boolean NOT NULL ," +
                "stock int NOT NULL ," +
                "shop_uuid String NOT NULL ," +
                "limit_time long ," +
                "currency_item_stack int ," +
                "experience_price int ," +
                "item_price int ," +
                "vault_price double ," +
                "player_points_price int ," +
                "PRIMARY KEY (uuid)" +
                ")");
        return ps.executeUpdate();
    }

    public int insert(@NotNull GoodMeta goodMeta) throws SQLException {
        PreparedStatement ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "INSERT INTO good (uuid,name,good_item_stack,type,create_time,system,stock,shop_uuid,limit_time," +
                "currency_item_stack,experience_price,item_price,vault_price,player_points_price)" +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        ps.setString(1, goodMeta.getUuid());
        ps.setString(2, goodMeta.getName());
        ps.setString(3, goodMeta.getGoodItemStack());
        ps.setString(4, goodMeta.getType());
        ps.setLong(5, goodMeta.getCreateTime());
        ps.setBoolean(6, goodMeta.isSystem());
        ps.setInt(7, goodMeta.getStock());
        ps.setObject(8, goodMeta.getShopUuid());
        ps.setObject(9, goodMeta.getLimitTime());
        ps.setObject(10, goodMeta.getCurrencyItemStack());
        ps.setObject(11, goodMeta.getExperiencePrice());
        ps.setObject(12, goodMeta.getItemPrice());
        ps.setObject(13, goodMeta.getVaultPrice());
        ps.setObject(14, goodMeta.getPlayerPointsPrice());
        return ps.executeUpdate();
    }

    @NotNull
    public List<GoodMeta> list() throws SQLException {
        PreparedStatement ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "SELECT * FROM good " +
                "");
        ResultSet rs = ps.executeQuery();
        List<GoodMeta> goodMetaList = new ArrayList<>();
        while (rs.next()) {
            goodMetaList.add(transform(rs));
        }
        return goodMetaList;
    }

    @NotNull
    public List<GoodMeta> listByShopUuid(@NotNull String shopUuid) throws SQLException {
        PreparedStatement ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "SELECT * FROM good WHERE shop_uuid = ?" +
                "");
        ps.setString(1, shopUuid);
        ResultSet rs = ps.executeQuery();
        List<GoodMeta> goodMetaList = new ArrayList<>();
        while (rs.next()) {
            goodMetaList.add(transform(rs));
        }
        return goodMetaList;
    }

    @Nullable
    public GoodMeta getByUuid(@NotNull String goodUuid) throws SQLException {
        PreparedStatement ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "SELECT * FROM good WHERE uuid = ?" +
                "");
        ps.setString(1, goodUuid);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return transform(rs);
        }
        return null;
    }


    @Nullable
    public GoodMeta getByNameAndShopUuid(@NotNull String goodName, @NotNull String shopUuid) throws SQLException {
        PreparedStatement ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "SELECT * FROM good WHERE name = ? and shop_uuid=?" +
                "");
        ps.setString(1, goodName);
        ps.setString(2, shopUuid);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return transform(rs);
        }
        return null;
    }

    public int deleteByUuid(@NotNull String goodUuid) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "DELETE FROM good WHERE uuid = ?" +
                "");
        ps.setString(1, goodUuid);
        return ps.executeUpdate();
    }

    @NotNull
    private GoodMeta transform(@NotNull ResultSet rs) throws SQLException {
        GoodMeta goodMeta = new GoodMeta(rs.getString("uuid"), rs.getString("name"),
                rs.getString("good_item_stack"), rs.getString("type"),
                rs.getLong("create_time"),
                rs.getBoolean("system"), rs.getInt("stock"), rs.getString("shop_uuid"));
        goodMeta.setLimitTime((Integer) rs.getObject("limit_time"));
        goodMeta.setCurrencyItemStack(rs.getString("currency_item_stack"));
        goodMeta.setExperiencePrice((Integer) rs.getObject("experience_price"));
        goodMeta.setItemPrice((Integer) rs.getObject("item_price"));
        goodMeta.setVaultPrice((Double) rs.getObject("vault_price"));
        goodMeta.setPlayerPointsPrice((Integer) rs.getObject("player_points_price"));
        return goodMeta;
    }

}
