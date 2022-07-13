package pers.zhangyang.easyguishop.listener.managegoodpagegoodoptionpage;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pers.zhangyang.easyguishop.EasyGuiShop;
import pers.zhangyang.easyguishop.domain.ManageGoodPageGoodOptionPage;
import pers.zhangyang.easyguishop.exception.NotExistGoodException;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

import java.sql.SQLException;

public class PlayerInputAfterClickManageGoodPageGoodOptionPageSetGoodLimitTime implements Listener {

    private final Player player;
    private final ManageGoodPageGoodOptionPage manageGoodPageGoodOptionPage;

    public PlayerInputAfterClickManageGoodPageGoodOptionPageSetGoodLimitTime(Player player, ManageGoodPageGoodOptionPage manageGoodPageGoodOptionPage) {
        this.manageGoodPageGoodOptionPage = manageGoodPageGoodOptionPage;
        this.player = player;
        Bukkit.getPluginManager().registerEvents(this, EasyGuiShop.instance);
        player.closeInventory();
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToSetGoodLimitTime"));
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
                manageGoodPageGoodOptionPage.send();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return;
        }


        unregisterSelf();
        try {
            manageGoodPageGoodOptionPage.send();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int price;
        try {
            price = Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.invalidNumber"));
            return;
        }

        if(price<0){
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.invalidNumber"));
            return;
        }
        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();
        try {
            guiService.setGoodLimitTime(manageGoodPageGoodOptionPage.getGoodMeta().getUuid(), price);
            manageGoodPageGoodOptionPage.send();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        } catch (NotExistGoodException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistGood"));
            return;
        }
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.setGoodLimitTime"));
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
