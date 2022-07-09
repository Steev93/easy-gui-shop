package pers.zhangyang.easyguishop.util;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.*;
import pers.zhangyang.easyguishop.exception.NotExistShopException;

import java.sql.SQLException;

public class RefreshUtil {

    public static void refreshAllShopPageShopOptionPageShopCommentPage() throws SQLException, NotExistShopException {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Inventory inv = p.getOpenInventory().getTopInventory();
            InventoryHolder inventoryHolder = inv.getHolder();
            if (inventoryHolder == null) {
                return;
            }
            if (!(inventoryHolder instanceof ShopCommentPage)) {
                continue;
            }
            ShopCommentPage allShopPageShopOptionPageShopCommentPage = (ShopCommentPage) inventoryHolder;
            allShopPageShopOptionPageShopCommentPage.refresh();
        }
    }

    public static void refreshManageShopPageShopOptionPage() throws SQLException, NotExistShopException {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Inventory inv = p.getOpenInventory().getTopInventory();
            InventoryHolder inventoryHolder = inv.getHolder();
            if (inventoryHolder == null) {
                return;
            }
            if (!(inventoryHolder instanceof ManageShopPageShopOptionPage)) {
                continue;
            }
            ManageShopPageShopOptionPage manageShopPageShopOptionPage = (ManageShopPageShopOptionPage) inventoryHolder;
            manageShopPageShopOptionPage.send();
        }
    }

    public static void refreshCollectedShopPageShopOptionPage() throws SQLException, NotExistShopException {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Inventory inv = p.getOpenInventory().getTopInventory();
            InventoryHolder inventoryHolder = inv.getHolder();
            if (inventoryHolder == null) {
                return;
            }
            if (!(inventoryHolder instanceof CollectedShopPageShopOptionPage)) {
                continue;
            }
            CollectedShopPageShopOptionPage collectedShopPageShopPotionPage = (CollectedShopPageShopOptionPage) inventoryHolder;
            collectedShopPageShopPotionPage.send();
        }
    }

    public static void refreshAllShopPageShopOptionPage() throws SQLException, NotExistShopException {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Inventory inv = p.getOpenInventory().getTopInventory();
            InventoryHolder inventoryHolder = inv.getHolder();
            if (inventoryHolder == null) {
                return;
            }
            if (!(inventoryHolder instanceof AllShopPageShopOptionPage)) {
                continue;
            }
            AllShopPageShopOptionPage allShopPageShopOptionPage = (AllShopPageShopOptionPage) inventoryHolder;
            allShopPageShopOptionPage.send();


        }
    }

    public static void refreshCollectedShopPage() throws SQLException {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Inventory inv = p.getOpenInventory().getTopInventory();
            InventoryHolder inventoryHolder = inv.getHolder();
            if (inventoryHolder == null) {
                return;
            }
            if (!(inventoryHolder instanceof CollectedShopPage)) {
                continue;
            }
            CollectedShopPage collectedShopPage = (CollectedShopPage) inventoryHolder;
            collectedShopPage.refresh();


        }
    }

    public static void refreshManageShopPage() throws SQLException {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Inventory inv = p.getOpenInventory().getTopInventory();
            InventoryHolder inventoryHolder = inv.getHolder();
            if (inventoryHolder == null) {
                return;
            }
            if (!(inventoryHolder instanceof ManageShopPage)) {
                continue;
            }
            ManageShopPage manageShopPage = (ManageShopPage) inventoryHolder;

            manageShopPage.refresh();


        }
    }

    public static void refreshShopCommentPage() throws SQLException, NotExistShopException {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Inventory inv = p.getOpenInventory().getTopInventory();
            InventoryHolder inventoryHolder = inv.getHolder();
            if (inventoryHolder == null) {
                return;
            }
            if (!(inventoryHolder instanceof ShopCommentPage)) {
                continue;
            }
            ShopCommentPage shopCommentPage = (ShopCommentPage) inventoryHolder;
            shopCommentPage.refresh();


        }
    }

    public static void refreshMyCommentPage() throws SQLException {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Inventory inv = p.getOpenInventory().getTopInventory();
            InventoryHolder inventoryHolder = inv.getHolder();
            if (inventoryHolder == null) {
                return;
            }
            if (!(inventoryHolder instanceof ManageCommentPage)) {
                continue;
            }
            ManageCommentPage manageCommentPage = (ManageCommentPage) inventoryHolder;
            manageCommentPage.refresh();


        }
    }

    public static void refreshAllShopPage() throws SQLException {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Inventory inv = p.getOpenInventory().getTopInventory();
            InventoryHolder inventoryHolder = inv.getHolder();
            if (inventoryHolder == null) {
                return;
            }
            if (!(inventoryHolder instanceof AllShopPage)) {
                continue;
            }
            AllShopPage allShopPage = (AllShopPage) inventoryHolder;

            allShopPage.refresh();
        }
    }
}
