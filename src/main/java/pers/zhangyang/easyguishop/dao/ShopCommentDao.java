package pers.zhangyang.easyguishop.dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easyguishop.meta.ShopCommentMeta;
import pers.zhangyang.easylibrary.base.DaoBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ShopCommentDao extends DaoBase {


    public int init() {
        try {

            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "CREATE TABLE IF NOT EXISTS shop_comment(" +
                    "uuid TEXT   ," +
                    "commenter_uuid TEXT   ," +
                    "shop_uuid TEXT   ," +
                    "comment_time BIGINT   ," +
                    "content TEXT   " +
                    ")");
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int insert(@NotNull ShopCommentMeta shopCommentMeta) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "INSERT INTO shop_comment (uuid,commenter_uuid,shop_uuid,content,comment_time)" +
                    "VALUES(?,?,?,?,?)");
            ps.setString(1, shopCommentMeta.getUuid());
            ps.setString(2, shopCommentMeta.getCommenterUuid());
            ps.setString(3, shopCommentMeta.getShopUuid());
            ps.setString(4, shopCommentMeta.getContent());
            ps.setLong(5, shopCommentMeta.getCommentTime());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public ShopCommentMeta getByUuid(@NotNull String uuid) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "SELECT * FROM shop_comment WHERE uuid = ?" +
                    "");
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();

            return singleTransform(rs, ShopCommentMeta.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public List<ShopCommentMeta> listByPlayerUuid(@NotNull String playerUuid) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "SELECT * FROM shop_comment WHERE commenter_uuid = ?" +
                    "");
            ps.setString(1, playerUuid);
            ResultSet rs = ps.executeQuery();

            return multipleTransform(rs, ShopCommentMeta.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public List<ShopCommentMeta> listByShopUuid(@NotNull String shopUuid) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "SELECT * FROM shop_comment WHERE shop_uuid = ?" +
                    "");
            ps.setString(1, shopUuid);
            ResultSet rs = ps.executeQuery();

            return multipleTransform(rs, ShopCommentMeta.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int deleteByUuid(@NotNull String uuid) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "DELETE FROM shop_comment where uuid=? " +
                    "");
            ps.setString(1, uuid);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int deleteByShopUuid(@NotNull String shopUuid) {
        try {
            PreparedStatement ps;
            ps = getConnection().prepareStatement("" +
                    "DELETE FROM shop_comment where shop_uuid=? " +
                    "");
            ps.setString(1, shopUuid);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
