package pers.zhangyang.easyguishop.listener.allshoppage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.AllShopPage;
import pers.zhangyang.easyguishop.domain.AllShopPageShopOptionPage;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiSerialButtonHandler;


@EventListener
public class PlayerClickAllShopPageAllShopPageShopOptionPage implements Listener {

    @GuiSerialButtonHandler(guiPage = AllShopPage.class, from = 0, to = 44)
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        int slot = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();
        AllShopPage allShopPage = (AllShopPage) holder;
        assert allShopPage != null;

        ShopMeta shopMeta = allShopPage.getShopMetaList().get(slot);
        AllShopPageShopOptionPage allShopPageShopOptionPage = new AllShopPageShopOptionPage(allShopPage, player, shopMeta);
        allShopPageShopOptionPage.send();

    }

}
