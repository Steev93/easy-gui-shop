package pers.zhangyang.easyguishop.listener.managegoodpage;


import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pers.zhangyang.easyguishop.domain.ManageGoodPage;
import pers.zhangyang.easyguishop.exception.GoodNotEmptyException;
import pers.zhangyang.easyguishop.exception.NotExistGoodException;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.FiniteInputListenerBase;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

public class PlayerInputAfterClickManageGoodPageDeleteGood extends FiniteInputListenerBase {

    private final ManageGoodPage manageGoodPage;

    public PlayerInputAfterClickManageGoodPageDeleteGood(Player player, OfflinePlayer owner, ManageGoodPage manageGoodPage) {
        super(player, owner, manageGoodPage, 1);
        this.manageGoodPage = manageGoodPage;
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToDeleteGood"));
    }


    @Override
    public void run() {
        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
        try {
            guiService.deleteGood(messages[0], manageGoodPage.getShopMeta().getUuid());
        } catch (NotExistGoodException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistGoodWhenDeleteGood"));
            return;
        } catch (GoodNotEmptyException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.goodNotEmpty"));
            return;
        }

        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.deleteGood"));
    }
}
