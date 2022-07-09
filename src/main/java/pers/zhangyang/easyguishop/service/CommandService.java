package pers.zhangyang.easyguishop.service;

import pers.zhangyang.easyguishop.exception.*;
import pers.zhangyang.easyguishop.meta.IconMeta;

import java.sql.SQLException;

public interface CommandService {

    void createIcon(IconMeta iconMeta) throws SQLException, DuplicateIconException;

    void deleteIcon(String iconName) throws NotExistIconException, SQLException;

    void plusShopPopularity(String shopName, int amount) throws NotExistShopException, SQLException;

    void subtractShopPopularity(String shopName, int amount) throws NotExistShopException, SQLException, NotMorePopularityException;

    void setIconPlayerPointsAndPrice(String iconName, int price) throws NotExistIconException, SQLException;

    void setIconItemPrice(String iconName, int price, String currencyData) throws NotExistIconException, SQLException;

    void setIconVaultPrice(String iconName, double price) throws NotExistIconException, SQLException;

    void setIconLimitTime(String goodUuid, Integer time) throws NotExistIconException, SQLException;

    void setIconName(String iconName, String name) throws NotExistIconException, SQLException, DuplicateIconException;

    void setIconStock(String iconName, int amount) throws NotExistIconException, SQLException;

    void setIconSystem(String iconName, boolean system) throws SQLException, NotExistIconException;

    void setGoodSystem(String shopName, String goodName, boolean system) throws NotExistShopException, SQLException, NotExistGoodException;

    void setShopSystem(String shopName, boolean system) throws SQLException, NotExistShopException;

    void initDatabase() throws SQLException;

    void correctDatabase() throws SQLException;
}
