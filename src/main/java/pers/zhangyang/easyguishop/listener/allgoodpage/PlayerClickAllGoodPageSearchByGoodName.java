package pers.zhangyang.easyguishop.listener.allgoodpage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import pers.zhangyang.easyguishop.domain.AllGoodPage;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;

@EventListener
public class PlayerClickAllGoodPageSearchByGoodName implements Listener {

    @GuiDiscreteButtonHandler(guiPage = AllGoodPage.class, slot = {47})
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        AllGoodPage allGoodPage = (AllGoodPage) event.getInventory().getHolder();
        Player player = (Player) event.getWhoClicked();
        assert allGoodPage != null;
        new PlayerInputAfterClickAllGoodPageSearchByGoodName(player, allGoodPage.getOwner(), allGoodPage);
    }

}