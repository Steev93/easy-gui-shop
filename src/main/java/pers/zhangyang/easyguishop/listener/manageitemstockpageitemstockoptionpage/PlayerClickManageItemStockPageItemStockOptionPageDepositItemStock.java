package pers.zhangyang.easyguishop.listener.manageitemstockpageitemstockoptionpage;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.ManageItemStockPageItemStockOptionPage;
import pers.zhangyang.easyguishop.meta.ItemStockMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easyguishop.yaml.SettingYaml;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

@EventListener
public class PlayerClickManageItemStockPageItemStockOptionPageDepositItemStock implements Listener {

    @GuiDiscreteButtonHandler(guiPage = ManageItemStockPageItemStockOptionPage.class, slot = {22})
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        Player player = (Player) event.getWhoClicked();
        ManageItemStockPageItemStockOptionPage manageItemStockPageItemStockOptionPage = (ManageItemStockPageItemStockOptionPage) holder;


        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();

        ItemStockMeta itemStockMeta;

        manageItemStockPageItemStockOptionPage.refresh();
        itemStockMeta = guiService.getItemStock(player.getUniqueId().toString(), manageItemStockPageItemStockOptionPage.getItemStockMeta().getItemStack());
        manageItemStockPageItemStockOptionPage.refresh();

        if (itemStockMeta == null) {
            return;
        }
        Location location = SettingYaml.INSTANCE.getLocationDefault("setting.bankLocation");
        if (location.getWorld() == null) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notNearBankLocationWhenDepositItemStock"));
            return;
        }
        if (!location.getWorld().equals(player.getLocation().getWorld())) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notNearBankLocationWhenDepositItemStock"));
            return;
        }
        if (location.distance(player.getLocation()) > SettingYaml.INSTANCE.getNonnegativeDoubleDefault("setting.manageItemStockRange")) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notNearBankLocationWhenDepositItemStock"));
            return;
        }


        new PlayerInputAfterClickManageItemStockPageItemStockOptionPageDepositItemStock(player,
                manageItemStockPageItemStockOptionPage.getOwner(), manageItemStockPageItemStockOptionPage.getItemStockMeta(),
                manageItemStockPageItemStockOptionPage);
    }

}
