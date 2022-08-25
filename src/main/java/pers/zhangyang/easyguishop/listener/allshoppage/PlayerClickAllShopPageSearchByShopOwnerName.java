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
public class PlayerClickAllShopPageSearchByShopOwnerName implements Listener {

    @GuiDiscreteButtonHandler(guiPage = AllShopPage.class, slot = {51},closeGui = true,refreshGui = false)
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        Player player = (Player) event.getWhoClicked();

        AllShopPage allShopPage = (AllShopPage) holder;
        assert allShopPage != null;
        new PlayerInputAfterClickAllShopPageSearchByShopOwnerName(player, allShopPage.getOwner(), allShopPage);

    }
}
