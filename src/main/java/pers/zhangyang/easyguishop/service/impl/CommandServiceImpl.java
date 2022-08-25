package pers.zhangyang.easyguishop.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.dao.*;
import pers.zhangyang.easyguishop.exception.*;
import pers.zhangyang.easyguishop.meta.*;
import pers.zhangyang.easyguishop.service.CommandService;
import pers.zhangyang.easylibrary.util.ItemStackUtil;
import pers.zhangyang.easylibrary.util.LocationUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CommandServiceImpl implements CommandService {

    @Override
    public void createIcon(@NotNull IconMeta iconMeta) throws DuplicateIconException {
        if (new IconDao().getByUuid(iconMeta.getUuid()) != null || new IconDao().getByName(iconMeta.getName()) != null) {
            throw new DuplicateIconException();
        }

        new IconDao().insert(iconMeta);
    }

    @Override
    public void deleteIcon(@NotNull String iconName) throws NotExistIconException {
        IconMeta iconMeta = new IconDao().getByName(iconName);
        if (iconMeta == null) {
            throw new NotExistIconException();
        }
        List<ShopMeta> shopMetaList = new ShopDao().listByIconUuid(iconMeta.getUuid());
        for (ShopMeta shopMeta : shopMetaList) {
            shopMeta.setIconUuid(null);
            new ShopDao().deleteByUuid(shopMeta.getUuid());
            new ShopDao().insert(shopMeta);
        }
        new IconOwnerDao().deleteByIconUuid(iconMeta.getUuid());
        new IconDao().deleteByUuid(iconMeta.getUuid());
    }

    @Override
    public void plusShopPopularity(@NotNull String shopName, int amount) throws NotExistShopException {
        if (amount < 0) {
            throw new IllegalArgumentException();
        }
        ShopMeta shopMeta = new ShopDao().getByName(shopName);
        if (shopMeta == null) {
            throw new NotExistShopException();
        }
        if (Integer.MAX_VALUE - shopMeta.getPopularity() >= amount) {
            shopMeta.setPopularity(shopMeta.getPopularity() + amount);
        } else {
            shopMeta.setPopularity(Integer.MAX_VALUE);
        }
        new ShopDao().deleteByUuid(shopMeta.getUuid());
        new ShopDao().insert(shopMeta);
    }

    @Override
    public void subtractShopPopularity(@NotNull String shopName, int amount) throws NotExistShopException, NotMorePopularityException {
        if (amount < 0) {
            throw new IllegalArgumentException();
        }
        ShopMeta shopMeta = new ShopDao().getByName(shopName);
        if (shopMeta == null) {
            throw new NotExistShopException();
        }
        if ((shopMeta.getPopularity() - amount) < 0) {
            throw new NotMorePopularityException();
        }
        shopMeta.setPopularity(shopMeta.getPopularity() - amount);
        new ShopDao().deleteByUuid(shopMeta.getUuid());
        new ShopDao().insert(shopMeta);
    }

    @Override
    public void setIconPlayerPointsAndPrice(@NotNull String iconName, int price) throws NotExistIconException {
        IconMeta iconMeta = new IconDao().getByName(iconName);
        if (iconMeta == null) {
            throw new NotExistIconException();
        }
        iconMeta.setPlayerPointsPrice(price);
        iconMeta.setVaultPrice(null);
        iconMeta.setItemPrice(null);
        iconMeta.setCurrencyItemStack(null);

        new IconDao().deleteByUuid(iconMeta.getUuid());
        new IconDao().insert(iconMeta);
    }


    @Override
    public void setIconItemPrice(@NotNull String iconName, int price, @NotNull String currencyData) throws NotExistIconException {
        IconMeta iconMeta = new IconDao().getByName(iconName);
        if (iconMeta == null) {
            throw new NotExistIconException();
        }
        iconMeta.setItemPrice(price);
        iconMeta.setPlayerPointsPrice(null);
        iconMeta.setVaultPrice(null);
        iconMeta.setCurrencyItemStack(currencyData);
        new IconDao().deleteByUuid(iconMeta.getUuid());
        new IconDao().insert(iconMeta);
    }

    @Override
    public void setIconVaultPrice(@NotNull String iconName, double price) throws NotExistIconException {
        IconMeta iconMeta = new IconDao().getByName(iconName);
        if (iconMeta == null) {
            throw new NotExistIconException();
        }
        iconMeta.setVaultPrice(price);
        iconMeta.setPlayerPointsPrice(null);
        iconMeta.setItemPrice(null);
        iconMeta.setCurrencyItemStack(null);
        new IconDao().deleteByUuid(iconMeta.getUuid());
        new IconDao().insert(iconMeta);
    }

    @Override
    public void setIconLimitTime(@NotNull String iconName, Integer time) throws NotExistIconException {
        IconMeta iconMeta = new IconDao().getByName(iconName);
        if (iconMeta == null) {
            throw new NotExistIconException();
        }
        iconMeta.setLimitTime(time);
        new IconDao().deleteByUuid(iconMeta.getUuid());
        new IconDao().insert(iconMeta);
    }


    @Override
    public void setIconName(@NotNull String oldName, @NotNull String name) throws NotExistIconException, DuplicateIconException {
        IconMeta iconMeta = new IconDao().getByName(oldName);
        if (iconMeta == null) {
            throw new NotExistIconException();
        }
        if (new IconDao().getByName(name) != null) {
            throw new DuplicateIconException();
        }
        iconMeta.setName(name);
        new IconDao().deleteByUuid(iconMeta.getUuid());
        new IconDao().insert(iconMeta);
    }

    @Override
    public void setIconStock(@NotNull String iconName, int amount) throws NotExistIconException {
        IconMeta iconMeta = new IconDao().getByName(iconName);
        if (iconMeta == null) {
            throw new NotExistIconException();
        }
        iconMeta.setStock(amount);
        new IconDao().deleteByUuid(iconMeta.getUuid());
        new IconDao().insert(iconMeta);
    }

    @Override
    public void setIconSystem(@NotNull String iconName, boolean system) throws NotExistIconException {
        IconMeta iconMeta = new IconDao().getByName(iconName);
        if (iconMeta == null) {
            throw new NotExistIconException();
        }
        iconMeta.setSystem(system);
        new IconDao().deleteByUuid(iconMeta.getUuid());
        new IconDao().insert(iconMeta);
    }

    @Override
    public void setGoodSystem(@NotNull String shopName, @NotNull String goodName, boolean system) throws NotExistShopException, NotExistGoodException {
        ShopMeta shopMeta = new ShopDao().getByName(shopName);
        if (shopMeta == null) {
            throw new NotExistShopException();
        }
        GoodMeta goodMeta = new GoodDao().getByNameAndShopUuid(goodName, shopMeta.getUuid());
        if (goodMeta == null) {
            throw new NotExistGoodException();
        }
        goodMeta.setSystem(system);
        new GoodDao().deleteByUuid(goodMeta.getUuid());
        new GoodDao().insert(goodMeta);
    }

    @Override
    public void setShopSystem(@NotNull String shopName, boolean system) throws NotExistShopException {
        ShopMeta shopMeta = new ShopDao().getByName(shopName);
        if (shopMeta == null) {
            throw new NotExistShopException();
        }
        List<GoodMeta> goodMetaList = new GoodDao().listByShopUuid(shopMeta.getUuid());
        for (GoodMeta g : goodMetaList) {
            g.setSystem(system);
            new GoodDao().deleteByUuid(g.getUuid());
            new GoodDao().insert(g);
        }
    }


    @Override
    public void correctDatabase() {

        //修理Shop表的序列化数据
        List<ShopMeta> shopMetaList = new ShopDao().list();
        for (ShopMeta s : shopMetaList) {
            if (s.getLocation()==null) {
            continue;
            }
            Location location=LocationUtil.deserializeLocation(s.getLocation());
            if (location==null){
                s.setLocation(null);
                new ShopDao().deleteByUuid(s.getUuid());
                new ShopDao().insert(s);
            }
        }


        //修理Shop表的序列化数据
         shopMetaList = new ShopDao().list();
        for (ShopMeta s : shopMetaList) {
            Gson gson = new Gson();
            Type stringListType = new TypeToken<ArrayList<String>>() {
            }.getType();
            try {
                gson.fromJson(s.getDescription(), stringListType);
            } catch (JsonSyntaxException e) {
                new ShopDao().deleteByUuid(s.getUuid());
                s.setShopDescription(null);
                new ShopDao().insert(s);
            }
            try {
                String data = s.getLocation();
                if (data != null) {
                    LocationUtil.deserializeLocation(data);
                }
            } catch (Exception e) {
                new ShopDao().deleteByUuid(s.getUuid());
                s.setLocation(null);
                new ShopDao().insert(s);
            }
        }

        //修理Good表的序列化数据
        List<GoodMeta> goodMetaList = new GoodDao().list();
        for (GoodMeta s : goodMetaList) {

            try {
                String data = s.getCurrencyItemStack();
                if (data != null) {
                    ItemStack itemStack = ItemStackUtil.itemStackDeserialize(data);
                    if (itemStack.getAmount() != 1) {
                        itemStack.setAmount(1);
                        s.setCurrencyItemStack(ItemStackUtil.itemStackSerialize(itemStack));
                        new GoodDao().deleteByUuid(s.getUuid());
                        new GoodDao().insert(s);
                    }
                }
            } catch (Exception e) {
                new GoodDao().deleteByUuid(s.getUuid());
                s.setCurrencyItemStack(null);
                s.setItemPrice(null);
                new GoodDao().insert(s);
            }
            try {
                String data = s.getGoodItemStack();
                ItemStack itemStack = ItemStackUtil.itemStackDeserialize(data);
                if (itemStack.getAmount() != 1) {
                    itemStack.setAmount(1);
                    s.setGoodItemStack(ItemStackUtil.itemStackSerialize(itemStack));
                    new GoodDao().deleteByUuid(s.getUuid());
                    new GoodDao().insert(s);
                }

            } catch (Exception e) {
                new GoodDao().deleteByUuid(s.getUuid());
            }
        }

        //修理Icon表的序列化数据
        List<IconMeta> iconMetaList = new IconDao().list();
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
                new IconDao().deleteByUuid(s.getUuid());
                s.setCurrencyItemStack(null);
                s.setItemPrice(null);
                new IconDao().insert(s);
            }
            try {
                //检查是不是物品，不是物品删掉
                ItemStackUtil.itemStackDeserialize(s.getIconItemStack());
            } catch (Exception e) {
                new IconDao().deleteByUuid(s.getUuid());
            }
        }

        //修理ItemStock表的序列化数据
        List<ItemStockMeta> itemStockMetaList = new ItemStockDao().list();
        for (ItemStockMeta s : itemStockMetaList) {

            try {
                //检查是否未物品，不是物品删掉
                ItemStack itemStack = ItemStackUtil.itemStackDeserialize(s.getItemStack());
                //检查数量是否未1，否则设置为1
                if (itemStack.getAmount() != 1) {
                    itemStack.setAmount(1);
                    s.setItemStack(ItemStackUtil.itemStackSerialize(itemStack));
                    new ItemStockDao().deleteByPlayerUuidAndItemStack(s.getPlayerUuid(), s.getItemStack());
                    new ItemStockDao().insert(s);
                }
            } catch (Exception e) {
                new ItemStockDao().deleteByPlayerUuidAndItemStack(s.getPlayerUuid(), s.getItemStack());
            }
        }


        //修理TradeRecord表的序列化数据
        List<TradeRecordMeta> tradeRecordMetaList = new TradeRecordDao().list();
        for (TradeRecordMeta s : tradeRecordMetaList) {


            try {
                String data = s.getGoodCurrencyItemStack();
                //如果货币是空的，删掉记录，否则检查数量1，不是1的话就设置未1
                if (data != null) {
                    ItemStack itemStack = ItemStackUtil.itemStackDeserialize(data);
                    if (itemStack.getAmount() != 1) {
                        itemStack.setAmount(1);
                        s.setGoodItemStack(ItemStackUtil.itemStackSerialize(itemStack));
                        new TradeRecordDao().deleteByUuid(s.getUuid());
                        new TradeRecordDao().insert(s);
                    }
                } else {
                    new TradeRecordDao().deleteByUuid(s.getUuid());
                }
            } catch (Exception e) {
                new TradeRecordDao().deleteByUuid(s.getUuid());
            }
            try {
                //检查商品是不是合格的物品，不是的话抛出异常就删除了
                ItemStack itemStack = ItemStackUtil.itemStackDeserialize(s.getGoodItemStack());
                //检查商品是不是数量1，如果不是设为1
                if (itemStack.getAmount() != 1) {
                    itemStack.setAmount(1);
                    s.setGoodItemStack(ItemStackUtil.itemStackSerialize(itemStack));
                    new TradeRecordDao().deleteByUuid(s.getUuid());
                    new TradeRecordDao().insert(s);
                }
            } catch (Exception e) {
                new TradeRecordDao().deleteByUuid(s.getUuid());
            }

        }


    }


}
