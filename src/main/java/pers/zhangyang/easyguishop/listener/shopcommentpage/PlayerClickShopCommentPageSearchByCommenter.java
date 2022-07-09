package pers.zhangyang.easyguishop.listener.shopcommentpage;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easyguishop.domain.ShopCommentPage;

public class PlayerClickShopCommentPageSearchByCommenter implements Listener {

    @EventHandler
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof ShopCommentPage)) {
            return;
        }
        int slot = event.getRawSlot();
        if (slot != 50) {
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

        ShopCommentPage allShopPageShopOptionPageShopCommentPage = (ShopCommentPage) holder;
        new PlayerInputAfterClickShopCommentPageSearchByCommenterName(player, allShopPageShopOptionPageShopCommentPage);

    }
}