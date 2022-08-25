package pers.zhangyang.easyguishop.listener.allshoppageshopoptionpage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.AllShopPageShopOptionPage;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;
import pers.zhangyang.easylibrary.util.LocationUtil;
import pers.zhangyang.easylibrary.util.MessageUtil;


@EventListener
public class PlayerClickAllShopPageShopOptionPageTeleportShopLocation implements Listener {

    @GuiDiscreteButtonHandler(guiPage = AllShopPageShopOptionPage.class, slot = {22},closeGui = true,refreshGui = false)
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        Player player = (Player) event.getWhoClicked();
        AllShopPageShopOptionPage allShopPageShopOptionPage = (AllShopPageShopOptionPage) holder;
        assert allShopPageShopOptionPage != null;
        ShopMeta shopMeta = allShopPageShopOptionPage.getShopMeta();
        if (shopMeta.getLocation() == null) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistLocation"));
            return;
        }

        player.teleport(LocationUtil.deserializeLocation(shopMeta.getLocation()));
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.teleportShopLocationInAllShopPageShopOptionPage"));
    }

}
