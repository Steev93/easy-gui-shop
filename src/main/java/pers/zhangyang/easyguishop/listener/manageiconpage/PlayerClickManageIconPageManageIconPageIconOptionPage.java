package pers.zhangyang.easyguishop.listener.manageiconpage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.ManageIconPage;
import pers.zhangyang.easyguishop.domain.ManageIconPageIconOptionPage;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiSerialButtonHandler;

@EventListener
public class PlayerClickManageIconPageManageIconPageIconOptionPage implements Listener {

    @GuiSerialButtonHandler(guiPage = ManageIconPage.class, from = 0, to = 44,closeGui = false,refreshGui = false)
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        int slot = event.getRawSlot();


        Player player = (Player) event.getWhoClicked();
        ManageIconPage manageIconPage = (ManageIconPage) holder;


        assert manageIconPage != null;
        new ManageIconPageIconOptionPage(manageIconPage, player, manageIconPage.getIconMetaList().get(slot),
                manageIconPage.getShopMeta()).send();
    }

}
