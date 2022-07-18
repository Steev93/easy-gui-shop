package pers.zhangyang.easyguishop.listener.manageshoppage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import pers.zhangyang.easyguishop.EasyGuiShop;
import pers.zhangyang.easyguishop.domain.ManageShopPage;
import pers.zhangyang.easyguishop.exception.DuplicateShopException;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.util.PermUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.util.UuidUtil;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

import java.sql.SQLException;

public class PlayerInputAfterClickManageShopPageCreateShop implements Listener {

    private final Player player;
    private final ManageShopPage manageShopPage;

    public PlayerInputAfterClickManageShopPageCreateShop(Player player, ManageShopPage manageShopPage) {
        this.manageShopPage = manageShopPage;
        this.player = player;
        Bukkit.getPluginManager().registerEvents(this, EasyGuiShop.instance);
        player.closeInventory();
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToCreateShop"));
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
                        manageShopPage.refresh();
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
                    manageShopPage.refresh();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                int nameLength = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', input)).length();

                Integer perm = PermUtil.getNumberPerm("EasyGuiShop.ShopNameLength.", player);
                if (perm == null) {
                    perm = 0;
                }
                if (perm < nameLength && !player.isOp()) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.beyondShopNameLength"));
                    return;
                }

                GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();

                ShopMeta shopMeta = new ShopMeta(UuidUtil.getUUID(), input, player.getUniqueId().toString(),
                        System.currentTimeMillis(), 0, 0, 0);

                try {
                    guiService.createShop(shopMeta);
                    manageShopPage.refresh();
                } catch (DuplicateShopException e) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.duplicateShopWhenCreateShop"));
                    return;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                }

                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.createShop"));
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
