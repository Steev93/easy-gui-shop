package pers.zhangyang.easyguishop.listener.managegoodpagegoodoptionpage;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easyguishop.EasyGuiShop;
import pers.zhangyang.easyguishop.domain.ManageGoodPageGoodOptionPage;
import pers.zhangyang.easyguishop.exception.NotExistGoodException;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.InventoryUtil;
import pers.zhangyang.easyguishop.util.ItemStackUtil;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

import java.sql.SQLException;

public class PlayerInputAfterClickManageGoodPageGoodOptionPageSetGoodItemPrice implements Listener {

    private final Player player;
    private final ManageGoodPageGoodOptionPage manageGoodPageGoodOptionPage;

    public PlayerInputAfterClickManageGoodPageGoodOptionPageSetGoodItemPrice(Player player, ManageGoodPageGoodOptionPage manageGoodPageGoodOptionPage) {
        this.manageGoodPageGoodOptionPage = manageGoodPageGoodOptionPage;
        this.player = player;
        Bukkit.getPluginManager().registerEvents(this, EasyGuiShop.instance);
        player.closeInventory();
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToSetGoodItemPrice"));
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

        ItemStack itemStack = InventoryUtil.getItemInMainHand(player).clone();
        itemStack.setAmount(1);
        if (itemStack.getType().equals(Material.AIR)) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notItemInMainHand"));
            try {
                manageGoodPageGoodOptionPage.send();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return;
        }

        int price;
        try {
            price = Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.invalidNumber"));
            try {
                manageGoodPageGoodOptionPage.send();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return;
        }
        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();
        try {
            guiService.setGoodItemPrice(manageGoodPageGoodOptionPage.getGoodMeta().getUuid(), price, ItemStackUtil.itemStackSerialize(itemStack));
            manageGoodPageGoodOptionPage.send();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        } catch (NotExistGoodException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistGood"));
            return;
        }
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.setGoodItemPrice"));

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
