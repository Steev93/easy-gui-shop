package pers.zhangyang.easyguishop.listener.allshoppage;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pers.zhangyang.easyguishop.EasyGuiShop;
import pers.zhangyang.easyguishop.domain.AllShopPage;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

import java.sql.SQLException;

public class PlayerInputAfterClickAllShopPageSearchByShopOwnerName implements Listener {

    private final Player player;
    private final AllShopPage allShopPage;

    public PlayerInputAfterClickAllShopPageSearchByShopOwnerName(Player player, AllShopPage allShopPage) {
        this.allShopPage = allShopPage;
        this.player = player;
        Bukkit.getPluginManager().registerEvents(this, EasyGuiShop.instance);
        player.closeInventory();
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToSearchByShopOwnerNameInAllShopPage"));
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
                allShopPage.refresh();
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
            return;
        }


        unregisterSelf();
        try {
            allShopPage.searchByShopOwnerName(input);
        } catch (SQLException e) {
            e.printStackTrace();
        }


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
