package pers.zhangyang.easyguishop.listener.manageshoppageshopoptionpage;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import pers.zhangyang.easyguishop.EasyGuiShop;
import pers.zhangyang.easyguishop.domain.ManageShopPageShopOptionPage;
import pers.zhangyang.easyguishop.exception.NotExistLineException;
import pers.zhangyang.easyguishop.exception.NotExistShopException;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

import java.sql.SQLException;

public class PlayerInputAfterClickManageShopPageShopOptionPageUpdateShopDescription implements Listener {

    private final Player player;
    private final ShopMeta shopMeta;
    private final ManageShopPageShopOptionPage manageShopPageShopOptionPage;
    private Integer line = null;
    private String des = null;

    public PlayerInputAfterClickManageShopPageShopOptionPageUpdateShopDescription(Player player, ShopMeta shopMeta, ManageShopPageShopOptionPage manageShopPage) {
        this.manageShopPageShopOptionPage = manageShopPage;
        this.player = player;
        this.shopMeta = shopMeta;
        Bukkit.getPluginManager().registerEvents(this, EasyGuiShop.instance);
        player.closeInventory();
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToUpdateShopDescription"));
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
                        manageShopPageShopOptionPage.send();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }.runTask(EasyGuiShop.instance);

            return;
        }

        if (line == null) {
            try {
                line = Integer.valueOf(input);
                line--;
            } catch (NumberFormatException e) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.invalidNumber"));
                return;
            }
            if (line < 0) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.invalidNumber"));
                line = null;
                return;
            }
            return;
        }
        des = input;


        unregisterSelf();
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    manageShopPageShopOptionPage.send();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();


                try {
                    guiService.updateShopDescription(shopMeta.getUuid(), line, des);
                    manageShopPageShopOptionPage.send();
                } catch (NotExistShopException e) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistShop"));
                    return;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                } catch (NotExistLineException e) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistLine"));
                    return;
                }

                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.updateShopDescription"));
            }
        }.runTask(EasyGuiShop.instance);

        //打开


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
