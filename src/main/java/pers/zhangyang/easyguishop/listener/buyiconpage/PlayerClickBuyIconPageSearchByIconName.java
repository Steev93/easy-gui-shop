package pers.zhangyang.easyguishop.listener.buyiconpage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.BuyIconPage;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;


@EventListener
public class PlayerClickBuyIconPageSearchByIconName implements Listener {

    @GuiDiscreteButtonHandler(guiPage = BuyIconPage.class, slot = {50},closeGui = true,refreshGui = false)
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        Player player = (Player) event.getWhoClicked();

        BuyIconPage allShopPage = (BuyIconPage) holder;
        assert allShopPage != null;
        new PlayerInputAfterClickBuyIconPageSearchByIconName(player, allShopPage.getOwner(), allShopPage);

    }
}
