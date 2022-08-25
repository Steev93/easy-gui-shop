package pers.zhangyang.easyguishop.listener.manageshoppage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.BuyIconPage;
import pers.zhangyang.easyguishop.domain.ManageShopPage;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;

@EventListener
public class PlayerClickManageShopPageBuyIconPage implements Listener {

    @GuiDiscreteButtonHandler(guiPage = ManageShopPage.class, slot = {51},closeGui = false,refreshGui = false)
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        Player player = (Player) event.getWhoClicked();
        ManageShopPage manageShopPage = (ManageShopPage) holder;
        BuyIconPage buyIconPage = new BuyIconPage(manageShopPage, player);

        buyIconPage.send();


    }

}
