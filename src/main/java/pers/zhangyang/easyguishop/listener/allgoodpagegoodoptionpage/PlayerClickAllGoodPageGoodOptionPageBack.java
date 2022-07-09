package pers.zhangyang.easyguishop.listener.allgoodpagegoodoptionpage;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easyguishop.domain.AllGoodPage;
import pers.zhangyang.easyguishop.domain.AllGoodPageGoodOptionPage;
import pers.zhangyang.easyguishop.domain.AllShopPageShopOptionPage;
import pers.zhangyang.easyguishop.domain.CollectedShopPageShopOptionPage;

import java.sql.SQLException;

public class PlayerClickAllGoodPageGoodOptionPageBack implements Listener {

    @EventHandler
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof AllGoodPageGoodOptionPage)) {
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

        AllGoodPageGoodOptionPage allGoodPageGoodOptionPage = (AllGoodPageGoodOptionPage) holder;
        InventoryHolder inventoryHolder=allGoodPageGoodOptionPage.getPreviousHolder();
        AllGoodPage allGoodPage= (AllGoodPage) inventoryHolder;
        try {
            allGoodPage.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}