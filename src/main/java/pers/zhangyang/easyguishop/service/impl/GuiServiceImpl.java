package pers.zhangyang.easyguishop.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easyguishop.dao.*;
import pers.zhangyang.easyguishop.exception.*;
import pers.zhangyang.easyguishop.meta.*;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.yaml.SettingYaml;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuiServiceImpl implements GuiService {

    public static final GuiServiceImpl INSTANCE = new GuiServiceImpl();

    @Override
    public List<ShopMeta> listShop() throws SQLException {
        List<ShopMeta> shopMetaList = ShopDao.INSTANCE.list();
        shopMetaList.sort((o1, o2) -> {
            int star1 = Math.addExact(Math.addExact(
                            Math.multiplyExact(o1.getPopularity(),SettingYaml.INSTANCE.getHotValueCoefficient("setting.hotValueCoefficient.popularity")),
                            Math.multiplyExact(o1.getCollectAmount(),SettingYaml.INSTANCE.getHotValueCoefficient("setting.hotValueCoefficient.collectAmount"))),
                    Math.multiplyExact(o1.getPageView(),SettingYaml.INSTANCE.getHotValueCoefficient("setting.hotValueCoefficient.pageView")));
            int star2 = Math.addExact(Math.addExact(
                            Math.multiplyExact(o2.getPopularity(),SettingYaml.INSTANCE.getHotValueCoefficient("setting.hotValueCoefficient.popularity")),
                            Math.multiplyExact(o2.getCollectAmount(),SettingYaml.INSTANCE.getHotValueCoefficient("setting.hotValueCoefficient.collectAmount"))),
                    Math.multiplyExact(o2.getPageView(),SettingYaml.INSTANCE.getHotValueCoefficient("setting.hotValueCoefficient.pageView")));
            return star2 - star1;
        });
        return shopMetaList;
    }

    @Override
    public List<ShopMeta> listPlayerShop(String ownerUuid) throws SQLException {
        List<ShopMeta> shopMetaList = ShopDao.INSTANCE.listByOwnerUuid(ownerUuid);
        shopMetaList.sort((o1, o2) -> {
            int star1 = Math.addExact(Math.addExact(
                            Math.multiplyExact(o1.getPopularity(),SettingYaml.INSTANCE.getHotValueCoefficient("setting.hotValueCoefficient.popularity")),
                            Math.multiplyExact(o1.getCollectAmount(),SettingYaml.INSTANCE.getHotValueCoefficient("setting.hotValueCoefficient.collectAmount"))),
                    Math.multiplyExact(o1.getPageView(),SettingYaml.INSTANCE.getHotValueCoefficient("setting.hotValueCoefficient.pageView")));
            int star2 = Math.addExact(Math.addExact(
                            Math.multiplyExact(o2.getPopularity(),SettingYaml.INSTANCE.getHotValueCoefficient("setting.hotValueCoefficient.popularity")),
                            Math.multiplyExact(o2.getCollectAmount(),SettingYaml.INSTANCE.getHotValueCoefficient("setting.hotValueCoefficient.collectAmount"))),
                    Math.multiplyExact(o2.getPageView(),SettingYaml.INSTANCE.getHotValueCoefficient("setting.hotValueCoefficient.pageView")));
            return star2 - star1;
        });
        return shopMetaList;
    }

    @Override
    public List<ShopMeta> listPlayerCollectedShop(String playerUuid) throws SQLException {
        List<ShopMeta> shopMetaList = ShopDao.INSTANCE.list();
        List<ShopCollectorMeta> shopCollectorMetaList = ShopCollectorDao.INSTANCE.listByCollectorUuid(playerUuid);
        shopMetaList.removeIf(shopMeta -> {
            for (ShopCollectorMeta s : shopCollectorMetaList) {
                if (s.getShopUuid().equals(shopMeta.getUuid())) {
                    return false;
                }
            }
            return true;
        });
        shopMetaList.sort((o1, o2) -> {
            int star1 = Math.addExact(Math.addExact(
                            Math.multiplyExact(o1.getPopularity(),SettingYaml.INSTANCE.getHotValueCoefficient("setting.hotValueCoefficient.popularity")),
                            Math.multiplyExact(o1.getCollectAmount(),SettingYaml.INSTANCE.getHotValueCoefficient("setting.hotValueCoefficient.collectAmount"))),
                    Math.multiplyExact(o1.getPageView(),SettingYaml.INSTANCE.getHotValueCoefficient("setting.hotValueCoefficient.pageView")));
            int star2 = Math.addExact(Math.addExact(
                            Math.multiplyExact(o2.getPopularity(),SettingYaml.INSTANCE.getHotValueCoefficient("setting.hotValueCoefficient.popularity")),
                            Math.multiplyExact(o2.getCollectAmount(),SettingYaml.INSTANCE.getHotValueCoefficient("setting.hotValueCoefficient.collectAmount"))),
                    Math.multiplyExact(o2.getPageView(),SettingYaml.INSTANCE.getHotValueCoefficient("setting.hotValueCoefficient.pageView")));
            return star2 - star1;
        });
        return shopMetaList;
    }

    @Override
    public List<ShopCommentMeta> listPlayerComment(String playerUuid) throws SQLException {
        List<ShopCommentMeta> shopCommentMetaList = ShopCommentDao.INSTANCE.listByPlayerUuid(playerUuid);
        Collections.reverse(shopCommentMetaList);
        return shopCommentMetaList;
    }

    @Override
    public List<ShopCommentMeta> listShopComment(String shopUuid) throws SQLException, NotExistShopException {
        ShopMeta shopMeta = ShopDao.INSTANCE.getByUuid(shopUuid);
        if (shopMeta == null) {
            throw new NotExistShopException();
        }
        List<ShopCommentMeta> shopCommentMetaList = ShopCommentDao.INSTANCE.listByShopUuid(shopUuid);
        Collections.reverse(shopCommentMetaList);
        return shopCommentMetaList;
    }

    @Override
    public List<IconMeta> listIcon() throws SQLException {
        List<IconMeta> iconMetaList = IconDao.INSTANCE.list();
        Collections.reverse(iconMetaList);
        return iconMetaList;
    }

    @Override
    public List<IconMeta> listPlayerIcon(String ownerUuid) throws SQLException {
        List<IconMeta> shopMetaList = IconDao.INSTANCE.list();
        List<IconOwnerMeta> iconOwnerMetaList = IconOwnerDao.INSTANCE.listByOwnerUuid(ownerUuid);
        shopMetaList.removeIf(iconMeta -> {
            for (IconOwnerMeta i : iconOwnerMetaList) {
                if (i.getIconUuid().equals(iconMeta.getUuid())) {
                    return false;
                }
            }
            return true;
        });
        Collections.reverse(shopMetaList);
        return shopMetaList;
    }

    @Override
    public List<GoodMeta> listShopGood(String shopUuid) throws SQLException, NotExistShopException {
        ShopMeta shopMeta = ShopDao.INSTANCE.getByUuid(shopUuid);
        if (shopMeta == null) {
            throw new NotExistShopException();
        }
        List<GoodMeta> goodMetaList = GoodDao.INSTANCE.listByShopUuid(shopUuid);
        Collections.reverse(goodMetaList);
        return goodMetaList;
    }

    @Override
    public List<ItemStockMeta> listPlayerItemStock(String playerUuid) throws SQLException {
        List<ItemStockMeta> itemStockMetaList = ItemStockDao.INSTANCE.listByPlayerUuid(playerUuid);
        Collections.reverse(itemStockMetaList);
        return itemStockMetaList;
    }

    @Override
    public List<TradeRecordMeta> listPlayerTradeRecord(String playerUuid) throws SQLException {
        List<TradeRecordMeta> tradeRecordMetaList = TradeRecordDao.INSTANCE.listByCustomerUuidOrMerchantUuid(playerUuid);
        Collections.reverse(tradeRecordMetaList);
        return tradeRecordMetaList;
    }

    @Override
    public void createShop(@NotNull ShopMeta shopMeta) throws DuplicateShopException, SQLException {
        if (ShopDao.INSTANCE.getByName(shopMeta.getName()) != null || ShopDao.INSTANCE.getByUuid(shopMeta.getUuid()) != null) {
            throw new DuplicateShopException();
        }
        ShopDao.INSTANCE.insert(shopMeta);
    }

    @Override
    public void viewShop(@NotNull String shopUuid, int amount) throws NotExistShopException, SQLException {
        ShopMeta shopMeta = ShopDao.INSTANCE.getByUuid(shopUuid);
        if (shopMeta == null) {
            throw new NotExistShopException();
        }
        if (amount <= Integer.MAX_VALUE - shopMeta.getPageView()) {
            shopMeta.setPageView(shopMeta.getPageView() + amount);
        } else {
            shopMeta.setPageView(Integer.MAX_VALUE);
        }
        ShopDao.INSTANCE.deleteByUuid(shopMeta.getUuid());
        ShopDao.INSTANCE.insert(shopMeta);
    }

    @Override
    public void deleteShop(String shopName) throws NotExistShopException, ShopNotEmptyException, SQLException {
        ShopMeta shopMeta = ShopDao.INSTANCE.getByName(shopName);
        if (shopMeta == null) {
            throw new NotExistShopException();
        }
        if (!GoodDao.INSTANCE.listByShopUuid(shopMeta.getUuid()).isEmpty()) {
            throw new ShopNotEmptyException();
        }
        ShopCommentDao.INSTANCE.deleteByShopUuid(shopMeta.getUuid());
        ShopDao.INSTANCE.deleteByUuid(shopMeta.getUuid());
    }

    @Override
    public void addShopDescription(String shopUuid, String d) throws NotExistShopException, SQLException {
        ShopMeta shopMeta = ShopDao.INSTANCE.getByUuid(shopUuid);
        if (shopMeta == null) {
            throw new NotExistShopException();
        }
        ShopDao.INSTANCE.deleteByUuid(shopMeta.getUuid());

        Gson gson = new Gson();
        Type stringListType = new TypeToken<ArrayList<String>>() {
        }.getType();
        List<String> stringList = gson.fromJson(shopMeta.getDescription(), stringListType);
        if (stringList != null) {
            stringList.add(d);
        } else {
            stringList = Collections.singletonList(d);
        }
        shopMeta.setShopDescription(gson.toJson(stringList));
        ShopDao.INSTANCE.insert(shopMeta);
    }

    @Override
    public void removeShopDescription(String shopUuid) throws NotExistShopException, SQLException, NotExistLineException {
        ShopMeta shopMeta = ShopDao.INSTANCE.getByUuid(shopUuid);
        if (shopMeta == null) {
            throw new NotExistShopException();
        }
        ShopDao.INSTANCE.deleteByUuid(shopMeta.getUuid());
        Gson gson = new Gson();
        Type stringListType = new TypeToken<ArrayList<String>>() {
        }.getType();
        List<String> stringList = gson.fromJson(shopMeta.getDescription(), stringListType);
        if (stringList == null || stringList.isEmpty()) {
            throw new NotExistLineException();
        }
        stringList.remove(stringList.size() - 1);
        shopMeta.setShopDescription(gson.toJson(stringList));
        ShopDao.INSTANCE.insert(shopMeta);
    }

    @Override
    public void updateShopDescription(String shopUuid, int lineIndex, String d) throws NotExistShopException, SQLException, NotExistLineException {
        ShopMeta shopMeta = ShopDao.INSTANCE.getByUuid(shopUuid);
        if (shopMeta == null) {
            throw new NotExistShopException();
        }
        ShopDao.INSTANCE.deleteByUuid(shopMeta.getUuid());

        Gson gson = new Gson();
        Type stringListType = new TypeToken<ArrayList<String>>() {
        }.getType();
        List<String> stringList = gson.fromJson(shopMeta.getDescription(), stringListType);
        if (stringList == null || stringList.size() < lineIndex + 1) {
            throw new NotExistLineException();
        }
        stringList.set(lineIndex, d);
        shopMeta.setShopDescription(gson.toJson(stringList));
        ShopDao.INSTANCE.insert(shopMeta);
    }

    @Override
    public void resetShopDescription(String shopUuid) throws NotExistShopException, SQLException {
        ShopMeta shopMeta = ShopDao.INSTANCE.getByUuid(shopUuid);
        if (shopMeta == null) {
            throw new NotExistShopException();
        }
        ShopDao.INSTANCE.deleteByUuid(shopMeta.getUuid());
        shopMeta.setShopDescription(null);
        ShopDao.INSTANCE.insert(shopMeta);
    }

    @Override
    public void setShopLocation(String shopUuid, String locationData) throws NotExistShopException, SQLException {
        ShopMeta shopMeta = ShopDao.INSTANCE.getByUuid(shopUuid);
        if (shopMeta == null) {
            throw new NotExistShopException();
        }
        ShopDao.INSTANCE.deleteByUuid(shopMeta.getUuid());
        shopMeta.setLocation(locationData);
        ShopDao.INSTANCE.insert(shopMeta);
    }

    @Override
    public void resetShopLocation(String shopUuid) throws NotExistShopException, SQLException {
        ShopMeta shopMeta = ShopDao.INSTANCE.getByUuid(shopUuid);
        if (shopMeta == null) {
            throw new NotExistShopException();
        }
        ShopDao.INSTANCE.deleteByUuid(shopMeta.getUuid());
        shopMeta.setLocation(null);
        ShopDao.INSTANCE.insert(shopMeta);
    }

    @Override
    public void collectShop(ShopCollectorMeta shopCollectorMeta) throws SQLException, DuplicateShopCollectorException, NotExistShopException {
        ShopMeta shopMeta = ShopDao.INSTANCE.getByUuid(shopCollectorMeta.getShopUuid());
        if (shopMeta == null) {
            throw new NotExistShopException();
        }
        ShopCollectorMeta shopCollectorMetaOld = ShopCollectorDao.INSTANCE.getByCollectorAndShopUuid(shopCollectorMeta.getCollectorUuid(),
                shopCollectorMeta.getShopUuid());
        if (shopCollectorMetaOld != null) {
            throw new DuplicateShopCollectorException();
        }
        ShopDao.INSTANCE.deleteByUuid(shopMeta.getUuid());
        shopMeta.setCollectAmount(shopMeta.getCollectAmount() + 1);
        ShopDao.INSTANCE.insert(shopMeta);
        ShopCollectorDao.INSTANCE.insert(shopCollectorMeta);
    }

    @Override
    public void cancelCollectShop(String playerUuid, String shopUuid) throws SQLException, NotExistShopCollectorException, NotExistShopException {
        ShopMeta shopMeta = ShopDao.INSTANCE.getByUuid(shopUuid);
        if (shopMeta == null) {
            throw new NotExistShopException();
        }
        ShopCollectorMeta shopCollectorMeta = ShopCollectorDao.INSTANCE.getByCollectorAndShopUuid(playerUuid, shopUuid);
        if (shopCollectorMeta == null) {
            throw new NotExistShopCollectorException();
        }
        ShopDao.INSTANCE.deleteByUuid(shopMeta.getUuid());
        shopMeta.setCollectAmount(shopMeta.getCollectAmount() - 1);
        ShopDao.INSTANCE.insert(shopMeta);
        ShopCollectorDao.INSTANCE.deleteByCollectorAndShopUuid(playerUuid, shopUuid);
    }

    @Override
    public void createShopComment(ShopCommentMeta shopCommentMeta) throws DuplicateShopCommenterException, SQLException {
        if (ShopCommentDao.INSTANCE.getByUuid(shopCommentMeta.getUuid()) != null) {
            throw new DuplicateShopCommenterException();
        }
        ShopCommentDao.INSTANCE.insert(shopCommentMeta);
    }

    @Override
    public void deleteShopComment(String commentUuid) throws SQLException, NotExistShopCommentException {
        if (ShopCommentDao.INSTANCE.getByUuid(commentUuid) == null) {
            throw new NotExistShopCommentException();
        }
        ShopCommentDao.INSTANCE.deleteByUuid(commentUuid);
    }

    @Override
    public @Nullable ShopCollectorMeta getShopCollector(String shopUuid, String collectorUuid) throws NotExistShopException, SQLException {
        ShopMeta shopMeta = ShopDao.INSTANCE.getByUuid(shopUuid);
        if (shopMeta == null) {
            throw new NotExistShopException();
        }
        return ShopCollectorDao.INSTANCE.getByCollectorAndShopUuid(collectorUuid, shopUuid);
    }

    @Override
    public ShopMeta getShop(String shopUuid) throws SQLException {
        return ShopDao.INSTANCE.getByUuid(shopUuid);

    }

    @Override
    public GoodMeta getGood(String goodUuid) throws SQLException {
        return GoodDao.INSTANCE.getByUuid(goodUuid);
    }


    @Override
    public void buyIcon(String playerUuid, String iconUuid, IconMeta oldIconMeta) throws SQLException, NotExistIconException, NotMoreIconException, StateChangeException, DuplicateIconOwnerException {
        IconOwnerMeta iconOwnerMeta = new IconOwnerMeta(iconUuid, playerUuid);
        IconMeta iconMeta = IconDao.INSTANCE.getByUuid(iconUuid);
        if (iconMeta == null) {
            throw new NotExistIconException();
        }
        if (!iconMeta.equals(oldIconMeta)) {
            throw new StateChangeException();
        }

        if (!iconMeta.isSystem()) {
            if (iconMeta.getStock() <= 0) {
                throw new NotMoreIconException();
            }
        }
        if (IconOwnerDao.INSTANCE.getByIconUuidAndOwnerUuid(iconOwnerMeta.getIconUuid(), iconOwnerMeta.getOwnerUuid()) != null) {
            throw new DuplicateIconOwnerException();
        }
        if (!iconMeta.isSystem()) {
            iconMeta.setStock(iconMeta.getStock() - 1);
            IconDao.INSTANCE.deleteByUuid(iconMeta.getUuid());
            IconDao.INSTANCE.insert(iconMeta);
        }
        IconOwnerDao.INSTANCE.insert(iconOwnerMeta);
    }


    @Override
    public void useShopIcon(String iconUuid, String shopUuid) throws NotExistIconException, SQLException, NotExistShopException {
        IconMeta iconMeta = IconDao.INSTANCE.getByUuid(iconUuid);
        if (iconMeta == null) {
            throw new NotExistIconException();
        }
        ShopMeta shopMeta = ShopDao.INSTANCE.getByUuid(shopUuid);
        if (shopMeta == null) {
            throw new NotExistShopException();
        }
        shopMeta.setIconUuid(iconUuid);
        ShopDao.INSTANCE.deleteByUuid(shopUuid);
        ShopDao.INSTANCE.insert(shopMeta);

    }

    @Override
    public void resetShopIcon(String shopUuid) throws NotExistShopException, SQLException {

        ShopMeta shopMeta = ShopDao.INSTANCE.getByUuid(shopUuid);
        if (shopMeta == null) {
            throw new NotExistShopException();
        }
        shopMeta.setIconUuid(null);
        ShopDao.INSTANCE.deleteByUuid(shopUuid);
        ShopDao.INSTANCE.insert(shopMeta);
    }

    @Override
    public IconMeta getIcon(String iconUuid) throws SQLException {
        return IconDao.INSTANCE.getByUuid(iconUuid);
    }

    @Override
    public TradeRecordMeta getTradeRecord(String uuid) throws SQLException {
        return TradeRecordDao.INSTANCE.getByUuid(uuid);
    }

    @Override
    public void createGood(GoodMeta goodMeta) throws SQLException, DuplicateGoodException {

        if (GoodDao.INSTANCE.getByUuid(goodMeta.getUuid()) != null || GoodDao.INSTANCE.getByNameAndShopUuid(goodMeta.getName(), goodMeta.getShopUuid()) != null) {
            throw new DuplicateGoodException();
        }
        GoodDao.INSTANCE.insert(goodMeta);
    }

    @Override
    public void deleteGood(String goodName, String shopUuid) throws NotExistGoodException, GoodNotEmptyException, SQLException {
        GoodMeta goodMeta = GoodDao.INSTANCE.getByNameAndShopUuid(goodName, shopUuid);
        if (goodMeta == null) {
            throw new NotExistGoodException();
        }
        if (goodMeta.getStock() != 0) {
            throw new GoodNotEmptyException();
        }
        GoodDao.INSTANCE.deleteByUuid(goodMeta.getUuid());
    }

    @Override
    public void setGoodPlayerPointsPrice(String goodUuid, int price) throws NotExistGoodException, SQLException {
        GoodMeta goodMeta = GoodDao.INSTANCE.getByUuid(goodUuid);
        if (goodMeta == null) {
            throw new NotExistGoodException();
        }
        goodMeta.setPlayerPointsPrice(price);
        goodMeta.setVaultPrice(null);
        goodMeta.setExperiencePrice(null);
        goodMeta.setItemPrice(null);
        goodMeta.setCurrencyItemStack(null);
        GoodDao.INSTANCE.deleteByUuid(goodMeta.getUuid());
        GoodDao.INSTANCE.insert(goodMeta);
    }

    @Override
    public void setGoodItemPrice(String goodUuid, int price, String currencyData) throws SQLException, NotExistGoodException {
        GoodMeta goodMeta = GoodDao.INSTANCE.getByUuid(goodUuid);
        if (goodMeta == null) {
            throw new NotExistGoodException();
        }
        goodMeta.setItemPrice(price);
        goodMeta.setCurrencyItemStack(currencyData);
        goodMeta.setPlayerPointsPrice(null);
        goodMeta.setVaultPrice(null);
        goodMeta.setExperiencePrice(null);
        GoodDao.INSTANCE.deleteByUuid(goodMeta.getUuid());
        GoodDao.INSTANCE.insert(goodMeta);
    }

    @Override
    public void setGoodVaultPrice(String goodUuid, double price) throws NotExistGoodException, SQLException {
        GoodMeta goodMeta = GoodDao.INSTANCE.getByUuid(goodUuid);
        if (goodMeta == null) {
            throw new NotExistGoodException();
        }
        goodMeta.setVaultPrice(price);
        goodMeta.setPlayerPointsPrice(null);
        goodMeta.setExperiencePrice(null);
        goodMeta.setItemPrice(null);
        goodMeta.setCurrencyItemStack(null);
        GoodDao.INSTANCE.deleteByUuid(goodMeta.getUuid());
        GoodDao.INSTANCE.insert(goodMeta);
    }

    @Override
    public void setGoodLimitTime(String goodUuid, Integer time) throws NotExistGoodException, SQLException {
        GoodMeta goodMeta = GoodDao.INSTANCE.getByUuid(goodUuid);
        if (goodMeta == null) {
            throw new NotExistGoodException();
        }
        goodMeta.setLimitTime(time);
        GoodDao.INSTANCE.deleteByUuid(goodMeta.getUuid());
        GoodDao.INSTANCE.insert(goodMeta);
    }

    @Override
    public void depositItemStock(String playerUuid, String itemStack, int amount) throws SQLException {
        ItemStockMeta itemStockMeta = ItemStockDao.INSTANCE.getByPlayerUuidAndItemStack(playerUuid, itemStack);
        if (itemStockMeta == null) {
            itemStockMeta = new ItemStockMeta(playerUuid, itemStack, 0);
            itemStockMeta.setAmount(itemStockMeta.getAmount() + amount);
            ItemStockDao.INSTANCE.insert(itemStockMeta);
            return;
        }
        if (amount > Integer.MAX_VALUE - itemStockMeta.getAmount()) {
            itemStockMeta.setAmount(Integer.MAX_VALUE);
        } else {
            itemStockMeta.setAmount(itemStockMeta.getAmount() + amount);
        }
        ItemStockDao.INSTANCE.deleteByPlayerUuidAndItemStack(playerUuid, itemStack);
        ItemStockDao.INSTANCE.insert(itemStockMeta);
    }

    @Override
    public void takeItemStock(String playerUuid, String itemStack, int amount) throws SQLException, NotMoreItemStockException, NotExistItemStockException {
        ItemStockMeta itemStockMeta = ItemStockDao.INSTANCE.getByPlayerUuidAndItemStack(playerUuid, itemStack);
        if (itemStockMeta == null) {
            throw new NotExistItemStockException();
        }
        if (itemStockMeta.getAmount() - amount < 0) {
            throw new NotMoreItemStockException();
        }
        itemStockMeta.setAmount(itemStockMeta.getAmount() - amount);
        ItemStockDao.INSTANCE.deleteByPlayerUuidAndItemStack(playerUuid, itemStack);
        ItemStockDao.INSTANCE.insert(itemStockMeta);
    }

    @Override
    public void createItemStock(ItemStockMeta itemStockMeta0) throws DuplicateItemStockException, SQLException {
        ItemStockMeta itemStockMeta = ItemStockDao.INSTANCE.getByPlayerUuidAndItemStack(itemStockMeta0.getPlayerUuid(), itemStockMeta0.getItemStack());
        if (itemStockMeta != null) {
            throw new DuplicateItemStockException();
        }
        ItemStockDao.INSTANCE.insert(itemStockMeta0);
    }

    @Override
    public void deleteItemStock(String playerUuid, String itemStack) throws ItemStockNotEmptyException, NotExistItemStockException, SQLException {
        ItemStockMeta itemStockMeta = ItemStockDao.INSTANCE.getByPlayerUuidAndItemStack(playerUuid, itemStack);
        if (itemStockMeta == null) {
            throw new NotExistItemStockException();
        }
        if (itemStockMeta.getAmount() != 0) {
            throw new ItemStockNotEmptyException();
        }
        ItemStockDao.INSTANCE.deleteByPlayerUuidAndItemStack(playerUuid, itemStack);
    }

    @Override
    public ItemStockMeta getItemStock(String playerUuid, String itemStack) throws SQLException {
        return ItemStockDao.INSTANCE.getByPlayerUuidAndItemStack(playerUuid, itemStack);

    }

    @Override
    public void changeGoodTransactionType(String goodUuid) throws NotExistGoodException, SQLException {
        GoodMeta goodMeta = GoodDao.INSTANCE.getByUuid(goodUuid);
        if (goodMeta == null) {
            throw new NotExistGoodException();
        }
        if (goodMeta.getType().equalsIgnoreCase("收购")) {
            goodMeta.setType("出售");
        } else if (goodMeta.getType().equalsIgnoreCase("出售")) {
            goodMeta.setType("收购");
        }
        GoodDao.INSTANCE.deleteByUuid(goodMeta.getUuid());
        GoodDao.INSTANCE.insert(goodMeta);
    }

    @Override
    public void trade(String goodUuid, int amount, GoodMeta old) throws SQLException, NotExistGoodException, NotMoreGoodException, StateChangeException {
        GoodMeta goodMeta = GoodDao.INSTANCE.getByUuid(goodUuid);
        if (goodMeta == null) {
            throw new NotExistGoodException();
        }
        if (!old.equals(goodMeta)) {
            throw new StateChangeException();
        }
        if (goodMeta.isSystem()) {
            return;
        }


        if (goodMeta.getType().equalsIgnoreCase("收购")) {
            if (amount > Integer.MAX_VALUE - goodMeta.getStock()) {
                goodMeta.setStock(Integer.MAX_VALUE);
            } else {
                goodMeta.setStock(goodMeta.getStock() + amount);
            }

        } else if (goodMeta.getType().equalsIgnoreCase("出售")) {
            if (goodMeta.getStock() - amount < 0) {
                throw new NotMoreGoodException();
            }
            goodMeta.setStock(goodMeta.getStock() - amount);
        }
        GoodDao.INSTANCE.deleteByUuid(goodMeta.getUuid());
        GoodDao.INSTANCE.insert(goodMeta);
    }


    @Override
    public void createTradeRecord(TradeRecordMeta tradeRecordMeta) throws DuplicateTradeRecordException, SQLException {
        if (TradeRecordDao.INSTANCE.getByUuid(tradeRecordMeta.getUuid()) != null) {
            throw new DuplicateTradeRecordException();
        }
        TradeRecordDao.INSTANCE.insert(tradeRecordMeta);
    }

    @Override
    public void setGoodName(String goodUuid, String name) throws NotExistGoodException, SQLException, DuplicateGoodException {
        GoodMeta goodMeta = GoodDao.INSTANCE.getByUuid(goodUuid);
        if (goodMeta == null) {
            throw new NotExistGoodException();
        }
        if (GoodDao.INSTANCE.getByNameAndShopUuid(name, goodMeta.getShopUuid()) != null) {
            throw new DuplicateGoodException();
        }
        goodMeta.setName(name);
        GoodDao.INSTANCE.deleteByUuid(goodMeta.getUuid());
        GoodDao.INSTANCE.insert(goodMeta);
    }

    @Override
    public void setShopName(String shopUuid, String name) throws NotExistShopException, SQLException, DuplicateShopException {
        ShopMeta shopMeta = ShopDao.INSTANCE.getByUuid(shopUuid);
        if (shopMeta == null) {
            throw new NotExistShopException();
        }
        if (ShopDao.INSTANCE.getByName(name) != null) {
            throw new DuplicateShopException();
        }
        shopMeta.setName(name);
        ShopDao.INSTANCE.deleteByUuid(shopMeta.getUuid());
        ShopDao.INSTANCE.insert(shopMeta);
    }


    @Override
    public void depositGood(String goodUuid, int amount) throws SQLException, NotExistGoodException {
        GoodMeta goodMeta = GoodDao.INSTANCE.getByUuid(goodUuid);
        if (goodMeta == null) {
            throw new NotExistGoodException();
        }
        if (amount > Integer.MAX_VALUE - goodMeta.getStock()) {
            goodMeta.setStock(Integer.MAX_VALUE);
        } else {
            goodMeta.setStock(goodMeta.getStock() + amount);
        }
        GoodDao.INSTANCE.deleteByUuid(goodMeta.getUuid());
        GoodDao.INSTANCE.insert(goodMeta);
    }

    @Override
    public void takeGood(String goodUuid, int amount) throws NotMoreGoodException, NotExistGoodException, SQLException {
        GoodMeta goodMeta = GoodDao.INSTANCE.getByUuid(goodUuid);
        if (goodMeta == null) {
            throw new NotExistGoodException();
        }
        if (goodMeta.getStock() - amount < 0) {
            throw new NotMoreGoodException();
        }
        goodMeta.setStock(goodMeta.getStock() - amount);
        GoodDao.INSTANCE.deleteByUuid(goodMeta.getUuid());
        GoodDao.INSTANCE.insert(goodMeta);
    }


    @Override
    public boolean hasItemStock(String playerUuid, String itemStack, int amount) throws SQLException {
        ItemStockMeta itemStockMeta = ItemStockDao.INSTANCE.getByPlayerUuidAndItemStack(playerUuid, itemStack);
        if (itemStockMeta == null) {
            return false;
        }
        return itemStockMeta.getAmount() >= amount;
    }

    @Override
    public void resetGoodPrice(String goodUuid) throws NotExistGoodException, SQLException {
        GoodMeta goodMeta = GoodDao.INSTANCE.getByUuid(goodUuid);
        if (goodMeta == null) {
            throw new NotExistGoodException();
        }
        goodMeta.setPlayerPointsPrice(null);
        goodMeta.setCurrencyItemStack(null);
        goodMeta.setVaultPrice(null);
        goodMeta.setItemPrice(null);
        GoodDao.INSTANCE.deleteByUuid(goodMeta.getUuid());
        GoodDao.INSTANCE.insert(goodMeta);
    }

    @Override
    public void setShopDescription(String shopUuid, String descriptionJson) throws SQLException, NotExistShopException {
        ShopMeta shopMeta = ShopDao.INSTANCE.getByUuid(shopUuid);
        if (shopMeta == null) {
            throw new NotExistShopException();
        }
        ShopDao.INSTANCE.deleteByUuid(shopMeta.getUuid());
        shopMeta.setShopDescription(descriptionJson);
        ShopDao.INSTANCE.insert(shopMeta);
    }


}
