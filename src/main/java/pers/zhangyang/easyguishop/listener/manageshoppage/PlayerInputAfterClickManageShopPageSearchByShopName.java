package pers.zhangyang.easyguishop.listener.manageshoppage;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import pers.zhangyang.easyguishop.EasyGuiShop;
import pers.zhangyang.easyguishop.domain.ManageShopPage;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

import java.sql.SQLException;

public class PlayerInputAfterClickManageShopPageSearchByShopName implements Listener {

    private final Player player;
    private final ManageShopPage manageShopPage;

    public PlayerInputAfterClickManageShopPageSearchByShopName(Player player, ManageShopPage manageShopPage) {
        this.manageShopPage = manageShopPage;
        this.player = player;
        Bukkit.getPluginManager().registerEvents(this, EasyGuiShop.instance);
        player.closeInventory();
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToSearchByShopNameInManageShopPage"));
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
                    manageShopPage.searchByShopName(input);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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
