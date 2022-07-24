package pers.zhangyang.easyguishop.listener.allshoppage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.AllShopPage;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;

@EventListener
public class PlayerClickAllShopPageSearchByShopName implements Listener {

    @GuiDiscreteButtonHandler(guiPage = AllShopPage.class, slot = {47})
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        Player player = (Player) event.getWhoClicked();
        AllShopPage allShopPage = (AllShopPage) holder;
        assert allShopPage != null;
        new PlayerInputAfterClickAllShopPageSearchByShopName(player, allShopPage.getOwner(), allShopPage);

    }
}
