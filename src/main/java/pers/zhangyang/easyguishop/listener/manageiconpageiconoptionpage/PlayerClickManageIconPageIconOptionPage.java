package pers.zhangyang.easyguishop.listener.manageiconpageiconoptionpage;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.ManageIconPageIconOptionPage;

public class PlayerClickManageIconPageIconOptionPage implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof ManageIconPageIconOptionPage)) {
            return;
        }
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        event.setCancelled(true);
    }
}