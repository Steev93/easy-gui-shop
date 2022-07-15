package pers.zhangyang.easyguishop.listener.managegoodpagegoodoptionpage;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easyguishop.domain.ManageGoodPageGoodOptionPage;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.LocationUtil;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easyguishop.yaml.SettingYaml;

import java.sql.SQLException;

public class PlayerClickManageGoodPageGoodOptionPageTakeGood implements Listener {

    @EventHandler
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof ManageGoodPageGoodOptionPage)) {
            return;
        }
        int slot = event.getRawSlot();
        if (slot != 23) {
            return;
        }
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || itemStack.getType().equals(Material.AIR)) {
            return;
        }
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }


        ManageGoodPageGoodOptionPage manageGoodPageGoodOptionPage = (ManageGoodPageGoodOptionPage) holder;
        Player player = (Player) event.getWhoClicked();

        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();

        ShopMeta shopMeta;
        try {
            manageGoodPageGoodOptionPage.send();
            shopMeta=guiService.getShop(manageGoodPageGoodOptionPage.getShopMeta().getUuid());
            manageGoodPageGoodOptionPage.send();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        if (shopMeta==null){
            return;
        }

        String locationData=shopMeta.getLocation();
        if (locationData==null){
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notSetShopLocationWhenTakeGood"));
            return;
        }
        Location location= LocationUtil.deserializeLocation(shopMeta.getLocation());
        if (location.getWorld()==null){
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notNearShopLocationWhenTakeGood"));
            return;
        }
        if(!location.getWorld().equals(player.getLocation().getWorld())){
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notNearShopLocationWhenTakeGood"));
            return;
        }
        if (location.distance(player.getLocation())> SettingYaml.INSTANCE.getRange("setting.manageGoodRange")){
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notNearShopLocationWhenTakeGood"));
            return;
        }

        new PlayerInputAfterClickManageGoodPageGoodOptionPageTakeGood(player, manageGoodPageGoodOptionPage);

    }

}
