package pers.zhangyang.easyguishop.listener.buyiconpage;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easyguishop.domain.BuyIconPage;
import pers.zhangyang.easyguishop.domain.BuyIconPageIconOptionPage;
import pers.zhangyang.easyguishop.meta.IconMeta;

import java.sql.SQLException;

public class PlayerClickBuyIconPageBuyIconPageIconOptionPage implements Listener {

    @EventHandler
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof BuyIconPage)) {
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

        BuyIconPage buyIconPage = (BuyIconPage) holder;
        Player player = (Player) event.getWhoClicked();
        IconMeta iconMeta = buyIconPage.getIconMetaList().get(slot);
        BuyIconPageIconOptionPage buyIconPageIconOptionPage = new BuyIconPageIconOptionPage(buyIconPage, player, iconMeta);
        try {
            buyIconPageIconOptionPage.send();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}