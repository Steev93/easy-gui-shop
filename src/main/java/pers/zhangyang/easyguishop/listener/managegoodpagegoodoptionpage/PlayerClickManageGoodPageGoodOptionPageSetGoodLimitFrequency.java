package pers.zhangyang.easyguishop.listener.managegoodpagegoodoptionpage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.ManageGoodPageGoodOptionPage;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;

@EventListener
public class PlayerClickManageGoodPageGoodOptionPageSetGoodLimitFrequency implements Listener {

    @GuiDiscreteButtonHandler(guiPage = ManageGoodPageGoodOptionPage.class, slot = {42},closeGui = true,refreshGui = false)
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        Player player = (Player) event.getWhoClicked();
        ManageGoodPageGoodOptionPage manageGoodPageGoodOptionPage = (ManageGoodPageGoodOptionPage) holder;
        new PlayerInputAfterClickManageGoodPageGoodOptionPageSetGoodLimitFrequency(player, manageGoodPageGoodOptionPage.getOwner(), manageGoodPageGoodOptionPage);

    }

}