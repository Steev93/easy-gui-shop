package pers.zhangyang.easyguishop.listener.allgoodpage;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easyguishop.domain.AllGoodPage;
import pers.zhangyang.easyguishop.domain.AllShopPageShopOptionPage;
import pers.zhangyang.easyguishop.domain.CollectedShopPageShopOptionPage;

import java.sql.SQLException;

public class PlayerClickAllGoodPageBack implements Listener {

    @EventHandler
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof AllGoodPage)) {
            return;
        }
        int slot = event.getRawSlot();
        if (slot != 49) {
            return;
        }
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || itemStack.getType().equals(Material.AIR)) {
            return;
        }
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        AllGoodPage allGoodPage = (AllGoodPage) holder;
        InventoryHolder inventoryHolder=allGoodPage.getPreviousHolder();

        if (inventoryHolder instanceof AllShopPageShopOptionPage){
            AllShopPageShopOptionPage allShopPageShopOptionPage = (AllShopPageShopOptionPage) allGoodPage.getPreviousHolder();
            try {
                allShopPageShopOptionPage.send();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (inventoryHolder instanceof CollectedShopPageShopOptionPage){
            CollectedShopPageShopOptionPage collectedShopPageShopOptionPage = (CollectedShopPageShopOptionPage) allGoodPage.getPreviousHolder();
            try {
                collectedShopPageShopOptionPage.send();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

}