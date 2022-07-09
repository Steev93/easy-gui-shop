package pers.zhangyang.easyguishop.listener.collectedshoppage;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easyguishop.domain.CollectedShopPage;
import pers.zhangyang.easyguishop.domain.CollectedShopPageShopOptionPage;
import pers.zhangyang.easyguishop.meta.ShopMeta;

import java.sql.SQLException;


public class PlayerClickCollectedShopPageCollectedShopPageShopOptionPage implements Listener {

    @EventHandler
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof CollectedShopPage)) {
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
        CollectedShopPage collectedShopPage = (CollectedShopPage) holder;
        ShopMeta shopMeta = collectedShopPage.getShopMetaList().get(slot);
        CollectedShopPageShopOptionPage collectedShopPageShopPotionPage = new CollectedShopPageShopOptionPage(collectedShopPage, player, shopMeta);
        try {
            collectedShopPageShopPotionPage.send();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
