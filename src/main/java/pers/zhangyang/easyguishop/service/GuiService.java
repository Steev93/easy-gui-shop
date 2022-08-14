package pers.zhangyang.easyguishop.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easyguishop.exception.*;
import pers.zhangyang.easyguishop.meta.*;

import java.util.List;

public interface GuiService {
    List<ShopMeta> listShop();

    List<ShopMeta> listPlayerShop(String ownerUuid);

    List<ShopMeta> listPlayerCollectedShop(String playerUuid);

    List<ShopCommentMeta> listPlayerComment(String playerUuid);

    List<ShopCommentMeta> listShopComment(String shopUuid) throws NotExistShopException;

    List<IconMeta> listIcon();

    List<IconMeta> listPlayerIcon(String ownerUuid);

    List<GoodMeta> listShopGood(String shopUuid) throws NotExistShopException;

    List<ItemStockMeta> listPlayerItemStock(String playerUuid);

    List<TradeRecordMeta> listPlayerTradeRecord(String playerUuid);

    void createShop(@NotNull ShopMeta shopMeta) throws DuplicateShopException;

    void viewShop(@NotNull String shopUuid, int amount) throws NotExistShopException;

    void deleteShop(String shopUuid) throws NotExistShopException, ShopNotEmptyException;

    void addShopDescription(String shopUuid, String d) throws NotExistShopException;

    void removeShopDescription(String shopUuid) throws NotExistShopException, NotExistLineException;

    void updateShopDescription(String shopUuid, int line, String d) throws NotExistShopException, NotExistLineException;

    void resetShopDescription(String shopUuid) throws NotExistShopException;

    void setShopLocation(String shopUuid, String locationData) throws NotExistShopException;

    void resetShopLocation(String shopUuid) throws NotExistShopException;

    void collectShop(ShopCollectorMeta shopCollectorMeta) throws NotExistShopException, DuplicateShopCollectorException;

    void cancelCollectShop(String playerUuid, String shopUuid) throws NotExistShopException, NotExistShopCollectorException;

    void createShopComment(ShopCommentMeta shopCommentMeta) throws DuplicateShopCommenterException;

    void deleteShopComment(String commentUuid) throws NotExistShopCommentException;

    @Nullable
    ShopCollectorMeta getShopCollector(String shopUuid, String collectorUuid) throws NotExistShopException;

    @Nullable
    ShopMeta getShop(String shopUuid);

    GoodMeta getGood(String goodUuid);

    void buyIcon(String playerUuid, String iconUuid, IconMeta oldIconMeta) throws NotExistIconException, NotMoreIconException, StateChangeException, DuplicateIconOwnerException;

    void buyIconItem(String playerUuid, String iconUuid, IconMeta oldIconMeta) throws NotExistIconException, NotMoreIconException, StateChangeException, DuplicateIconOwnerException, NotEnoughItemStockException;

    void useShopIcon(String iconUuid, String shopUuid) throws NotExistIconException, NotExistShopException;

    void resetShopIcon(String shopUuid) throws NotExistShopException;

    IconMeta getIcon(String iconUuid);

    TradeRecordMeta getTradeRecord(String uuid);

    void createGood(GoodMeta goodMeta) throws DuplicateGoodException;

    void deleteGood(String goodName, String shopUuid) throws NotExistGoodException, GoodNotEmptyException;

    void setGoodPlayerPointsPrice(String goodUuid, int price) throws NotExistGoodException;

    void setGoodItemPrice(String goodUuid, int price, String currencyData) throws NotExistGoodException;

    void setGoodVaultPrice(String goodUuid, double price) throws NotExistGoodException;

    void setGoodLimitTime(String goodUuid, Integer time) throws NotExistGoodException;

    void depositItemStock(String playerUuid, String itemStack, int amount);

    void takeItemStock(String playerUuid, String itemStack, int amount) throws NotMoreItemStockException, NotExistItemStockException;

    void createItemStock(ItemStockMeta itemStockMeta) throws DuplicateItemStockException;

    void deleteItemStock(String playerUuid, String itemStack) throws ItemStockNotEmptyException, NotExistItemStockException;

    ItemStockMeta getItemStock(String playerUuid, String itemStack);

    void changeGoodTransactionType(String goodUuid) throws NotExistGoodException;

    void trade(String goodUuid, int amount, GoodMeta old) throws NotExistGoodException, NotMoreGoodException, StateChangeException;

    void tradeItem(String goodUuid, int amount, GoodMeta old, String merchantUuid, String customUuid) throws NotExistGoodException, NotMoreGoodException, StateChangeException, NotMoreItemStockException, NotEnoughItemStockException;

    void createTradeRecord(TradeRecordMeta tradeRecordMeta) throws DuplicateTradeRecordException;

    void setGoodName(String goodUuid, String name) throws NotExistGoodException, DuplicateGoodException;

    void setShopName(String shopUuid, String name) throws NotExistShopException, DuplicateShopException;

    void depositGood(String goodUuid, int amount) throws NotExistGoodException;

    void takeGood(String goodUuid, int amount) throws NotMoreGoodException, NotExistGoodException;

    boolean hasItemStock(String playerUuid, String itemStack, int amount);

    void resetGoodPrice(String goodUuid) throws NotExistGoodException;

    void setShopDescription(String shopUuid, String descriptionJson) throws NotExistShopException;
}
