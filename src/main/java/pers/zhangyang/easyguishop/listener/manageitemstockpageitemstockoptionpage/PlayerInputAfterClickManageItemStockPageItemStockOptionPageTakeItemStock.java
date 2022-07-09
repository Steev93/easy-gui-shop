package pers.zhangyang.easyguishop.listener.manageitemstockpageitemstockoptionpage;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pers.zhangyang.easyguishop.EasyGuiShop;
import pers.zhangyang.easyguishop.domain.ManageItemStockPageItemStockOptionPage;
import pers.zhangyang.easyguishop.exception.NotExistItemStockException;
import pers.zhangyang.easyguishop.exception.NotMoreItemStockException;
import pers.zhangyang.easyguishop.meta.ItemStockMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.InventoryUtil;
import pers.zhangyang.easyguishop.util.ItemStackUtil;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

import java.sql.SQLException;

public class PlayerInputAfterClickManageItemStockPageItemStockOptionPageTakeItemStock implements Listener {

    private final Player player;
    private final ItemStockMeta itemStockMeta;
    private final ManageItemStockPageItemStockOptionPage itemStockPageItemStockOptionPage;

    public PlayerInputAfterClickManageItemStockPageItemStockOptionPageTakeItemStock(Player player, ItemStockMeta shopMeta, ManageItemStockPageItemStockOptionPage manageShopPage) {
        this.itemStockPageItemStockOptionPage = manageShopPage;
        this.player = player;
        this.itemStockMeta = shopMeta;
        Bukkit.getPluginManager().registerEvents(this, EasyGuiShop.instance);
        player.closeInventory();
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToTakeItemStock"));
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {

        Player player = event.getPlayer();
        if (!player.equals(this.player)) {
            return;
        }
        event.setCancelled(true);
        String input = event.getMessage();
        if (input.equalsIgnoreCase(MessageYaml.INSTANCE.getInput("message.input.cancel"))) {
            unregisterSelf();
            try {
                itemStockPageItemStockOptionPage.send();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return;
        }

        unregisterSelf();
        try {
            itemStockPageItemStockOptionPage.send();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int amount;
        try {
            amount = Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.invalidNumber"));
            try {
                itemStockPageItemStockOptionPage.send();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return;
        }

        int space = InventoryUtil.checkSpace(player, ItemStackUtil.itemStackDeserialize(itemStockMeta.getItemStack()));
        if (space < amount) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEnoughSpaceWhenTakeItemStock"));
            return;
        }
        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();
        try {
            guiService.takeItemStock(player.getUniqueId().toString(), itemStockMeta.getItemStack(), amount);
            itemStockPageItemStockOptionPage.send();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NotMoreItemStockException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notMoreItemStock"));
            return;
        } catch (NotExistItemStockException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistItemStock"));
            return;
        }
        InventoryUtil.addItem(player, ItemStackUtil.itemStackDeserialize(itemStockMeta.getItemStack()), amount);
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.takeItemStock"));

    }


    private void unregisterSelf() {
        PlayerChatEvent.getHandlerList().unregister(this);
        PlayerQuitEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!event.getPlayer().equals(this.player)) {
            return;
        }
        unregisterSelf();
    }


}
