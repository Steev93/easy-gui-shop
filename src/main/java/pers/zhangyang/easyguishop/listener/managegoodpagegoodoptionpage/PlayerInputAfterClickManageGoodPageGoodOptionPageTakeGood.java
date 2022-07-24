package pers.zhangyang.easyguishop.listener.managegoodpagegoodoptionpage;


import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pers.zhangyang.easyguishop.domain.ManageGoodPageGoodOptionPage;
import pers.zhangyang.easyguishop.exception.NotExistGoodException;
import pers.zhangyang.easyguishop.exception.NotMoreGoodException;
import pers.zhangyang.easyguishop.meta.GoodMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.FiniteInputListenerBase;
import pers.zhangyang.easylibrary.util.ItemStackUtil;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.PlayerUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

public class PlayerInputAfterClickManageGoodPageGoodOptionPageTakeGood extends FiniteInputListenerBase {

    private final ManageGoodPageGoodOptionPage manageGoodPageGoodOptionPage;

    public PlayerInputAfterClickManageGoodPageGoodOptionPageTakeGood(Player player, OfflinePlayer owner, ManageGoodPageGoodOptionPage manageGoodPageGoodOptionPage) {
        super(player, owner, manageGoodPageGoodOptionPage, 1);
        this.manageGoodPageGoodOptionPage = manageGoodPageGoodOptionPage;
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToTakeGood"));
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

        GoodMeta goodMeta = manageGoodPageGoodOptionPage.getGoodMeta();
        int space = PlayerUtil.checkSpace(player, ItemStackUtil.itemStackDeserialize(goodMeta.getGoodItemStack()));
        if (space < amount) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEnoughSpaceWhenTakeGood"));
            return;
        }


        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
        try {
            guiService.takeGood(manageGoodPageGoodOptionPage.getGoodMeta().getUuid(), amount);
            manageGoodPageGoodOptionPage.send();
        } catch (NotExistGoodException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistGood"));
            return;
        } catch (NotMoreGoodException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notMoreGood"));
            return;
        }
        PlayerUtil.addItem(player, ItemStackUtil.itemStackDeserialize(goodMeta.getGoodItemStack()), amount);
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.takeGood"));
    }
}
