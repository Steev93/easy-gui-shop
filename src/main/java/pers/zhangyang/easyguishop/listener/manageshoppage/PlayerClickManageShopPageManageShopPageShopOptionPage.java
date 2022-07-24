package pers.zhangyang.easyguishop.listener.manageshoppage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.ManageShopPage;
import pers.zhangyang.easyguishop.domain.ManageShopPageShopOptionPage;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiSerialButtonHandler;

@EventListener
public class PlayerClickManageShopPageManageShopPageShopOptionPage implements Listener {

    @GuiSerialButtonHandler(guiPage = ManageShopPage.class, from = 0, to = 44)
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        int slot = event.getRawSlot();


        Player player = (Player) event.getWhoClicked();
        ManageShopPage manageShopPage = (ManageShopPage) holder;
        ShopMeta shopMeta = manageShopPage.getShopMetaList().get(slot);
        ManageShopPageShopOptionPage manageShopPageShopOptionPage = new ManageShopPageShopOptionPage(manageShopPage, player, shopMeta);

        manageShopPageShopOptionPage.send();
    }

}
