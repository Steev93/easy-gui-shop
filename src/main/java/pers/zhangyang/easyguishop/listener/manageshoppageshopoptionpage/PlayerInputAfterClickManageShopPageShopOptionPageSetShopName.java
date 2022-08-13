package pers.zhangyang.easyguishop.listener.manageshoppageshopoptionpage;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pers.zhangyang.easyguishop.domain.ManageShopPageShopOptionPage;
import pers.zhangyang.easyguishop.exception.DuplicateShopException;
import pers.zhangyang.easyguishop.exception.NotExistShopException;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.FiniteInputListenerBase;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.PermUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

import java.util.List;

public class PlayerInputAfterClickManageShopPageShopOptionPageSetShopName extends FiniteInputListenerBase {

    private final ShopMeta shopMeta;
    private final ManageShopPageShopOptionPage manageShopPageShopOptionPage;

    public PlayerInputAfterClickManageShopPageShopOptionPageSetShopName(Player player, OfflinePlayer owner, ShopMeta shopMeta, ManageShopPageShopOptionPage manageShopPage) {
        super(player, owner, manageShopPage, 1);
        this.manageShopPageShopOptionPage = manageShopPage;
        this.shopMeta = shopMeta;
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToSetShopName"));
    }


    @Override
    public void run() {


        Player onlineOwner = owner.getPlayer();
        if (onlineOwner == null) {
            List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.notOnline");
            MessageUtil.sendMessageTo(player, list);
            return;
        }


        int nameLength = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', messages[0])).length();

        Integer perm = PermUtil.getMaxNumberPerm("EasyGuiShop.ShopNameLength.", onlineOwner);
        if (perm == null) {
            perm = 0;
        }
        if (perm < nameLength && !onlineOwner.isOp()) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.beyondShopNameLengthWhenSetShopName"));
            return;
        }


        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();


        try {
            guiService.setShopName(shopMeta.getUuid(), messages[0]);

        } catch (NotExistShopException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistShop"));
            return;
        } catch (DuplicateShopException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.duplicateShopWhenSetShopName"));
            return;
        }

        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.setShopName"));
    }
}
