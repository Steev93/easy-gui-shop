package pers.zhangyang.easyguishop.listener.managegoodpagegoodoptionpage;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.ManageGoodPageGoodOptionPage;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easyguishop.yaml.SettingYaml;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;
import pers.zhangyang.easylibrary.util.LocationUtil;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

@EventListener
public class PlayerClickManageGoodPageGoodOptionPageDepositGood implements Listener {

    @GuiDiscreteButtonHandler(guiPage = ManageGoodPageGoodOptionPage.class, slot = {21})
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        ManageGoodPageGoodOptionPage manageGoodPageGoodOptionPage = (ManageGoodPageGoodOptionPage) holder;
        Player player = (Player) event.getWhoClicked();

        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();

        ShopMeta shopMeta;
        assert manageGoodPageGoodOptionPage != null;
        manageGoodPageGoodOptionPage.refresh();
        shopMeta = guiService.getShop(manageGoodPageGoodOptionPage.getShopMeta().getUuid());
        manageGoodPageGoodOptionPage.refresh();

        if (shopMeta == null) {
            return;
        }
        String locationData = shopMeta.getLocation();
        if (locationData == null) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notSetShopLocationWhenDepositGood"));
            return;
        }
        Location location = LocationUtil.deserializeLocation(shopMeta.getLocation());
        if (location.getWorld() == null) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notNearShopLocationWhenDepositGood"));
            return;
        }
        if (!location.getWorld().equals(player.getLocation().getWorld())) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notNearShopLocationWhenDepositGood"));
            return;
        }
        if (location.distance(player.getLocation()) > SettingYaml.INSTANCE.getNonnegativeDoubleDefault("setting.manageGoodRange")) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notNearShopLocationWhenDepositGood"));
            return;
        }


        new PlayerInputAfterClickManageGoodPageGoodOptionPageDepositGood(player, manageGoodPageGoodOptionPage.getOwner(), manageGoodPageGoodOptionPage);

    }

}