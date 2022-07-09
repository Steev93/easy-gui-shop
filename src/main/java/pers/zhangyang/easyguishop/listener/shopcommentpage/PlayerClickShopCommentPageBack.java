package pers.zhangyang.easyguishop.listener.shopcommentpage;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easyguishop.domain.AllShopPageShopOptionPage;
import pers.zhangyang.easyguishop.domain.CollectedShopPageShopOptionPage;
import pers.zhangyang.easyguishop.domain.ManageShopPageShopOptionPage;
import pers.zhangyang.easyguishop.domain.ShopCommentPage;

import java.sql.SQLException;

public class PlayerClickShopCommentPageBack implements Listener {

    @EventHandler
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof ShopCommentPage)) {
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


        Player player = (Player) event.getWhoClicked();
        ShopCommentPage shopCommentPage = (ShopCommentPage) holder;


        if (shopCommentPage.getPreviousHolder() instanceof AllShopPageShopOptionPage) {
            AllShopPageShopOptionPage allShopPageShopOptionPage = (AllShopPageShopOptionPage) shopCommentPage.getPreviousHolder();
            try {
                allShopPageShopOptionPage.send();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (shopCommentPage.getPreviousHolder() instanceof CollectedShopPageShopOptionPage) {
            CollectedShopPageShopOptionPage collectedShopPageShopPotionPage = (CollectedShopPageShopOptionPage) shopCommentPage.getPreviousHolder();
            try {
                collectedShopPageShopPotionPage.send();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (shopCommentPage.getPreviousHolder() instanceof ManageShopPageShopOptionPage) {
            ManageShopPageShopOptionPage manageShopPageShopOptionPage = (ManageShopPageShopOptionPage) shopCommentPage.getPreviousHolder();
            try {
                manageShopPageShopOptionPage.send();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

}
