package pers.zhangyang.easyguishop.dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easyguishop.manager.ConnectionManager;
import pers.zhangyang.easyguishop.meta.ItemStockMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemStockDao {
    public static final ItemStockDao INSTANCE = new ItemStockDao();


    public int init() throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "CREATE TABLE IF NOT EXISTS item_stock(" +
                "player_uuid String NOT NULL ," +
                "item_stack String NOT NULL ," +
                "amount int NOT NULL ," +
                "PRIMARY KEY (player_uuid,item_stack)" +
                ")");
        return ps.executeUpdate();

    }

    @NotNull
    public List<ItemStockMeta> list() throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "SELECT * FROM item_stock " +
                "");
        ResultSet rs = ps.executeQuery();
        List<ItemStockMeta> shopCollectorMetaList = new ArrayList<>();
        while (rs.next()) {
            shopCollectorMetaList.add(transform(rs));
        }
        return shopCollectorMetaList;
    }

    @NotNull
    public List<ItemStockMeta> listByPlayerUuid(@NotNull String playerUuid) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "SELECT * FROM item_stock WHERE player_uuid = ?" +
                "");
        ps.setString(1, playerUuid);
        ResultSet rs = ps.executeQuery();
        List<ItemStockMeta> shopCollectorMetaList = new ArrayList<>();
        while (rs.next()) {
            shopCollectorMetaList.add(transform(rs));
        }
        return shopCollectorMetaList;
    }

    public int insert(@NotNull ItemStockMeta itemStockMeta) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "INSERT INTO item_stock (player_uuid,item_stack,amount)" +
                "VALUES(?,?,?)");
        ps.setString(1, itemStockMeta.getPlayerUuid());
        ps.setString(2, itemStockMeta.getItemStack());
        ps.setInt(3, itemStockMeta.getAmount());
        return ps.executeUpdate();
    }

    public int deleteByPlayerUuidAndItemStack(@NotNull String playerUuid, @NotNull String itemStack) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "DELETE FROM item_stock where player_uuid=? and item_stack=? " +
                "");
        ps.setString(1, playerUuid);
        ps.setString(2, itemStack);
        return ps.executeUpdate();
    }

    @Nullable
    public ItemStockMeta getByPlayerUuidAndItemStack(@NotNull String playerUuid, @NotNull String itemStack) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "SELECT * FROM item_stock where player_uuid=? and item_stack=?" +
                "");
        ps.setString(1, playerUuid);
        ps.setString(2, itemStack);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return transform(rs);
        }
        return null;
    }

    @NotNull
    private ItemStockMeta transform(@NotNull ResultSet rs) throws SQLException {
        return new ItemStockMeta(rs.getString("player_uuid"), rs.getString("item_stack"), rs.getInt("amount"));
    }
}
