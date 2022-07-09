package pers.zhangyang.easyguishop.dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easyguishop.manager.ConnectionManager;
import pers.zhangyang.easyguishop.meta.ShopCommentMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShopCommentDao {

    public static final ShopCommentDao INSTANCE = new ShopCommentDao();


    public int init() throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "CREATE TABLE IF NOT EXISTS shop_comment(" +
                "uuid String NOT NULL ," +
                "commenter_uuid String NOT NULL ," +
                "shop_uuid String NOT NULL ," +
                "comment_time long NOT NULL ," +
                "content String NOT NULL ," +
                "PRIMARY KEY (uuid)" +
                ")");
        return ps.executeUpdate();
    }

    public int insert(@NotNull ShopCommentMeta shopCommentMeta) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "INSERT INTO shop_comment (uuid,commenter_uuid,shop_uuid,content,comment_time)" +
                "VALUES(?,?,?,?,?)");
        ps.setString(1, shopCommentMeta.getUuid());
        ps.setString(2, shopCommentMeta.getCommenterUuid());
        ps.setString(3, shopCommentMeta.getShopUuid());
        ps.setString(4, shopCommentMeta.getContent());
        ps.setLong(5, shopCommentMeta.getCommentTime());
        return ps.executeUpdate();
    }

    @Nullable
    public ShopCommentMeta getByUuid(@NotNull String uuid) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "SELECT * FROM shop_comment WHERE uuid = ?" +
                "");
        ps.setString(1, uuid);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return transform(rs);
        }
        return null;
    }

    @NotNull
    public List<ShopCommentMeta> listByPlayerUuid(@NotNull String playerUuid) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "SELECT * FROM shop_comment WHERE commenter_uuid = ?" +
                "");
        ps.setString(1, playerUuid);
        ResultSet rs = ps.executeQuery();
        List<ShopCommentMeta> shopCommentMetaList = new ArrayList<>();
        while (rs.next()) {
            shopCommentMetaList.add(transform(rs));
        }
        return shopCommentMetaList;
    }

    @NotNull
    public List<ShopCommentMeta> listByShopUuid(@NotNull String shopUuid) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "SELECT * FROM shop_comment WHERE shop_uuid = ?" +
                "");
        ps.setString(1, shopUuid);
        ResultSet rs = ps.executeQuery();
        List<ShopCommentMeta> shopCommentMetaList = new ArrayList<>();
        while (rs.next()) {
            shopCommentMetaList.add(transform(rs));
        }
        return shopCommentMetaList;
    }

    public int deleteByUuid(@NotNull String uuid) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "DELETE FROM shop_comment where uuid=? " +
                "");
        ps.setString(1, uuid);
        return ps.executeUpdate();
    }

    public int deleteByShopUuid(@NotNull String shopUuid) throws SQLException {
        PreparedStatement ps;
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "DELETE FROM shop_comment where shop_uuid=? " +
                "");
        ps.setString(1, shopUuid);
        return ps.executeUpdate();
    }

    @NotNull
    private ShopCommentMeta transform(@NotNull ResultSet rs) throws SQLException {
        return new ShopCommentMeta(rs.getString("uuid"), rs.getString("commenter_uuid"),
                rs.getString("shop_uuid"), rs.getString("content"), rs.getLong("comment_time"));
    }

}
