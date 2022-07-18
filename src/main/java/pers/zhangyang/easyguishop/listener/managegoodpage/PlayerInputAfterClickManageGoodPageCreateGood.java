package pers.zhangyang.easyguishop.listener.managegoodpage;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import pers.zhangyang.easyguishop.EasyGuiShop;
import pers.zhangyang.easyguishop.domain.ManageGoodPage;
import pers.zhangyang.easyguishop.exception.DuplicateGoodException;
import pers.zhangyang.easyguishop.meta.GoodMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.*;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

import java.sql.SQLException;

public class PlayerInputAfterClickManageGoodPageCreateGood implements Listener {

    private final Player player;
    private final ManageGoodPage manageGoodPage;
    String type;
    String name;

    public PlayerInputAfterClickManageGoodPageCreateGood(Player player, ManageGoodPage manageGoodPage) {
        this.manageGoodPage = manageGoodPage;
        this.player = player;
        Bukkit.getPluginManager().registerEvents(this, EasyGuiShop.instance);
        player.closeInventory();
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToCreateGood"));
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
                        manageGoodPage.refresh();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }.runTask(EasyGuiShop.instance);

            return;
        }
        if (name == null) {
            name = input;
            return;
        }
        type = input;
        unregisterSelf();
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    manageGoodPage.refresh();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                ItemStack itemStack = InventoryUtil.getItemInMainHand(player).clone();
                itemStack.setAmount(1);
                if (itemStack.getType().equals(Material.AIR)) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notItemInMainHand"));
                    return;
                }
                String buy = MessageYaml.INSTANCE.getInput("message.input.buy");
                String sell = MessageYaml.INSTANCE.getInput("message.input.sell");
                if (!type.equalsIgnoreCase(buy) && !type.equalsIgnoreCase(sell)) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.unknownType"));
                    return;
                }
                GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();
                GoodMeta goodMeta = new GoodMeta(UuidUtil.getUUID(), name, ItemStackUtil.itemStackSerialize(itemStack), type, System.currentTimeMillis(),
                        false, 0, manageGoodPage.getShopMeta().getUuid());
                try {
                    guiService.createGood(goodMeta);
                    manageGoodPage.refresh();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                } catch (DuplicateGoodException e) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.duplicateGoodWhenCreateGood"));
                    return;
                }
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.createGood"));
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
