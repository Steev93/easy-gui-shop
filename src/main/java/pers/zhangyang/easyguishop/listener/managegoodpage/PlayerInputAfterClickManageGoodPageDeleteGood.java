package pers.zhangyang.easyguishop.listener.managegoodpage;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pers.zhangyang.easyguishop.EasyGuiShop;
import pers.zhangyang.easyguishop.domain.ManageGoodPage;
import pers.zhangyang.easyguishop.exception.GoodNotEmptyException;
import pers.zhangyang.easyguishop.exception.NotExistGoodException;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

import java.sql.SQLException;

public class PlayerInputAfterClickManageGoodPageDeleteGood implements Listener {

    private final Player player;
    private final ManageGoodPage manageGoodPage;

    public PlayerInputAfterClickManageGoodPageDeleteGood(Player player, ManageGoodPage manageGoodPage) {
        this.manageGoodPage = manageGoodPage;
        this.player = player;
        Bukkit.getPluginManager().registerEvents(this, EasyGuiShop.instance);
        player.closeInventory();
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToDeleteGood"));
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
                manageGoodPage.refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return;
        }
        unregisterSelf();
        try {
            manageGoodPage.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();
        try {
            guiService.deleteGood(input, manageGoodPage.getShopMeta().getUuid());
            manageGoodPage.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        } catch (NotExistGoodException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistGoodWhenDeleteGood"));
            return;
        } catch (GoodNotEmptyException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.goodNotEmpty"));
            return;
        }

        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.deleteGood"));
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
