package pers.zhangyang.easyguishop.service.impl;

import com.google.gson.Gson;
import org.bukkit.ChatColor;
import pers.zhangyang.easyguishop.dao.*;
import pers.zhangyang.easyguishop.meta.*;
import pers.zhangyang.easyguishop.service.BaseService;
import pers.zhangyang.easylibrary.dao.VersionDao;
import pers.zhangyang.easylibrary.meta.VersionMeta;
import pers.zhangyang.easylibrary.util.VersionUtil;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static pers.zhangyang.easylibrary.base.DaoBase.getConnection;

public class BaseServiceImpl implements BaseService {


    @Override
    public void transform2_2_4() {
        try {

            //从2.0.0到2.2.4的 如果存在version表并且版本小于2.2.4，需要更新
            DatabaseMetaData metaData = getConnection().getMetaData();
            ResultSet rs = metaData.getTables(null, null, "version", null);
            if (!rs.next()) {
                return;
            }
            VersionDao versionDao = new VersionDao();
            VersionMeta versionMeta = versionDao.get();
            assert versionMeta != null;
            if (!VersionUtil.isOlderThan(versionMeta.getBig(), versionMeta.getMiddle(), versionMeta.getSmall(), 2, 2, 4)) {
                return;
            }
            List<IconMeta> iconMetaList = new IconDao().list();
            for (IconMeta iconMeta : iconMetaList) {
                iconMeta.setName(ChatColor.translateAlternateColorCodes('&', iconMeta.getName()));
                new IconDao().deleteByUuid(iconMeta.getUuid());
                new IconDao().insert(iconMeta);
            }
            versionDao.delete();
            versionDao.insert(new VersionMeta(2, 2, 4));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void transform2_8_0() {
        try {
            //从2.0.0到2.2.4的 如果存在version表并且版本小于2.2.4，需要更新
            DatabaseMetaData metaData = getConnection().getMetaData();
            ResultSet rs = metaData.getTables(null, null, "version", null);
            if (!rs.next()) {
                return;
            }
            VersionDao versionDao = new VersionDao();
            VersionMeta versionMeta = versionDao.get();
            assert versionMeta != null;
            if (!VersionUtil.isOlderThan(versionMeta.getBig(), versionMeta.getMiddle(), versionMeta.getSmall(), 2, 8, 0)) {
                return;
            }

            PreparedStatement ps=getConnection().prepareStatement("" +
                    "ALTER TABLE good ADD " +
                    "limit_frequency INT ");
            ps.executeUpdate();

            List<IconMeta> iconMetaList = new IconDao().list();
            for (IconMeta iconMeta : iconMetaList) {
                iconMeta.setName(ChatColor.translateAlternateColorCodes('&', iconMeta.getName()));
                new IconDao().deleteByUuid(iconMeta.getUuid());
                new IconDao().insert(iconMeta);
            }

            List<ShopMeta> shopMetaList = new ShopDao().list();
            for (ShopMeta shopMeta : shopMetaList) {
                shopMeta.setName(ChatColor.translateAlternateColorCodes('&', shopMeta.getName()));
                new ShopDao().deleteByUuid(shopMeta.getUuid());
                new ShopDao().insert(shopMeta);
            }

            List<GoodMeta> goodMetaList = new GoodDao().list();
            for (GoodMeta goodMeta : goodMetaList) {
                goodMeta.setName(ChatColor.translateAlternateColorCodes('&', goodMeta.getName()));
                new GoodDao().deleteByUuid(goodMeta.getUuid());
                new GoodDao().insert(goodMeta);
            }






            versionDao.delete();
            versionDao.insert(new VersionMeta(2, 8, 1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void transform2_0_0() {
        try {
            //从1.3.11更新到2.0.0的 如果不存在version并且存在update_table时，说明是2.0.0以前的版本 需要更新
            DatabaseMetaData metaData = getConnection().getMetaData();
            ResultSet rs = metaData.getTables(null, null, "version", null);
            if (rs.next()) {
                return;
            }
            rs = metaData.getTables(null, null, "update_table", null);
            if (!rs.next()) {
                return;
            }
            new GoodDao().init();
            new IconDao().init();
            new ShopCollectorDao().init();
            new IconOwnerDao().init();
            new ShopDao().init();
            new ShopCommentDao().init();
            new TradeRecordDao().init();
            new ItemStockDao().init();
            VersionDao versionDao = new VersionDao();
            versionDao.init();
            //更新shop_table表到shop表
            PreparedStatement ps = getConnection().prepareStatement("" +
                    "SELECT * FROM shop_table");
            rs = ps.executeQuery();
            while (rs.next()) {
                ShopMeta shopMeta = new ShopMeta(rs.getString("shopUuid"), rs.getString("shopName"),
                        rs.getString("shopOwnerUuid"), rs.getLong("shopCreateTime"), rs.getInt("shopCollectNumber"),
                        0, 0);
                shopMeta.setIconUuid(rs.getString("shopIconUuid"));
                String oldDescription = rs.getString("shopDescription");
                if (oldDescription != null) {
                    List<String> stringList = new ArrayList();
                    Collections.addAll(stringList, oldDescription.split(" "));
                    Gson gson = new Gson();
                    shopMeta.setShopDescription(gson.toJson(stringList));
                }
                new ShopDao().insert(shopMeta);
            }

            //更新icon_table表为icon表
            ps = getConnection().prepareStatement("" +
                    "SELECT * FROM icon_table");
            rs = ps.executeQuery();
            while (rs.next()) {
                IconMeta iconMeta = new IconMeta(rs.getString("iconUuid"), rs.getString("iconName"),
                        System.currentTimeMillis(), 0, rs.getString("iconData"), true);
                if (rs.getString("iconCurrency").equalsIgnoreCase("vault")) {
                    iconMeta.setVaultPrice(rs.getDouble("iconPrice"));
                }
                if (rs.getString("iconCurrency").equalsIgnoreCase("playerPoints")) {
                    iconMeta.setPlayerPointsPrice(rs.getInt("iconPrice"));
                }

                new IconDao().insert(iconMeta);
            }

            //更新good_table表为good表
            ps = getConnection().prepareStatement("" +
                    "SELECT * FROM good_table");
            rs = ps.executeQuery();
            while (rs.next()) {
                GoodMeta goodMeta = new GoodMeta(rs.getString("goodUuid"), rs.getString("goodName"),
                        rs.getString("goodData"), rs.getString("goodType"), System.currentTimeMillis(),
                        rs.getString("goodInfinity").equalsIgnoreCase("无限"), rs.getInt("goodRest"),
                        rs.getString("goodShopUuid"));
                if (rs.getString("goodCurrency").equalsIgnoreCase("vault")) {
                    goodMeta.setVaultPrice(rs.getDouble("goodPrice"));
                }
                if (rs.getString("goodCurrency").equalsIgnoreCase("playerPoints")) {
                    goodMeta.setPlayerPointsPrice(rs.getInt("goodPrice"));
                }
                new GoodDao().insert(goodMeta);
            }

            //更新collect_table到shopCollector表
            ps = getConnection().prepareStatement("" +
                    "SELECT * FROM collect_table");
            rs = ps.executeQuery();
            while (rs.next()) {
                ShopCollectorMeta shopCollectorMeta = new ShopCollectorMeta(rs.getString("shopUuid"),
                        rs.getString("shopCollectorUuid"));

                new ShopCollectorDao().insert(shopCollectorMeta);
            }

            //更新icon_have_table到iconOwner表
            ps = getConnection().prepareStatement("" +
                    "SELECT * FROM icon_have_table");
            rs = ps.executeQuery();
            while (rs.next()) {
                IconOwnerMeta iconOwnerMeta = new IconOwnerMeta(rs.getString("iconUuid"),
                        rs.getString("iconOwnerUuid"));

                new IconOwnerDao().insert(iconOwnerMeta);
            }

            versionDao.delete();
            versionDao.insert(new VersionMeta(2, 0, 0));


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
