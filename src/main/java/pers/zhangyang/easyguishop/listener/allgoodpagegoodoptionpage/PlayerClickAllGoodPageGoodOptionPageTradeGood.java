package pers.zhangyang.easyguishop.listener.allgoodpagegoodoptionpage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.AllGoodPageGoodOptionPage;
import pers.zhangyang.easyguishop.meta.GoodMeta;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;

@EventListener
public class PlayerClickAllGoodPageGoodOptionPageTradeGood implements Listener {

    @GuiDiscreteButtonHandler(guiPage = AllGoodPageGoodOptionPage.class, slot = {40})
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        AllGoodPageGoodOptionPage allGoodPageGoodOptionPage = (AllGoodPageGoodOptionPage) holder;
        Player player = (Player) event.getWhoClicked();
        assert allGoodPageGoodOptionPage != null;
        GoodMeta goodMeta = allGoodPageGoodOptionPage.getGoodMeta();
        new PlayerInputAfterClickAllGoodPageGoodOptionPageTradeGood(player, allGoodPageGoodOptionPage.getOwner(), goodMeta, allGoodPageGoodOptionPage);
    }

}