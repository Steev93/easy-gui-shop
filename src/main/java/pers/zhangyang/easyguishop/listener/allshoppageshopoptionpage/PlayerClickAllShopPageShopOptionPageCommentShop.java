package pers.zhangyang.easyguishop.listener.allshoppageshopoptionpage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.AllShopPageShopOptionPage;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;

@EventListener
public class PlayerClickAllShopPageShopOptionPageCommentShop implements Listener {

    @GuiDiscreteButtonHandler(guiPage = AllShopPageShopOptionPage.class, slot = {21})
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();


        Player player = (Player) event.getWhoClicked();
        AllShopPageShopOptionPage allShopPageShopOptionPage = (AllShopPageShopOptionPage) holder;

        assert allShopPageShopOptionPage != null;
        new PlayerInputAfterClickAllShopPageShopOptionPageCommentShop(player, allShopPageShopOptionPage.getOwner(), allShopPageShopOptionPage);
    }

}
