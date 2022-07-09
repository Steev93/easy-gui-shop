package pers.zhangyang.easyguishop.listener.manageshoppageshopoptionpage;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.ManageShopPageShopOptionPage;

public class PlayerClickManageShopPageShopOptionPage implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof ManageShopPageShopOptionPage)) {
            return;
        }
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        event.setCancelled(true);
    }
}
