package pers.zhangyang.easyguishop.listener.allgoodpage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import pers.zhangyang.easyguishop.domain.AllGoodPage;
import pers.zhangyang.easyguishop.domain.AllGoodPageGoodOptionPage;
import pers.zhangyang.easyguishop.meta.GoodMeta;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiSerialButtonHandler;

@EventListener
public class PlayerClickAllGoodPageAllGoodPageGoodOptionPage implements Listener {

    @GuiSerialButtonHandler(guiPage = AllGoodPage.class, from = 0, to = 44)
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        int slot = event.getRawSlot();
        AllGoodPage allGoodPage = (AllGoodPage) event.getInventory().getHolder();
        assert allGoodPage != null;
        Player player = (Player) event.getWhoClicked();

        ShopMeta shopMeta = allGoodPage.getShopMeta();
        GoodMeta goodMeta = allGoodPage.getGoodMetaList().get(slot);
        AllGoodPageGoodOptionPage allGoodPageGoodOptionPage = new AllGoodPageGoodOptionPage(allGoodPage, player, shopMeta, goodMeta);
        allGoodPageGoodOptionPage.send();


    }

}