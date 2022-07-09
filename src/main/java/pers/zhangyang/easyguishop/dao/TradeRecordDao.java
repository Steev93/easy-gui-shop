package pers.zhangyang.easyguishop.dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easyguishop.manager.ConnectionManager;
import pers.zhangyang.easyguishop.meta.TradeRecordMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TradeRecordDao {

    public static final TradeRecordDao INSTANCE = new TradeRecordDao();

    public int init() throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "CREATE TABLE IF NOT EXISTS trade_record(" +
                "uuid String NOT NULL ," +
                "customer_uuid String NOT NULL ," +
                "merchant_uuid String NOT NULL ," +
                "good_item_stack String NOT NULL ," +
                "trade_amount int NOT NULL ," +
                "good_system boolean NOT NULL ," +
                "trade_time String NOT NULL ," +
                "good_type String NOT NULL ," +
                "trade_tax_rate double ," +
                "good_currency_item_stack int ," +
                "good_vault_price double ," +
                "good_player_points_price int ," +
                "good_item_price int ," +
                "PRIMARY KEY (uuid)" +
                ")");
        return ps.executeUpdate();

    }

    public int insert(@NotNull TradeRecordMeta tradeRecordMeta) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "INSERT INTO trade_record (uuid,customer_uuid,merchant_uuid,good_item_stack,trade_amount,good_system,trade_time,good_type," +
                "good_currency_item_stack,good_vault_price,good_player_points_price,good_item_price,trade_tax_rate)" +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
        ps.setString(1, tradeRecordMeta.getUuid());
        ps.setString(2, tradeRecordMeta.getCustomerUuid());
        ps.setString(3, tradeRecordMeta.getMerchantUuid());
        ps.setString(4, tradeRecordMeta.getGoodItemStack());
        ps.setInt(5, tradeRecordMeta.getTradeAmount());
        ps.setBoolean(6, tradeRecordMeta.isGoodSystem());
        ps.setLong(7, tradeRecordMeta.getTradeTime());
        ps.setString(8, tradeRecordMeta.getGoodType());
        ps.setObject(9, tradeRecordMeta.getGoodCurrencyItemStack());
        ps.setObject(10, tradeRecordMeta.getGoodVaultPrice());
        ps.setObject(11, tradeRecordMeta.getGoodPlayerPointsPrice());
        ps.setObject(12, tradeRecordMeta.getGoodItemPrice());
        ps.setObject(13, tradeRecordMeta.getTradeTaxRate());
        return ps.executeUpdate();
    }

    @NotNull
    public List<TradeRecordMeta> listByCustomerUuidOrMerchantUuid(@NotNull String playerUuid) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "SELECT * FROM trade_record WHERE customer_uuid = ? OR merchant_uuid=?" +
                "");
        ps.setString(1, playerUuid);
        ps.setString(2, playerUuid);
        ResultSet rs = ps.executeQuery();
        List<TradeRecordMeta> goodMetaList = new ArrayList<>();
        while (rs.next()) {
            goodMetaList.add(transform(rs));
        }
        return goodMetaList;

    }

    @NotNull
    public List<TradeRecordMeta> list() throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "SELECT * FROM trade_record" +
                "");
        ResultSet rs = ps.executeQuery();
        List<TradeRecordMeta> goodMetaList = new ArrayList<>();
        while (rs.next()) {
            goodMetaList.add(transform(rs));
        }
        return goodMetaList;

    }

    @Nullable
    public TradeRecordMeta getByUuid(@NotNull String uuid) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "SELECT * FROM trade_record WHERE uuid = ?" +
                "");
        ps.setString(1, uuid);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return transform(rs);
        }
        return null;
    }

    public int deleteByUuid(@NotNull String uuid) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "DELETE FROM trade_record WHERE uuid = ?" +
                "");
        ps.setString(1, uuid);
        return ps.executeUpdate();
    }

    @NotNull
    private TradeRecordMeta transform(@NotNull ResultSet rs) throws SQLException {
        TradeRecordMeta shopMeta = new TradeRecordMeta(rs.getString("uuid"), rs.getString("customer_uuid"),
                rs.getString("merchant_uuid"), rs.getString("good_item_stack"),
                rs.getInt("trade_amount"), rs.getBoolean("good_system"),
                rs.getLong("trade_time"), rs.getString("good_type"),rs.getDouble("trade_tax_rate"));
        shopMeta.setGoodCurrencyItemStack(rs.getString("good_currency_item_stack"));
        shopMeta.setGoodVaultPrice((Double) rs.getObject("good_vault_price"));
        shopMeta.setGoodPlayerPointsPrice((Integer) rs.getObject("good_player_points_price"));
        shopMeta.setGoodItemPrice((Integer) rs.getObject("good_item_price"));
        return shopMeta;
    }


}
