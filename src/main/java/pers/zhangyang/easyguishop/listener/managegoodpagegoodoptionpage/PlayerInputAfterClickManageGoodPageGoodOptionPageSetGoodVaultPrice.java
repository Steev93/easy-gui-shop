package pers.zhangyang.easyguishop.listener.managegoodpagegoodoptionpage;


import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pers.zhangyang.easyguishop.domain.ManageGoodPageGoodOptionPage;
import pers.zhangyang.easyguishop.exception.NotExistGoodException;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.FiniteInputListenerBase;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

public class PlayerInputAfterClickManageGoodPageGoodOptionPageSetGoodVaultPrice extends FiniteInputListenerBase {

    private final ManageGoodPageGoodOptionPage manageGoodPageGoodOptionPage;

    public PlayerInputAfterClickManageGoodPageGoodOptionPageSetGoodVaultPrice(Player player, OfflinePlayer owner, ManageGoodPageGoodOptionPage manageGoodPageGoodOptionPage) {
        super(player, owner, manageGoodPageGoodOptionPage, 1);
        this.manageGoodPageGoodOptionPage = manageGoodPageGoodOptionPage;
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToSetGoodVaultPrice"));
    }

    @Override
    public void run() {

        double price;
        try {
            price = Double.parseDouble(messages[0]);
        } catch (NumberFormatException ex) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.invalidNumber"));
            return;
        }
        if (price < 0) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.invalidNumber"));
            return;
        }
        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
        try {
            guiService.setGoodVaultPrice(manageGoodPageGoodOptionPage.getGoodMeta().getUuid(), price);
            manageGoodPageGoodOptionPage.send();
        } catch (NotExistGoodException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistGood"));
            return;
        }
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.setGoodVaultPrice"));
    }
}
