package pers.zhangyang.easyguishop.listener.manageitemstockpage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.ManageItemStockPage;
import pers.zhangyang.easyguishop.domain.ManageItemStockPageItemStockOptionPage;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiSerialButtonHandler;

@EventListener
public class PlayerClickManageItemStockPageManageItemStockPageItemStockOptionPage implements Listener {

    @GuiSerialButtonHandler(guiPage = ManageItemStockPage.class, from = 0, to = 44,closeGui = false,refreshGui = false)
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        int slot = event.getRawSlot();

        Player player = (Player) event.getWhoClicked();
        ManageItemStockPage manageItemStockPage = (ManageItemStockPage) holder;
        assert manageItemStockPage != null;
        ManageItemStockPageItemStockOptionPage manageItemStockPageItemStockOptionPage = new ManageItemStockPageItemStockOptionPage(manageItemStockPage,
                player, manageItemStockPage.getItemStockMetaList().get(slot));


        manageItemStockPageItemStockOptionPage.send();


    }

}