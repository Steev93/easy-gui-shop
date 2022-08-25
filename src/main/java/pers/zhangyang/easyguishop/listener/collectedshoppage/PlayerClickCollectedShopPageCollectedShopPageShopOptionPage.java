package pers.zhangyang.easyguishop.listener.collectedshoppage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.CollectedShopPage;
import pers.zhangyang.easyguishop.domain.CollectedShopPageShopOptionPage;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiSerialButtonHandler;

@EventListener
public class PlayerClickCollectedShopPageCollectedShopPageShopOptionPage implements Listener {

    @GuiSerialButtonHandler(guiPage = CollectedShopPage.class, from = 0, to = 44,closeGui = false,refreshGui = false)
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        int slot = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();
        CollectedShopPage collectedShopPage = (CollectedShopPage) holder;
        assert collectedShopPage != null;
        ShopMeta shopMeta = collectedShopPage.getShopMetaList().get(slot);
        CollectedShopPageShopOptionPage collectedShopPageShopPotionPage = new CollectedShopPageShopOptionPage(collectedShopPage, player, shopMeta);

        collectedShopPageShopPotionPage.send();

    }

}
