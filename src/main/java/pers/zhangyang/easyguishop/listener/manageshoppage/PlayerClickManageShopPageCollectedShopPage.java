package pers.zhangyang.easyguishop.listener.manageshoppage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.AllShopPage;
import pers.zhangyang.easyguishop.domain.CollectedShopPage;
import pers.zhangyang.easyguishop.domain.ManageShopPage;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;

@EventListener
public class PlayerClickManageShopPageCollectedShopPage implements Listener {

    @GuiDiscreteButtonHandler(guiPage = ManageShopPage.class, slot = {46})
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        ManageShopPage allShopPage = (ManageShopPage) holder;
        Player player = (Player) event.getWhoClicked();


        CollectedShopPage collectedShopPage = new CollectedShopPage(allShopPage, player);
        collectedShopPage.send();


    }

}