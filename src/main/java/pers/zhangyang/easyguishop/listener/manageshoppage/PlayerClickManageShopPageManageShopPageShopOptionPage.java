package pers.zhangyang.easyguishop.listener.manageshoppage;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easyguishop.domain.ManageShopPage;
import pers.zhangyang.easyguishop.domain.ManageShopPageShopOptionPage;
import pers.zhangyang.easyguishop.meta.ShopMeta;

import java.sql.SQLException;

public class PlayerClickManageShopPageManageShopPageShopOptionPage implements Listener {

    @EventHandler
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof ManageShopPage)) {
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
        ManageShopPage manageShopPage = (ManageShopPage) holder;
        ShopMeta shopMeta = manageShopPage.getShopMetaList().get(slot);
        ManageShopPageShopOptionPage manageShopPageShopOptionPage = new ManageShopPageShopOptionPage(manageShopPage, player, shopMeta);
        try {
            manageShopPageShopOptionPage.send();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
