package pers.zhangyang.easyguishop.listener.buyiconpage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.BuyIconPage;
import pers.zhangyang.easyguishop.domain.BuyIconPageIconOptionPage;
import pers.zhangyang.easyguishop.meta.IconMeta;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiSerialButtonHandler;


@EventListener
public class PlayerClickBuyIconPageBuyIconPageIconOptionPage implements Listener {

    @GuiSerialButtonHandler(guiPage = BuyIconPage.class, from = 0, to = 44)
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        int slot = event.getRawSlot();
        BuyIconPage buyIconPage = (BuyIconPage) holder;
        Player player = (Player) event.getWhoClicked();
        assert buyIconPage != null;
        IconMeta iconMeta = buyIconPage.getIconMetaList().get(slot);
        BuyIconPageIconOptionPage buyIconPageIconOptionPage = new BuyIconPageIconOptionPage(buyIconPage, player, iconMeta);

        buyIconPageIconOptionPage.send();


    }

}