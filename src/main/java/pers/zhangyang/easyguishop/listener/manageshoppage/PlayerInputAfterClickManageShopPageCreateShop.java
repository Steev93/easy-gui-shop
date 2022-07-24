package pers.zhangyang.easyguishop.listener.manageshoppage;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pers.zhangyang.easyguishop.domain.ManageShopPage;
import pers.zhangyang.easyguishop.exception.DuplicateShopException;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.FiniteInputListenerBase;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.PermUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;
import pers.zhangyang.easylibrary.util.UuidUtil;

public class PlayerInputAfterClickManageShopPageCreateShop extends FiniteInputListenerBase {

    private final ManageShopPage manageShopPage;

    public PlayerInputAfterClickManageShopPageCreateShop(Player player, OfflinePlayer owner, ManageShopPage manageShopPage) {
        super(player, owner, manageShopPage, 1);
        this.manageShopPage = manageShopPage;
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToCreateShop"));
    }


    @Override
    public void run() {

        int nameLength = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', messages[0])).length();

        Integer perm = PermUtil.getNumberPerm("EasyGuiShop.ShopNameLength.", player);
        if (perm == null) {
            perm = 0;
        }
        if (perm < nameLength && !player.isOp()) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.beyondShopNameLengthWhenCreateShop"));
            return;
        }
        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();

        ShopMeta shopMeta = new ShopMeta(UuidUtil.getUUID(), messages[0], owner.getUniqueId().toString(),
                System.currentTimeMillis(), 0, 0, 0);

        try {
            guiService.createShop(shopMeta);
            manageShopPage.refresh();
        } catch (DuplicateShopException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.duplicateShopWhenCreateShop"));
            return;
        }

        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.createShop"));
    }
}
