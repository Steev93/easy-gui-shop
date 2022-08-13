package pers.zhangyang.easyguishop.listener.manageitemstockpageitemstockoptionpage;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pers.zhangyang.easyguishop.domain.ManageItemStockPageItemStockOptionPage;
import pers.zhangyang.easyguishop.meta.ItemStockMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.FiniteInputListenerBase;
import pers.zhangyang.easylibrary.util.ItemStackUtil;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.PlayerUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

public class PlayerInputAfterClickManageItemStockPageItemStockOptionPageDepositItemStock extends FiniteInputListenerBase {

    private final ItemStockMeta itemStockMeta;
    private final ManageItemStockPageItemStockOptionPage itemStockPageItemStockOptionPage;

    public PlayerInputAfterClickManageItemStockPageItemStockOptionPageDepositItemStock(Player player, OfflinePlayer owner, ItemStockMeta shopMeta, ManageItemStockPageItemStockOptionPage manageShopPage) {
        super(player, owner, manageShopPage, 1);
        this.itemStockPageItemStockOptionPage = manageShopPage;
        this.itemStockMeta = shopMeta;
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToDepositItemStock"));
    }


    @Override
    public void run() {


        int amount;
        try {
            amount = Integer.parseInt(messages[0]);
        } catch (NumberFormatException ex) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.invalidNumber"));
            return;
        }
        if (amount < 0) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.invalidNumber"));
            return;
        }
        int have = PlayerUtil.computeItemHave(ItemStackUtil.itemStackDeserialize(itemStockMeta.getItemStack()), player);
        if (have < amount) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEnoughItemStockWhenDepositItemStock"));

            return;
        }
        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();

        guiService.depositItemStock(owner.getUniqueId().toString(), itemStockMeta.getItemStack(), amount);


        PlayerUtil.removeItem(player, ItemStackUtil.itemStackDeserialize(itemStockMeta.getItemStack()), amount);
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.depositItemStock"));

    }
}
