package pers.zhangyang.easyguishop.dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easyguishop.meta.ItemStockMeta;
import pers.zhangyang.easylibrary.base.DaoBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ItemStockDao extends DaoBase {


    public int init() {
        try {

            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "CREATE TABLE IF NOT EXISTS item_stock(" +
                    "player_uuid TEXT   ," +
                    "item_stack TEXT   ," +
                    "amount INT   " +
                    ")");
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @NotNull
    public List<ItemStockMeta> list() {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "SELECT * FROM item_stock " +
                    "");
            ResultSet rs = ps.executeQuery();

            return multipleTransform(rs, ItemStockMeta.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public List<ItemStockMeta> listByPlayerUuid(@NotNull String playerUuid) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "SELECT * FROM item_stock WHERE player_uuid = ?" +
                    "");
            ps.setString(1, playerUuid);
            ResultSet rs = ps.executeQuery();

            return multipleTransform(rs, ItemStockMeta.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int insert(@NotNull ItemStockMeta itemStockMeta) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "INSERT INTO item_stock (player_uuid,item_stack,amount)" +
                    "VALUES(?,?,?)");
            ps.setString(1, itemStockMeta.getPlayerUuid());
            ps.setString(2, itemStockMeta.getItemStack());
            ps.setInt(3, itemStockMeta.getAmount());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int deleteByPlayerUuidAndItemStack(@NotNull String playerUuid, @NotNull String itemStack) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "DELETE FROM item_stock where player_uuid=? and item_stack=? " +
                    "");
            ps.setString(1, playerUuid);
            ps.setString(2, itemStack);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public ItemStockMeta getByPlayerUuidAndItemStack(@NotNull String playerUuid, @NotNull String itemStack) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "SELECT * FROM item_stock where player_uuid=? and item_stack=?" +
                    "");
            ps.setString(1, playerUuid);
            ps.setString(2, itemStack);
            ResultSet rs = ps.executeQuery();

            return singleTransform(rs, ItemStockMeta.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
