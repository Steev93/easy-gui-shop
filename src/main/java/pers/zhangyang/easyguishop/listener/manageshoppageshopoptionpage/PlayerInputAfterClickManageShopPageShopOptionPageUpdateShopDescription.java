package pers.zhangyang.easyguishop.listener.manageshoppageshopoptionpage;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pers.zhangyang.easyguishop.domain.ManageShopPageShopOptionPage;
import pers.zhangyang.easyguishop.exception.NotExistLineException;
import pers.zhangyang.easyguishop.exception.NotExistShopException;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.FiniteInputListenerBase;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

public class PlayerInputAfterClickManageShopPageShopOptionPageUpdateShopDescription extends FiniteInputListenerBase {

    private final ShopMeta shopMeta;
    private final ManageShopPageShopOptionPage manageShopPageShopOptionPage;

    public PlayerInputAfterClickManageShopPageShopOptionPageUpdateShopDescription(Player player, OfflinePlayer owner, ShopMeta shopMeta, ManageShopPageShopOptionPage manageShopPage) {
        super(player, owner, manageShopPage, 2);
        this.manageShopPageShopOptionPage = manageShopPage;
        this.shopMeta = shopMeta;
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToUpdateShopDescription"));
    }


    @Override
    public void run() {


        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();

        int line;
        try {
            line = Integer.parseInt(messages[0]);
            line--;
        } catch (NumberFormatException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.invalidNumber"));
            return;
        }
        if (line < 0) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.invalidNumber"));
            return;
        }
        try {
            guiService.updateShopDescription(shopMeta.getUuid(), line, messages[1]);

        } catch (NotExistShopException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistShop"));
            return;
        } catch (NotExistLineException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistLineWhenUpdateShopDescription"));
            return;
        }

        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.updateShopDescription"));
    }
}
