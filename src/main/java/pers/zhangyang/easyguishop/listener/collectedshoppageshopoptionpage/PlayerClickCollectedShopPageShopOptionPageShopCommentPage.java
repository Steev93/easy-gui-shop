package pers.zhangyang.easyguishop.listener.collectedshoppageshopoptionpage;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easyguishop.domain.CollectedShopPageShopOptionPage;
import pers.zhangyang.easyguishop.domain.ShopCommentPage;
import pers.zhangyang.easyguishop.meta.ShopMeta;

import java.sql.SQLException;

public class PlayerClickCollectedShopPageShopOptionPageShopCommentPage implements Listener {

    @EventHandler
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof CollectedShopPageShopOptionPage)) {
            return;
        }
        int slot = event.getRawSlot();
        if (slot != 23) {
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
        CollectedShopPageShopOptionPage collectedShopPageShopPotionPage = (CollectedShopPageShopOptionPage) holder;
        ShopMeta shopMeta = collectedShopPageShopPotionPage.getShopMeta();
        ShopCommentPage allShopPageShopOptionPageShopCommentPage = new ShopCommentPage(collectedShopPageShopPotionPage, player, shopMeta);
        try {
            allShopPageShopOptionPageShopCommentPage.send();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
