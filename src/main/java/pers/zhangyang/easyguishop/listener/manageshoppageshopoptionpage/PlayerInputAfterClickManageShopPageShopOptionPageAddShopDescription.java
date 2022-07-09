package pers.zhangyang.easyguishop.listener.manageshoppageshopoptionpage;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pers.zhangyang.easyguishop.EasyGuiShop;
import pers.zhangyang.easyguishop.domain.ManageShopPageShopOptionPage;
import pers.zhangyang.easyguishop.exception.NotExistShopException;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

import java.sql.SQLException;

public class PlayerInputAfterClickManageShopPageShopOptionPageAddShopDescription implements Listener {

    private final Player player;
    private final ShopMeta shopMeta;
    private final ManageShopPageShopOptionPage manageShopPageShopOptionPage;

    public PlayerInputAfterClickManageShopPageShopOptionPageAddShopDescription(Player player, ShopMeta shopMeta, ManageShopPageShopOptionPage manageShopPage) {
        this.manageShopPageShopOptionPage = manageShopPage;
        this.player = player;
        this.shopMeta = shopMeta;
        Bukkit.getPluginManager().registerEvents(this, EasyGuiShop.instance);
        player.closeInventory();
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToAddShopDescription"));
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
                manageShopPageShopOptionPage.send();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return;
        }

        unregisterSelf();
        try {
            manageShopPageShopOptionPage.send();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();


        try {
            guiService.addShopDescription(shopMeta.getUuid(), input);
            manageShopPageShopOptionPage.send();
        } catch (NotExistShopException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistShop"));
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.addShopDescription"));
        //打开


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
