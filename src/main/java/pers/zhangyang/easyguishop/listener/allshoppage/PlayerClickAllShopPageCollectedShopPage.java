package pers.zhangyang.easyguishop.listener.allshoppage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.AllShopPage;
import pers.zhangyang.easyguishop.domain.CollectedShopPage;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;

@EventListener
public class PlayerClickAllShopPageCollectedShopPage implements Listener {

    @GuiDiscreteButtonHandler(guiPage = AllShopPage.class, slot = {48})
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        AllShopPage allShopPage = (AllShopPage) holder;
        Player player = (Player) event.getWhoClicked();


        CollectedShopPage collectedShopPage = new CollectedShopPage(allShopPage, player);
        collectedShopPage.send();


    }

}