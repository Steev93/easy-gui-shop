package pers.zhangyang.easyguishop.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easyguishop.exception.*;
import pers.zhangyang.easyguishop.meta.*;

import java.sql.SQLException;
import java.util.List;

public interface GuiService {
    List<ShopMeta> listShop() throws SQLException;

    List<ShopMeta> listPlayerShop(String ownerUuid) throws SQLException;

    List<ShopMeta> listPlayerCollectedShop(String playerUuid) throws SQLException;

    List<ShopCommentMeta> listPlayerComment(String playerUuid) throws SQLException;

    List<ShopCommentMeta> listShopComment(String shopUuid) throws SQLException, NotExistShopException;

    List<IconMeta> listIcon() throws SQLException;

    List<IconMeta> listPlayerIcon(String ownerUuid) throws SQLException;

    List<GoodMeta> listShopGood(String shopUuid) throws SQLException, NotExistShopException;

    List<ItemStockMeta> listPlayerItemStock(String playerUuid) throws SQLException;

    List<TradeRecordMeta> listPlayerTradeRecord(String playerUuid) throws SQLException;

    void createShop(@NotNull ShopMeta shopMeta) throws DuplicateShopException, SQLException;

    void viewShop(@NotNull String shopUuid, int amount) throws NotExistShopException, SQLException;

    void deleteShop(String shopUuid) throws NotExistShopException, ShopNotEmptyException, SQLException;

    void addShopDescription(String shopUuid, String d) throws NotExistShopException, SQLException;

    void removeShopDescription(String shopUuid) throws NotExistShopException, SQLException, NotExistLineException;

    void updateShopDescription(String shopUuid, int line, String d) throws NotExistShopException, SQLException, NotExistLineException;

    void resetShopDescription(String shopUuid) throws NotExistShopException, SQLException;

    void setShopLocation(String shopUuid, String locationData) throws NotExistShopException, SQLException;

    void resetShopLocation(String shopUuid) throws NotExistShopException, SQLException;

    void collectShop(ShopCollectorMeta shopCollectorMeta) throws NotExistShopException, SQLException, DuplicateShopCollectorException;

    void cancelCollectShop(String playerUuid, String shopUuid) throws NotExistShopException, SQLException, NotExistShopCollectorException;

    void createShopComment(ShopCommentMeta shopCommentMeta) throws DuplicateShopCommenterException, SQLException;

    void deleteShopComment(String commentUuid) throws SQLException, NotExistShopCommentException;

    @Nullable
    ShopCollectorMeta getShopCollector(String shopUuid, String collectorUuid) throws NotExistShopException, SQLException;

    @Nullable
    ShopMeta getShop(String shopUuid) throws SQLException;

    GoodMeta getGood(String goodUuid) throws SQLException;

    void buyIcon(String playerUuid, String iconUuid, IconMeta oldIconMeta) throws SQLException, NotExistIconException, NotMoreIconException, StateChangeException, DuplicateIconOwnerException;

    void useShopIcon(String iconUuid, String shopUuid) throws NotExistIconException, SQLException, NotExistShopException;

    void resetShopIcon(String shopUuid) throws NotExistShopException, SQLException;

    IconMeta getIcon(String iconUuid) throws SQLException;

    TradeRecordMeta getTradeRecord(String uuid) throws SQLException;

    void createGood(GoodMeta goodMeta) throws SQLException, DuplicateGoodException;

    void deleteGood(String goodName, String shopUuid) throws NotExistGoodException, GoodNotEmptyException, SQLException;

    void setGoodPlayerPointsPrice(String goodUuid, int price) throws NotExistGoodException, SQLException;

    void setGoodItemPrice(String goodUuid, int price, String currencyData) throws SQLException, NotExistGoodException;

    void setGoodVaultPrice(String goodUuid, double price) throws NotExistGoodException, SQLException;

    void setGoodLimitTime(String goodUuid, Integer time) throws NotExistGoodException, SQLException;

    void depositItemStock(String playerUuid, String itemStack, int amount) throws SQLException;

    void takeItemStock(String playerUuid, String itemStack, int amount) throws SQLException, NotMoreItemStockException, NotExistItemStockException;

    void createItemStock(ItemStockMeta itemStockMeta) throws DuplicateItemStockException, SQLException;

    void deleteItemStock(String playerUuid, String itemStack) throws ItemStockNotEmptyException, NotExistItemStockException, SQLException;

    ItemStockMeta getItemStock(String playerUuid, String itemStack) throws SQLException;

    void changeGoodTransactionType(String goodUuid) throws NotExistGoodException, SQLException;

    void trade(String goodUuid, int amount, GoodMeta old) throws SQLException, NotExistGoodException, NotMoreGoodException, StateChangeException;

    void createTradeRecord(TradeRecordMeta tradeRecordMeta) throws DuplicateTradeRecordException, SQLException;

    void setGoodName(String goodUuid, String name) throws NotExistGoodException, SQLException, DuplicateGoodException;

    void setShopName(String shopUuid, String name) throws NotExistShopException, SQLException, DuplicateShopException;

    void depositGood(String goodUuid, int amount) throws SQLException, NotExistGoodException;

    void takeGood(String goodUuid, int amount) throws NotMoreGoodException, NotExistGoodException, SQLException;

    boolean hasItemStock(String playerUuid, String itemStack, int amount) throws SQLException;

    void resetGoodPrice(String goodUuid) throws NotExistGoodException, SQLException;

    void setShopDescription(String shopUuid,String descriptionJson) throws SQLException, NotExistShopException;
}
