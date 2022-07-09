package pers.zhangyang.easyguishop.listener.manageitemstockpage;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easyguishop.domain.ManageItemStockPage;
import pers.zhangyang.easyguishop.exception.DuplicateItemStockException;
import pers.zhangyang.easyguishop.meta.ItemStockMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.InventoryUtil;
import pers.zhangyang.easyguishop.util.ItemStackUtil;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

import java.sql.SQLException;

public class PlayerClickManageItemStockCreateItemStock implements Listener {

    @EventHandler
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof ManageItemStockPage)) {
            return;
        }
        int slot = event.getRawSlot();
        if (slot != 51) {
            return;
        }
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || itemStack.getType().equals(Material.AIR)) {
            return;
        }
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }


        ManageItemStockPage manageItemStockPage = (ManageItemStockPage) holder;
        Player player = (Player) event.getWhoClicked();
        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();
        if (InventoryUtil.getItemInMainHand(player).getType().equals(Material.AIR)) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notItemInMainHand"));
            return;
        }
        ItemStack hand = InventoryUtil.getItemInMainHand(player).clone();
        hand.setAmount(1);
        ItemStockMeta itemStockMeta = new ItemStockMeta(player.getUniqueId().toString(), ItemStackUtil.itemStackSerialize(hand), 0);
        try {
            manageItemStockPage.refresh();
            guiService.createItemStock(itemStockMeta);
            manageItemStockPage.refresh();
        } catch (DuplicateItemStockException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.duplicateItemStock"));
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.createItemStock"));

    }

}