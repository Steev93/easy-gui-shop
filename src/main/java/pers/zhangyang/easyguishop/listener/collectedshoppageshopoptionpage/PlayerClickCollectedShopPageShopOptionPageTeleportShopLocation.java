package pers.zhangyang.easyguishop.listener.collectedshoppageshopoptionpage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.CollectedShopPageShopOptionPage;
import pers.zhangyang.easyguishop.domain.Gamer;
import pers.zhangyang.easyguishop.manager.GamerManager;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;
import pers.zhangyang.easylibrary.util.LocationUtil;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.PermUtil;

import java.util.List;

@EventListener
public class PlayerClickCollectedShopPageShopOptionPageTeleportShopLocation implements Listener {

    @GuiDiscreteButtonHandler(guiPage = CollectedShopPageShopOptionPage.class, slot = {22},closeGui = true,refreshGui = false)
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        Player player = (Player) event.getWhoClicked();
        CollectedShopPageShopOptionPage collectedShopPageShopPotionPage = (CollectedShopPageShopOptionPage) holder;

        assert collectedShopPageShopPotionPage != null;
        ShopMeta shopMeta = collectedShopPageShopPotionPage.getShopMeta();
        if (shopMeta.getLocation() == null) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistLocation"));
            return;
        }

        Player onlineOwner = collectedShopPageShopPotionPage.getOwner().getPlayer();
        if (onlineOwner == null) {
            List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.notOnline");
            MessageUtil.sendMessageTo(player, list);
            return;
        }

        Gamer gamer = GamerManager.INSTANCE.getGamer(onlineOwner);
        if (!onlineOwner.isOp()) {
            Integer perm = PermUtil.getMinNumberPerm("EasyGuiShop.teleportShopLocationInterval.", onlineOwner);
            if (perm == null) {
                perm = 0;
            }
            if (gamer.getLastTeleportShopLocationTime() != null && System.currentTimeMillis() - gamer.getLastTeleportShopLocationTime()
                    < perm * 1000L) {

                List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.tooFastInCollectedShopPageShopOptionPage");
                MessageUtil.sendMessageTo(player, list);
                return;
            }
        }


        player.teleport(LocationUtil.deserializeLocation(shopMeta.getLocation()));
        gamer.setLastTeleportShopLocationTime(System.currentTimeMillis());
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.teleportShopLocationInCollectedShopPageShopOptionPage"));


    }

}
