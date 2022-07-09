package pers.zhangyang.easyguishop.listener.manageshoppageshopoptionpage;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easyguishop.domain.ManageIconPage;
import pers.zhangyang.easyguishop.domain.ManageShopPageShopOptionPage;

import java.sql.SQLException;

public class PlayerClickManageShopPageShopOptionPageManageIconPage implements Listener {

    @EventHandler
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof ManageShopPageShopOptionPage)) {
            return;
        }
        int slot = event.getRawSlot();
        if (slot != 40) {
            return;
        }
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || itemStack.getType().equals(Material.AIR)) {
            return;
        }
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }


        ManageShopPageShopOptionPage manageGoodPage = (ManageShopPageShopOptionPage) holder;
        Player player = (Player) event.getWhoClicked();

        ManageIconPage manageIconPage = new ManageIconPage(manageGoodPage, player, manageGoodPage.getShopMeta());
        try {
            manageIconPage.send();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
