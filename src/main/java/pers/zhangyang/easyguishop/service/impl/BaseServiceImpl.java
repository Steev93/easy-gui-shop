package pers.zhangyang.easyguishop.service.impl;

import com.google.gson.Gson;
import org.bukkit.ChatColor;
import pers.zhangyang.easyguishop.EasyGuiShop;
import pers.zhangyang.easyguishop.dao.*;
import pers.zhangyang.easyguishop.manager.ConnectionManager;
import pers.zhangyang.easyguishop.meta.*;
import pers.zhangyang.easyguishop.service.BaseService;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BaseServiceImpl implements BaseService {

    public static final BaseServiceImpl INSTANCE = new BaseServiceImpl();

    @Override
    public void initDatabase() throws SQLException {
        GoodDao.INSTANCE.init();
        IconDao.INSTANCE.init();
        ShopCollectorDao.INSTANCE.init();
        IconOwnerDao.INSTANCE.init();
        ShopDao.INSTANCE.init();
        ShopCommentDao.INSTANCE.init();
        TradeRecordDao.INSTANCE.init();
        ItemStockDao.INSTANCE.init();
        VersionDao.INSTANCE.init();
        if (VersionDao.INSTANCE.get() == null) {
            String[] strings = EasyGuiShop.instance.getDescription().getVersion().split("\\.");
            VersionDao.INSTANCE.insert(new VersionMeta(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]), Integer.parseInt(strings[2])));
        }
    }
    @Override
    public void transform2_0_4() throws SQLException {
        //从2.0.0到2.2.4的 如果不存在version并且存在update_table时，说明是2.0.0以前的版本 需要更新
        VersionMeta versionMeta=VersionDao.INSTANCE.get();
        assert versionMeta != null;
        if (versionMeta.getBig()==2&&versionMeta.getMiddle()>2){
            return;
        }
        if (versionMeta.getBig()==2&&versionMeta.getMiddle()==2&&versionMeta.getSmall()>=4){
            return;
        }


        List<IconMeta> iconMetaList=IconDao.INSTANCE.list();
        for (IconMeta iconMeta:iconMetaList){
            iconMeta.setName(ChatColor.translateAlternateColorCodes('&',iconMeta.getName()));
            IconDao.INSTANCE.deleteByUuid(iconMeta.getUuid());
            IconDao.INSTANCE.insert(iconMeta);
        }
        VersionDao.INSTANCE.delete();
        VersionDao.INSTANCE.insert(new VersionMeta(2, 2, 4));

    }

    @Override
    public void transform2_0_0() throws SQLException {
        //从1.3.11更新到2.0.0的 如果不存在version并且存在update_table时，说明是2.0.0以前的版本 需要更新
        DatabaseMetaData metaData = ConnectionManager.INSTANCE.getConnection().getMetaData();
        ResultSet rs = metaData.getTables(null, null, "version", null);
        if (rs.next()) {
            return;
        }
        rs = metaData.getTables(null, null, "update_table", null);
        if (!rs.next()) {
            return;
        }
        GoodDao.INSTANCE.init();
        IconDao.INSTANCE.init();
        ShopCollectorDao.INSTANCE.init();
        IconOwnerDao.INSTANCE.init();
        ShopDao.INSTANCE.init();
        ShopCommentDao.INSTANCE.init();
        TradeRecordDao.INSTANCE.init();
        ItemStockDao.INSTANCE.init();
        VersionDao.INSTANCE.init();
        //更新shop_table表到shop表
        PreparedStatement ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
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
            ShopDao.INSTANCE.insert(shopMeta);
        }

        //更新icon_table表为icon表
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
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

            IconDao.INSTANCE.insert(iconMeta);
        }

        //更新good_table表为good表
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
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
            GoodDao.INSTANCE.insert(goodMeta);
        }

        //更新collect_table到shopCollector表
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "SELECT * FROM collect_table");
        rs = ps.executeQuery();
        while (rs.next()) {
            ShopCollectorMeta shopCollectorMeta = new ShopCollectorMeta(rs.getString("shopUuid"),
                    rs.getString("shopCollectorUuid"));

            ShopCollectorDao.INSTANCE.insert(shopCollectorMeta);
        }

        //更新icon_have_table到iconOwner表
        ps = ConnectionManager.INSTANCE.getConnection().prepareStatement("" +
                "SELECT * FROM icon_have_table");
        rs = ps.executeQuery();
        while (rs.next()) {
            IconOwnerMeta iconOwnerMeta = new IconOwnerMeta(rs.getString("iconUuid"),
                    rs.getString("iconOwnerUuid"));

            IconOwnerDao.INSTANCE.insert(iconOwnerMeta);
        }

        VersionDao.INSTANCE.delete();
        VersionDao.INSTANCE.insert(new VersionMeta(2, 0, 0));


    }


}
