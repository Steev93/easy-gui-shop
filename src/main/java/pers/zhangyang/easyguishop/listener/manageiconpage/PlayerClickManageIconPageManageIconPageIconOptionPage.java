package pers.zhangyang.easyguishop.listener.manageiconpage;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easyguishop.domain.ManageIconPage;
import pers.zhangyang.easyguishop.domain.ManageIconPageIconOptionPage;

import java.sql.SQLException;

public class PlayerClickManageIconPageManageIconPageIconOptionPage implements Listener {

    @EventHandler
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof ManageIconPage)) {
            return;
        }
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 44) {
            return;
        }
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || itemStack.getType().equals(Material.AIR)) {
            return;
        }
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }


        Player player = (Player) event.getWhoClicked();
        ManageIconPage manageIconPage = (ManageIconPage) holder;


        try {
            new ManageIconPageIconOptionPage(manageIconPage, player, manageIconPage.getIconMetaList().get(slot),
                    manageIconPage.getShopMeta()).send();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
