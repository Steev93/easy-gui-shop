package pers.zhangyang.easyguishop.listener.manageitemstockpageitemstockoptionpage;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pers.zhangyang.easyguishop.domain.ManageItemStockPageItemStockOptionPage;
import pers.zhangyang.easyguishop.exception.NotExistItemStockException;
import pers.zhangyang.easyguishop.exception.NotMoreItemStockException;
import pers.zhangyang.easyguishop.meta.ItemStockMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.FiniteInputListenerBase;
import pers.zhangyang.easylibrary.util.ItemStackUtil;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.PlayerUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

public class PlayerInputAfterClickManageItemStockPageItemStockOptionPageTakeItemStock extends FiniteInputListenerBase {

    private final ItemStockMeta itemStockMeta;
    private final ManageItemStockPageItemStockOptionPage itemStockPageItemStockOptionPage;

    public PlayerInputAfterClickManageItemStockPageItemStockOptionPageTakeItemStock(Player player, OfflinePlayer owner, ItemStockMeta shopMeta, ManageItemStockPageItemStockOptionPage manageShopPage) {
        super(player, owner, manageShopPage, 1);
        this.itemStockPageItemStockOptionPage = manageShopPage;
        this.itemStockMeta = shopMeta;
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToTakeItemStock"));
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
        int space = PlayerUtil.checkSpace(player, ItemStackUtil.itemStackDeserialize(itemStockMeta.getItemStack()));
        if (space < amount) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEnoughSpaceWhenTakeItemStock"));
            return;
        }
        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
        try {
            guiService.takeItemStock(owner.getUniqueId().toString(), itemStockMeta.getItemStack(), amount);

        } catch (NotMoreItemStockException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notMoreItemStockWhenTakeItemStock"));
            return;
        } catch (NotExistItemStockException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistItemStockWhenTakeItemStock"));
            return;
        }
        PlayerUtil.addItem(player, ItemStackUtil.itemStackDeserialize(itemStockMeta.getItemStack()), amount);
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.takeItemStock"));
    }
}
