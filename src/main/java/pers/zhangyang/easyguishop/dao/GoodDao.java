package pers.zhangyang.easyguishop.dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easyguishop.meta.GoodMeta;
import pers.zhangyang.easylibrary.base.DaoBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class GoodDao extends DaoBase {


    public int init() {
        try {
            PreparedStatement ps = getConnection().prepareStatement("" +
                    "CREATE TABLE IF NOT EXISTS good(" +
                    "uuid TEXT   ," +
                    "`name` TEXT   ," +
                    "good_item_stack TEXT   ," +
                    "`type` TEXT   ," +
                    "create_time BIGINT   ," +
                    "`system` BIT   ," +
                    "stock INT   ," +
                    "shop_uuid TEXT   ," +
                    "limit_time INT ," +
                    "currency_item_stack TEXT ," +
                    "item_price INT ," +
                    "vault_price DOUBLE ," +
                    "player_points_price INT ," +
                    "limit_frequency INT " +
                    ")");
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int insert(@NotNull GoodMeta goodMeta) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("" +
                    "INSERT INTO good (uuid,`name`,good_item_stack,`type`,create_time,`system`,stock,shop_uuid,limit_time," +
                    "currency_item_stack,item_price,vault_price,player_points_price,limit_frequency)" +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, goodMeta.getUuid());
            ps.setString(2, goodMeta.getName());
            ps.setString(3, goodMeta.getGoodItemStack());
            ps.setString(4, goodMeta.getType());
            ps.setLong(5, goodMeta.getCreateTime());
            ps.setBoolean(6, goodMeta.isSystem());
            ps.setInt(7, goodMeta.getStock());
            ps.setString(8, goodMeta.getShopUuid());
            ps.setObject(9, goodMeta.getLimitTime());
            ps.setString(10, goodMeta.getCurrencyItemStack());
            ps.setObject(11, goodMeta.getItemPrice());
            ps.setObject(12, goodMeta.getVaultPrice());
            ps.setObject(13, goodMeta.getPlayerPointsPrice());
            ps.setObject(14, goodMeta.getLimitFrequency());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public List<GoodMeta> list() {
        try {
            PreparedStatement ps = getConnection().prepareStatement("" +
                    "SELECT * FROM good " +
                    "");
            ResultSet rs = ps.executeQuery();
            return multipleTransform(rs, GoodMeta.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public List<GoodMeta> listByShopUuid(@NotNull String shopUuid) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("" +
                    "SELECT * FROM good WHERE shop_uuid = ?" +
                    "");
            ps.setString(1, shopUuid);
            ResultSet rs = ps.executeQuery();
            return multipleTransform(rs, GoodMeta.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public GoodMeta getByUuid(@NotNull String goodUuid) {
        try {

            PreparedStatement ps = getConnection().prepareStatement("" +
                    "SELECT * FROM good WHERE uuid = ?" +
                    "");
            ps.setString(1, goodUuid);
            ResultSet rs = ps.executeQuery();

            return singleTransform(rs, GoodMeta.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Nullable
    public GoodMeta getByNameAndShopUuid(@NotNull String goodName, @NotNull String shopUuid) {
        try {

            PreparedStatement ps = getConnection().prepareStatement("" +
                    "SELECT * FROM good WHERE `name` = ? and shop_uuid=?" +
                    "");
            ps.setString(1, goodName);
            ps.setString(2, shopUuid);
            ResultSet rs = ps.executeQuery();

            return singleTransform(rs, GoodMeta.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int deleteByUuid(@NotNull String goodUuid) {
        try {

            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "DELETE FROM good WHERE uuid = ?" +
                    "");
            ps.setString(1, goodUuid);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
