package pers.zhangyang.easyguishop.listener.manageitemstockpage;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easyguishop.domain.ManageItemStockPage;
import pers.zhangyang.easyguishop.exception.DuplicateItemStockException;
import pers.zhangyang.easyguishop.meta.ItemStockMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;
import pers.zhangyang.easylibrary.util.ItemStackUtil;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.PlayerUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

@EventListener
public class PlayerClickManageItemStockPageCreateItemStock implements Listener {

    @GuiDiscreteButtonHandler(guiPage = ManageItemStockPage.class, slot = {51})
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        ManageItemStockPage manageItemStockPage = (ManageItemStockPage) holder;
        Player player = (Player) event.getWhoClicked();
        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
        if (PlayerUtil.getItemInMainHand(player).getType().equals(Material.AIR)) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notItemInMainHand"));
            return;
        }
        ItemStack hand = PlayerUtil.getItemInMainHand(player).clone();
        hand.setAmount(1);
        ItemStockMeta itemStockMeta = new ItemStockMeta(player.getUniqueId().toString(), ItemStackUtil.itemStackSerialize(hand), 0);
        assert manageItemStockPage != null;
        try {
            guiService.createItemStock(itemStockMeta);
        } catch (DuplicateItemStockException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.duplicateItemStock"));
            return;
        }finally {
            manageItemStockPage.refresh();
        }
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.createItemStock"));

    }

}