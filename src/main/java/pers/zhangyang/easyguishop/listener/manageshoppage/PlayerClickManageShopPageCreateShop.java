package pers.zhangyang.easyguishop.listener.manageshoppage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.ManageShopPage;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.PermUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

import java.util.List;

@EventListener
public class PlayerClickManageShopPageCreateShop implements Listener {

    @GuiDiscreteButtonHandler(guiPage = ManageShopPage.class, slot = {48},closeGui = true,refreshGui = false)
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        Player player = (Player) event.getWhoClicked();

        ManageShopPage manageShopPage = (ManageShopPage) holder;

        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();

        Player onlineOwner = manageShopPage.getOwner().getPlayer();
        if (onlineOwner == null) {
            List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.notOnline");
            MessageUtil.sendMessageTo(player, list);
            return;
        }
        Integer perm = PermUtil.getMaxNumberPerm("EasyGuiShop.ShopAmount.", onlineOwner);
        if (perm == null) {
            perm = 0;
        }
        List<ShopMeta> shopMetaList;

        shopMetaList = guiService.listPlayerShop(player.getUniqueId().toString());

        if (perm <= shopMetaList.size() && !onlineOwner.isOp()) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.beyondShopAmount"));
            return;
        }
        new PlayerInputAfterClickManageShopPageCreateShop(player, manageShopPage.getOwner(), manageShopPage);
    }

}
