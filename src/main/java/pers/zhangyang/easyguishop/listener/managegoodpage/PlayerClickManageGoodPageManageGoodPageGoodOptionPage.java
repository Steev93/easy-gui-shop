package pers.zhangyang.easyguishop.listener.managegoodpage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.ManageGoodPage;
import pers.zhangyang.easyguishop.domain.ManageGoodPageGoodOptionPage;
import pers.zhangyang.easyguishop.meta.GoodMeta;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiSerialButtonHandler;

@EventListener
public class PlayerClickManageGoodPageManageGoodPageGoodOptionPage implements Listener {

    @GuiSerialButtonHandler(guiPage = ManageGoodPage.class, from = 0, to = 44,closeGui = false,refreshGui = false)
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        int slot = event.getRawSlot();


        Player player = (Player) event.getWhoClicked();

        ManageGoodPage manageGoodPage = (ManageGoodPage) holder;
        assert manageGoodPage != null;
        ShopMeta shopMeta = manageGoodPage.getShopMeta();
        GoodMeta goodMeta = manageGoodPage.getGoodMetaList().get(slot);
        ManageGoodPageGoodOptionPage manageGoodPageGoodOptionPage = new ManageGoodPageGoodOptionPage(manageGoodPage, player, shopMeta, goodMeta);


        manageGoodPageGoodOptionPage.send();


    }

}
