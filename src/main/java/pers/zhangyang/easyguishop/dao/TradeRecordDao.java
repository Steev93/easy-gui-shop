package pers.zhangyang.easyguishop.dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easyguishop.meta.TradeRecordMeta;
import pers.zhangyang.easylibrary.base.DaoBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TradeRecordDao extends DaoBase {

    public static final TradeRecordDao INSTANCE = new TradeRecordDao();

    public int init() {
        try {

            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "CREATE TABLE IF NOT EXISTS trade_record(" +
                    "uuid TEXT   ," +
                    "customer_uuid TEXT   ," +
                    "merchant_uuid TEXT   ," +
                    "good_item_stack TEXT   ," +
                    "trade_amount INT   ," +
                    "good_system BIT   ," +
                    "trade_time BIGINT   ," +
                    "good_type TEXT   ," +
                    "trade_tax_rate DOUBLE ," +
                    "good_currency_item_stack TEXT ," +
                    "good_vault_price DOUBLE ," +
                    "good_player_points_price INT ," +
                    "good_item_price INT " +
                    ")");
            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int insert(@NotNull TradeRecordMeta tradeRecordMeta) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
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
            ps.setString(9, tradeRecordMeta.getGoodCurrencyItemStack());
            ps.setObject(10, tradeRecordMeta.getGoodVaultPrice());
            ps.setObject(11, tradeRecordMeta.getGoodPlayerPointsPrice());
            ps.setObject(12, tradeRecordMeta.getGoodItemPrice());
            ps.setObject(13, tradeRecordMeta.getTradeTaxRate());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public List<TradeRecordMeta> listByCustomerUuidOrMerchantUuid(@NotNull String playerUuid) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "SELECT * FROM trade_record WHERE customer_uuid = ? OR merchant_uuid=?" +
                    "");
            ps.setString(1, playerUuid);
            ps.setString(2, playerUuid);
            ResultSet rs = ps.executeQuery();

            return multipleTransform(rs, TradeRecordMeta.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @NotNull
    public List<TradeRecordMeta> list() {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "SELECT * FROM trade_record" +
                    "");
            ResultSet rs = ps.executeQuery();

            return multipleTransform(rs, TradeRecordMeta.class);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public TradeRecordMeta getByUuid(@NotNull String uuid) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "SELECT * FROM trade_record WHERE uuid = ?" +
                    "");
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();

            return singleTransform(rs, TradeRecordMeta.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int deleteByUuid(@NotNull String uuid) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "DELETE FROM trade_record WHERE uuid = ?" +
                    "");
            ps.setString(1, uuid);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
