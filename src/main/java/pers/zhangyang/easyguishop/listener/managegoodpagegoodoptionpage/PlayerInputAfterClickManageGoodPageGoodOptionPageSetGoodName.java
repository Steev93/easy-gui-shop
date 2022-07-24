package pers.zhangyang.easyguishop.listener.managegoodpagegoodoptionpage;


import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pers.zhangyang.easyguishop.domain.ManageGoodPageGoodOptionPage;
import pers.zhangyang.easyguishop.exception.DuplicateGoodException;
import pers.zhangyang.easyguishop.exception.NotExistGoodException;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.FiniteInputListenerBase;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

public class PlayerInputAfterClickManageGoodPageGoodOptionPageSetGoodName extends FiniteInputListenerBase {

    private final ManageGoodPageGoodOptionPage manageGoodPageGoodOptionPage;

    public PlayerInputAfterClickManageGoodPageGoodOptionPageSetGoodName(Player player, OfflinePlayer owner, ManageGoodPageGoodOptionPage manageGoodPageGoodOptionPage) {
        super(player, owner, manageGoodPageGoodOptionPage, 1);
        this.manageGoodPageGoodOptionPage = manageGoodPageGoodOptionPage;
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToSetGoodName"));
    }

    @Override
    public void run() {


        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
        try {
            guiService.setGoodName(manageGoodPageGoodOptionPage.getGoodMeta().getUuid(), messages[0]);
            manageGoodPageGoodOptionPage.send();
        } catch (NotExistGoodException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistGood"));
            return;
        } catch (DuplicateGoodException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.duplicateGoodWhenSetGoodName"));
            return;
        }
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.setGoodName"));
    }
}
