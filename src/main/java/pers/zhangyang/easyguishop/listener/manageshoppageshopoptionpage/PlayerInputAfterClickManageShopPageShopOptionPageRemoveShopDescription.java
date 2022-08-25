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

public class PlayerInputAfterClickManageShopPageShopOptionPageRemoveShopDescription extends FiniteInputListenerBase {

    private final ShopMeta shopMeta;
    private final ManageShopPageShopOptionPage manageShopPageShopOptionPage;

    public PlayerInputAfterClickManageShopPageShopOptionPageRemoveShopDescription(Player player, OfflinePlayer owner, ShopMeta shopMeta, ManageShopPageShopOptionPage manageShopPage) {
        super(player, owner, manageShopPage, 1);
        this.manageShopPageShopOptionPage = manageShopPage;
        this.shopMeta = shopMeta;
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToRemoveShopDescription"));
    }


    @Override
    public void run() {

        int lineIndex;
        try {
            lineIndex= Integer.parseInt(messages[0]);
        }catch (NumberFormatException e){
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.invalidNumber"));
            return;
        }
        lineIndex=lineIndex-1;
        if (lineIndex<0){
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.invalidNumber"));
            return;
        }

        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();


        try {
            guiService.removeShopDescription(shopMeta.getUuid(), lineIndex);
        }  catch (NotExistShopException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistShop"));
            return;
        } catch (NotExistLineException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistLineWhenRemoveShopDescription"));
            return;
        }
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.removeShopDescription"));

    }
}
