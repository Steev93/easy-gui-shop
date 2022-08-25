package pers.zhangyang.easyguishop.listener.manageiconpage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.ManageIconPage;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;

@EventListener
public class PlayerClickManageIconPageSearchByIconName implements Listener {

    @GuiDiscreteButtonHandler(guiPage = ManageIconPage.class, slot = {50},closeGui = true,refreshGui = false)
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        Player player = (Player) event.getWhoClicked();
        ManageIconPage manageShopPage = (ManageIconPage) holder;
        assert manageShopPage != null;
        new PlayerInputAfterClickManageIconPageSearchByIconName(player, manageShopPage.getOwner(), manageShopPage);

    }
}
