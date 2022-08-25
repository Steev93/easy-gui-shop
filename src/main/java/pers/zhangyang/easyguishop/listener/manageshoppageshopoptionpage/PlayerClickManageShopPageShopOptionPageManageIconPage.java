package pers.zhangyang.easyguishop.listener.manageshoppageshopoptionpage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.ManageIconPage;
import pers.zhangyang.easyguishop.domain.ManageShopPageShopOptionPage;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;

@EventListener
public class PlayerClickManageShopPageShopOptionPageManageIconPage implements Listener {

    @GuiDiscreteButtonHandler(guiPage = ManageShopPageShopOptionPage.class, slot = {40},closeGui = false,refreshGui = false)
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        ManageShopPageShopOptionPage manageGoodPage = (ManageShopPageShopOptionPage) holder;
        Player player = (Player) event.getWhoClicked();

        ManageIconPage manageIconPage = new ManageIconPage(manageGoodPage, player, manageGoodPage.getShopMeta());

        manageIconPage.send();


    }

}
