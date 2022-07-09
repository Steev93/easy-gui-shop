package pers.zhangyang.easyguishop.listener.managetraderecordpage;

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
import pers.zhangyang.easyguishop.meta.TradeRecordMeta;

import java.sql.SQLException;

public class PlayerClickManageTradeRecordPageManageTradeRecordPageTradeRecordOptionPage implements Listener {

    @EventHandler
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof ManageTradeRecordPage)) {
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
        ManageTradeRecordPage manageTradeRecordPage = (ManageTradeRecordPage) holder;
        TradeRecordMeta tradeRecordMeta = manageTradeRecordPage.getTradeRecordMetaList().get(slot);
        ManageTradeRecordPageTradeRecordOptionPage manageTradeRecordPageTradeRecordOptionPage
                = new ManageTradeRecordPageTradeRecordOptionPage(manageTradeRecordPage, player, tradeRecordMeta);
        try {
            manageTradeRecordPageTradeRecordOptionPage.send();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}