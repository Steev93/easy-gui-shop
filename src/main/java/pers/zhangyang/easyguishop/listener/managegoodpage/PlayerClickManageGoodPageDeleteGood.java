package pers.zhangyang.easyguishop.listener.managegoodpage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.ManageGoodPage;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;

@EventListener
public class PlayerClickManageGoodPageDeleteGood implements Listener {

    @GuiDiscreteButtonHandler(guiPage = ManageGoodPage.class, slot = {50},closeGui = true,refreshGui = false)
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();


        Player player = (Player) event.getWhoClicked();
        ManageGoodPage manageGoodPage = (ManageGoodPage) holder;
        assert manageGoodPage != null;
        new PlayerInputAfterClickManageGoodPageDeleteGood(player, manageGoodPage.getOwner(), manageGoodPage);

    }

}