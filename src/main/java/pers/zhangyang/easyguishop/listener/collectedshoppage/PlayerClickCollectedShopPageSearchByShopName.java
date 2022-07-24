package pers.zhangyang.easyguishop.listener.collectedshoppage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.CollectedShopPage;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;

@EventListener
public class PlayerClickCollectedShopPageSearchByShopName implements Listener {

    @GuiDiscreteButtonHandler(guiPage = CollectedShopPage.class, slot = {47})
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        Player player = (Player) event.getWhoClicked();

        CollectedShopPage collectedShopPage = (CollectedShopPage) holder;
        assert collectedShopPage != null;
        new PlayerInputAfterClickCollectedShopPageSearchByShopName(player, collectedShopPage.getOwner(), collectedShopPage);

    }
}
