package pers.zhangyang.easyguishop.listener.manageitemstockpageitemstockoptionpage;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easyguishop.domain.ManageItemStockPageItemStockOptionPage;
import pers.zhangyang.easyguishop.meta.ItemStockMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easyguishop.yaml.SettingYaml;

import java.sql.SQLException;

public class PlayerClickManageItemStockPageItemStockOptionPageTakeItemStock implements Listener {

    @EventHandler
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof ManageItemStockPageItemStockOptionPage)) {
            return;
        }
        int slot = event.getRawSlot();
        if (slot != 21) {
            return;
        }
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || itemStack.getType().equals(Material.AIR)) {
            return;
        }
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        ManageItemStockPageItemStockOptionPage manageItemStockPageItemStockOptionPage = (ManageItemStockPageItemStockOptionPage) holder;


        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();

        ItemStockMeta itemStockMeta;
        try {
            manageItemStockPageItemStockOptionPage.send();
            itemStockMeta=guiService.getItemStock(player.getUniqueId().toString(),manageItemStockPageItemStockOptionPage.getItemStockMeta().getItemStack());
            manageItemStockPageItemStockOptionPage.send();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        if (itemStockMeta==null){
            return;
        }
        Location location= SettingYaml.INSTANCE.getLocationMath("setting.bankLocation");
        if (location.distance(player.getLocation())> SettingYaml.INSTANCE.getRange("setting.manageItemStockRange")){
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notNearBankLocationWhenTakeItemStock"));
            return;
        }



        new PlayerInputAfterClickManageItemStockPageItemStockOptionPageTakeItemStock(player, manageItemStockPageItemStockOptionPage.getItemStockMeta(),
                manageItemStockPageItemStockOptionPage);
    }

}
