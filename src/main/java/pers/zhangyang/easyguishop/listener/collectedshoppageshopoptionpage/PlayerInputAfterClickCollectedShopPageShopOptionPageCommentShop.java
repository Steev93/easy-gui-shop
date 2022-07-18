package pers.zhangyang.easyguishop.listener.collectedshoppageshopoptionpage;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import pers.zhangyang.easyguishop.EasyGuiShop;
import pers.zhangyang.easyguishop.domain.CollectedShopPageShopOptionPage;
import pers.zhangyang.easyguishop.exception.DuplicateShopCommenterException;
import pers.zhangyang.easyguishop.meta.ShopCommentMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.util.UuidUtil;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

import java.sql.SQLException;

public class PlayerInputAfterClickCollectedShopPageShopOptionPageCommentShop implements Listener {

    private final Player player;
    private final CollectedShopPageShopOptionPage collectedShopPageShopPotionPage;

    public PlayerInputAfterClickCollectedShopPageShopOptionPageCommentShop(Player player, CollectedShopPageShopOptionPage allShopPage) {
        this.collectedShopPageShopPotionPage = allShopPage;
        this.player = player;
        Bukkit.getPluginManager().registerEvents(this, EasyGuiShop.instance);
        player.closeInventory();
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToCommentShopInCollectedShopPageShopOptionPage"));
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
                        collectedShopPageShopPotionPage.send();
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
                    collectedShopPageShopPotionPage.send();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();
                ShopCommentMeta shopCommentMeta = new ShopCommentMeta(UuidUtil.getUUID(), player.getUniqueId().toString(),
                        collectedShopPageShopPotionPage.getShopMeta().getUuid(), input, System.currentTimeMillis());
                try {
                    guiService.createShopComment(shopCommentMeta);
                    collectedShopPageShopPotionPage.send();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                } catch (DuplicateShopCommenterException ignored) {
                }
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.commentShopInCollectedShopPageShopOptionPage"));
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
