package pers.zhangyang.easyguishop.listener.managetraderecordpagetraderecordoptionpage;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easyguishop.domain.ManageTradeRecordPage;
import pers.zhangyang.easyguishop.domain.ManageTradeRecordPageTradeRecordOptionPage;

import java.sql.SQLException;

public class PlayerClickManageTradeRecordPageTradeRecordOptionPageBack implements Listener {

    @EventHandler
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof ManageTradeRecordPageTradeRecordOptionPage)) {
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

        ManageTradeRecordPageTradeRecordOptionPage manageTradeRecordPageTradeRecordOptionPage = (ManageTradeRecordPageTradeRecordOptionPage) holder;
        ManageTradeRecordPage manageItemStockPage = (ManageTradeRecordPage) manageTradeRecordPageTradeRecordOptionPage.getPreviousHolder();
        try {
            manageItemStockPage.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
