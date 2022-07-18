package pers.zhangyang.easyguishop.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easyguishop.EasyGuiShop;
import pers.zhangyang.easyguishop.dao.*;
import pers.zhangyang.easyguishop.exception.*;
import pers.zhangyang.easyguishop.meta.*;
import pers.zhangyang.easyguishop.service.CommandService;
import pers.zhangyang.easyguishop.util.ItemStackUtil;
import pers.zhangyang.easyguishop.util.LocationUtil;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommandServiceImpl implements CommandService {
    public static final CommandServiceImpl INSTANCE = new CommandServiceImpl();

    @Override
    public void createIcon(IconMeta iconMeta) throws SQLException, DuplicateIconException {
        if (IconDao.INSTANCE.getByUuid(iconMeta.getUuid()) != null || IconDao.INSTANCE.getByName(iconMeta.getName()) != null) {
            throw new DuplicateIconException();
        }

        IconDao.INSTANCE.insert(iconMeta);
    }

    @Override
    public void deleteIcon(String iconName) throws NotExistIconException, SQLException {
        IconMeta iconMeta = IconDao.INSTANCE.getByName(iconName);
        if (iconMeta == null) {
            throw new NotExistIconException();
        }
        List<ShopMeta> shopMetaList = ShopDao.INSTANCE.listByIconUuid(iconMeta.getUuid());
        for (ShopMeta shopMeta : shopMetaList) {
            shopMeta.setIconUuid(null);
            ShopDao.INSTANCE.deleteByUuid(shopMeta.getUuid());
            ShopDao.INSTANCE.insert(shopMeta);
        }
        IconOwnerDao.INSTANCE.deleteByIconUuid(iconMeta.getUuid());
        IconDao.INSTANCE.deleteByUuid(iconMeta.getUuid());
    }

    @Override
    public void plusShopPopularity(String shopName, int amount) throws NotExistShopException, SQLException {
        if (amount < 0) {
            throw new IllegalArgumentException();
        }
        ShopMeta shopMeta = ShopDao.INSTANCE.getByName(shopName);
        if (shopMeta == null) {
            throw new NotExistShopException();
        }
        if (Integer.MAX_VALUE - shopMeta.getPopularity() >= amount) {
            shopMeta.setPopularity(shopMeta.getPopularity() + amount);
        } else {
            shopMeta.setPopularity(Integer.MAX_VALUE);
        }
        ShopDao.INSTANCE.deleteByUuid(shopMeta.getUuid());
        ShopDao.INSTANCE.insert(shopMeta);
    }

    @Override
    public void subtractShopPopularity(String shopName, int amount) throws NotExistShopException, SQLException, NotMorePopularityException {
        if (amount < 0) {
            throw new IllegalArgumentException();
        }
        ShopMeta shopMeta = ShopDao.INSTANCE.getByName(shopName);
        if (shopMeta == null) {
            throw new NotExistShopException();
        }
        if ((shopMeta.getPopularity() - amount) < 0) {
            throw new NotMorePopularityException();
        }
        shopMeta.setPopularity(shopMeta.getPopularity() - amount);
        ShopDao.INSTANCE.deleteByUuid(shopMeta.getUuid());
        ShopDao.INSTANCE.insert(shopMeta);
    }

    @Override
    public void setIconPlayerPointsAndPrice(String iconName, int price) throws NotExistIconException, SQLException {
        IconMeta iconMeta = IconDao.INSTANCE.getByName(iconName);
        if (iconMeta == null) {
            throw new NotExistIconException();
        }
        iconMeta.setPlayerPointsPrice(price);
        iconMeta.setVaultPrice(null);
        iconMeta.setItemPrice(null);
        iconMeta.setCurrencyItemStack(null);

        IconDao.INSTANCE.deleteByUuid(iconMeta.getUuid());
        IconDao.INSTANCE.insert(iconMeta);
    }


    @Override
    public void setIconItemPrice(String iconName, int price, String currencyData) throws NotExistIconException, SQLException {
        IconMeta iconMeta = IconDao.INSTANCE.getByName(iconName);
        if (iconMeta == null) {
            throw new NotExistIconException();
        }
        iconMeta.setItemPrice(price);
        iconMeta.setPlayerPointsPrice(null);
        iconMeta.setVaultPrice(null);
        iconMeta.setCurrencyItemStack(currencyData);
        IconDao.INSTANCE.deleteByUuid(iconMeta.getUuid());
        IconDao.INSTANCE.insert(iconMeta);
    }

    @Override
    public void setIconVaultPrice(String iconName, double price) throws NotExistIconException, SQLException {
        IconMeta iconMeta = IconDao.INSTANCE.getByName(iconName);
        if (iconMeta == null) {
            throw new NotExistIconException();
        }
        iconMeta.setVaultPrice(price);
        iconMeta.setPlayerPointsPrice(null);
        iconMeta.setItemPrice(null);
        iconMeta.setCurrencyItemStack(null);
        IconDao.INSTANCE.deleteByUuid(iconMeta.getUuid());
        IconDao.INSTANCE.insert(iconMeta);
    }

    @Override
    public void setIconLimitTime(String iconName, Integer time) throws NotExistIconException, SQLException {
        IconMeta iconMeta = IconDao.INSTANCE.getByName(iconName);
        if (iconMeta == null) {
            throw new NotExistIconException();
        }
        iconMeta.setLimitTime(time);
        IconDao.INSTANCE.deleteByUuid(iconMeta.getUuid());
        IconDao.INSTANCE.insert(iconMeta);
    }

    @Override
    public void setIconName(String oldName, String name) throws NotExistIconException, SQLException, DuplicateIconException {
        IconMeta iconMeta = IconDao.INSTANCE.getByName(oldName);
        if (iconMeta == null) {
            throw new NotExistIconException();
        }
        if (IconDao.INSTANCE.getByName(name) != null) {
            throw new DuplicateIconException();
        }
        iconMeta.setName(name);
        IconDao.INSTANCE.deleteByUuid(iconMeta.getUuid());
        IconDao.INSTANCE.insert(iconMeta);
    }

    @Override
    public void setIconStock(String iconName, int amount) throws NotExistIconException, SQLException {
        IconMeta iconMeta = IconDao.INSTANCE.getByName(iconName);
        if (iconMeta == null) {
            throw new NotExistIconException();
        }
        iconMeta.setStock(amount);
        IconDao.INSTANCE.deleteByUuid(iconMeta.getUuid());
        IconDao.INSTANCE.insert(iconMeta);
    }

    @Override
    public void setIconSystem(String iconName, boolean system) throws SQLException, NotExistIconException {
        IconMeta iconMeta = IconDao.INSTANCE.getByName(iconName);
        if (iconMeta == null) {
            throw new NotExistIconException();
        }
        iconMeta.setSystem(system);
        IconDao.INSTANCE.deleteByUuid(iconMeta.getUuid());
        IconDao.INSTANCE.insert(iconMeta);
    }

    @Override
    public void setGoodSystem(String shopName, String goodName, boolean system) throws NotExistShopException, SQLException, NotExistGoodException {
        ShopMeta shopMeta = ShopDao.INSTANCE.getByName(shopName);
        if (shopMeta == null) {
            throw new NotExistShopException();
        }
        GoodMeta goodMeta = GoodDao.INSTANCE.getByNameAndShopUuid(goodName, shopMeta.getUuid());
        if (goodMeta == null) {
            throw new NotExistGoodException();
        }
        goodMeta.setSystem(system);
        GoodDao.INSTANCE.deleteByUuid(goodMeta.getUuid());
        GoodDao.INSTANCE.insert(goodMeta);
    }

    @Override
    public void setShopSystem(String shopName, boolean system) throws SQLException, NotExistShopException {
        ShopMeta shopMeta = ShopDao.INSTANCE.getByName(shopName);
        if (shopMeta == null) {
            throw new NotExistShopException();
        }
        List<GoodMeta> goodMetaList = GoodDao.INSTANCE.listByShopUuid(shopMeta.getUuid());
        for (GoodMeta g : goodMetaList) {
            g.setSystem(system);
            GoodDao.INSTANCE.deleteByUuid(g.getUuid());
            GoodDao.INSTANCE.insert(g);
        }
    }

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
    public void correctDatabase() throws SQLException {
        //修理Shop表的序列化数据
        List<ShopMeta> shopMetaList = ShopDao.INSTANCE.list();
        for (ShopMeta s : shopMetaList) {
            Gson gson = new Gson();
            Type stringListType = new TypeToken<ArrayList<String>>() {
            }.getType();
            try {
                gson.fromJson(s.getDescription(), stringListType);
            } catch (JsonSyntaxException e) {
                ShopDao.INSTANCE.deleteByUuid(s.getUuid());
                s.setShopDescription(null);
                ShopDao.INSTANCE.insert(s);
            }
            try {
                String data = s.getLocation();
                if (data != null) {
                    LocationUtil.deserializeLocation(data);
                }
            } catch (Exception e) {
                ShopDao.INSTANCE.deleteByUuid(s.getUuid());
                s.setLocation(null);
                ShopDao.INSTANCE.insert(s);
            }
        }

        //修理Good表的序列化数据
        List<GoodMeta> goodMetaList = GoodDao.INSTANCE.list();
        for (GoodMeta s : goodMetaList) {

            try {
                String data = s.getCurrencyItemStack();
                if (data != null) {
                    ItemStack itemStack = ItemStackUtil.itemStackDeserialize(data);
                    if (itemStack.getAmount() != 1) {
                        itemStack.setAmount(1);
                        s.setCurrencyItemStack(ItemStackUtil.itemStackSerialize(itemStack));
                        GoodDao.INSTANCE.deleteByUuid(s.getUuid());
                        GoodDao.INSTANCE.insert(s);
                    }
                }
            } catch (Exception e) {
                GoodDao.INSTANCE.deleteByUuid(s.getUuid());
                s.setCurrencyItemStack(null);
                s.setItemPrice(null);
                GoodDao.INSTANCE.insert(s);
            }
            try {
                String data = s.getGoodItemStack();
                ItemStack itemStack = ItemStackUtil.itemStackDeserialize(data);
                if (itemStack.getAmount() != 1) {
                    itemStack.setAmount(1);
                    s.setGoodItemStack(ItemStackUtil.itemStackSerialize(itemStack));
                    GoodDao.INSTANCE.deleteByUuid(s.getUuid());
                    GoodDao.INSTANCE.insert(s);
                }

            } catch (Exception e) {
                GoodDao.INSTANCE.deleteByUuid(s.getUuid());
            }
        }

        //修理Icon表的序列化数据
        List<IconMeta> iconMetaList = IconDao.INSTANCE.list();
        for (IconMeta s : iconMetaList) {

            try {

                String data = s.getCurrencyItemStack();
                if (data != null) {
                    //如果货币不是物品，设为null
                    ItemStack itemStack = ItemStackUtil.itemStackDeserialize(data);
                    //如果货币不是1，设置1
                    if (itemStack.getAmount() != 1) {
                        itemStack.setAmount(1);
                        s.setCurrencyItemStack(ItemStackUtil.itemStackSerialize(itemStack));
                    }
                }
            } catch (Exception e) {
                IconDao.INSTANCE.deleteByUuid(s.getUuid());
                s.setCurrencyItemStack(null);
                s.setItemPrice(null);
                IconDao.INSTANCE.insert(s);
            }
            try {
                //检查是不是物品，不是物品删掉
                ItemStackUtil.itemStackDeserialize(s.getIconItemStack());
            } catch (Exception e) {
                IconDao.INSTANCE.deleteByUuid(s.getUuid());
            }
        }

        //修理ItemStock表的序列化数据
        List<ItemStockMeta> itemStockMetaList = ItemStockDao.INSTANCE.list();
        for (ItemStockMeta s : itemStockMetaList) {

            try {
                //检查是否未物品，不是物品删掉
                ItemStack itemStack = ItemStackUtil.itemStackDeserialize(s.getItemStack());
                //检查数量是否未1，否则设置为1
                if (itemStack.getAmount() != 1) {
                    itemStack.setAmount(1);
                    s.setItemStack(ItemStackUtil.itemStackSerialize(itemStack));
                    ItemStockDao.INSTANCE.deleteByPlayerUuidAndItemStack(s.getPlayerUuid(), s.getItemStack());
                    ItemStockDao.INSTANCE.insert(s);
                }
            } catch (Exception e) {
                ItemStockDao.INSTANCE.deleteByPlayerUuidAndItemStack(s.getPlayerUuid(), s.getItemStack());
            }
        }


        //修理TradeRecord表的序列化数据
        List<TradeRecordMeta> tradeRecordMetaList = TradeRecordDao.INSTANCE.list();
        for (TradeRecordMeta s : tradeRecordMetaList) {


            try {
                String data = s.getGoodCurrencyItemStack();
                //如果货币是空，删掉记录，否则检查数量1，不是1的话就设置未1
                if (data != null) {
                    ItemStack itemStack = ItemStackUtil.itemStackDeserialize(data);
                    if (itemStack.getAmount() != 1) {
                        itemStack.setAmount(1);
                        s.setGoodItemStack(ItemStackUtil.itemStackSerialize(itemStack));
                        TradeRecordDao.INSTANCE.deleteByUuid(s.getUuid());
                        TradeRecordDao.INSTANCE.insert(s);
                    }
                } else {
                    TradeRecordDao.INSTANCE.deleteByUuid(s.getUuid());
                }
            } catch (Exception e) {
                TradeRecordDao.INSTANCE.deleteByUuid(s.getUuid());
            }
            try {
                //检查商品是不是合格的物品，不是的话抛出异常就删除了
                ItemStack itemStack = ItemStackUtil.itemStackDeserialize(s.getGoodItemStack());
                //检查商品是不是数量1，如果不是设为1
                if (itemStack.getAmount() != 1) {
                    itemStack.setAmount(1);
                    s.setGoodItemStack(ItemStackUtil.itemStackSerialize(itemStack));
                    TradeRecordDao.INSTANCE.deleteByUuid(s.getUuid());
                    TradeRecordDao.INSTANCE.insert(s);
                }
            } catch (Exception e) {
                TradeRecordDao.INSTANCE.deleteByUuid(s.getUuid());
            }

        }


    }


}
