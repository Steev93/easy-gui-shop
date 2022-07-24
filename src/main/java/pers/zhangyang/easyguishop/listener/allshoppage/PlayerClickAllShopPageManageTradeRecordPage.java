package pers.zhangyang.easyguishop.listener.allshoppage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.AllShopPage;
import pers.zhangyang.easyguishop.domain.ManageTradeRecordPage;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;

@EventListener
public class PlayerClickAllShopPageManageTradeRecordPage implements Listener {

    @GuiDiscreteButtonHandler(guiPage = AllShopPage.class, slot = {46})
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        AllShopPage allShopPage = (AllShopPage) holder;
        Player player = (Player) event.getWhoClicked();
        ManageTradeRecordPage manageTradeRecordPage = new ManageTradeRecordPage(allShopPage, player);

        manageTradeRecordPage.send();


    }

}