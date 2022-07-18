package pers.zhangyang.easyguishop.listener.managegoodpagegoodoptionpage;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import pers.zhangyang.easyguishop.EasyGuiShop;
import pers.zhangyang.easyguishop.domain.ManageGoodPageGoodOptionPage;
import pers.zhangyang.easyguishop.exception.NotExistGoodException;
import pers.zhangyang.easyguishop.exception.NotMoreGoodException;
import pers.zhangyang.easyguishop.meta.GoodMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.InventoryUtil;
import pers.zhangyang.easyguishop.util.ItemStackUtil;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

import java.sql.SQLException;

public class PlayerInputAfterClickManageGoodPageGoodOptionPageTakeGood implements Listener {

    private final Player player;
    private final ManageGoodPageGoodOptionPage manageGoodPageGoodOptionPage;

    public PlayerInputAfterClickManageGoodPageGoodOptionPageTakeGood(Player player, ManageGoodPageGoodOptionPage manageGoodPageGoodOptionPage) {
        this.manageGoodPageGoodOptionPage = manageGoodPageGoodOptionPage;
        this.player = player;
        Bukkit.getPluginManager().registerEvents(this, EasyGuiShop.instance);
        player.closeInventory();
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToTakeGood"));
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!player.equals(this.player)) {
            return;
        }
        event.setCancelled(true);
        String input = event.getMessage();
        if (input.equalsIgnoreCase(MessageYaml.INSTANCE.getInput("message.input.cancel"))) {
            unregisterSelf();
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        manageGoodPageGoodOptionPage.send();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }.runTask(EasyGuiShop.instance);

            return;
        }


        unregisterSelf();
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    manageGoodPageGoodOptionPage.send();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                int amount;
                try {
                    amount = Integer.parseInt(input);
                } catch (NumberFormatException ex) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.invalidNumber"));

                    return;
                }

                if (amount < 0) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.invalidNumber"));
                    return;
                }

                GoodMeta goodMeta = manageGoodPageGoodOptionPage.getGoodMeta();
                int space = InventoryUtil.checkSpace(player, ItemStackUtil.itemStackDeserialize(goodMeta.getGoodItemStack()));
                if (space < amount) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEnoughSpaceWhenTakeGood"));
                    return;
                }


                GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();
                try {
                    guiService.takeGood(manageGoodPageGoodOptionPage.getGoodMeta().getUuid(), amount);
                    manageGoodPageGoodOptionPage.send();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                } catch (NotExistGoodException e) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistGood"));
                    return;
                } catch (NotMoreGoodException e) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notMoreGood"));
                    return;
                }
                InventoryUtil.addItem(player, ItemStackUtil.itemStackDeserialize(goodMeta.getGoodItemStack()), amount);
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.takeGood"));
            }
        }.runTask(EasyGuiShop.instance);


    }


    private void unregisterSelf() {
        AsyncPlayerChatEvent.getHandlerList().unregister(this);
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
