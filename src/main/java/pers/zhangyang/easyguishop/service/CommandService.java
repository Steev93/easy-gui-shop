package pers.zhangyang.easyguishop.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easyguishop.exception.*;
import pers.zhangyang.easyguishop.meta.IconMeta;


public interface CommandService {

    void createIcon(@NotNull IconMeta iconMeta) throws DuplicateIconException;

    void deleteIcon(@NotNull String iconName) throws NotExistIconException;

    void plusShopPopularity(@NotNull String shopName, int amount) throws NotExistShopException;

    void subtractShopPopularity(@NotNull String shopName, int amount) throws NotExistShopException, NotMorePopularityException;

    void setIconPlayerPointsAndPrice(@NotNull String iconName, int price) throws NotExistIconException;

    void setIconItemPrice(@NotNull String iconName, int price, @NotNull String currencyData) throws NotExistIconException;

    void setIconVaultPrice(@NotNull String iconName, double price) throws NotExistIconException;

    void setIconLimitTime(@NotNull String goodUuid, @Nullable Integer time) throws NotExistIconException;

    void setIconName(@NotNull String iconName, @NotNull String name) throws NotExistIconException, DuplicateIconException;

    void setIconStock(@NotNull String iconName, int amount) throws NotExistIconException;

    void setIconSystem(@NotNull String iconName, boolean system) throws NotExistIconException;

    void setGoodSystem(@NotNull String shopName, @NotNull String goodName, boolean system) throws NotExistShopException, NotExistGoodException;

    void setShopSystem(@NotNull String shopName, boolean system) throws NotExistShopException;

    void correctDatabase();
}
