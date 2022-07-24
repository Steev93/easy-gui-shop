package pers.zhangyang.easyguishop.listener.managegoodpagegoodoptionpage;


import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easyguishop.domain.ManageGoodPageGoodOptionPage;
import pers.zhangyang.easyguishop.exception.NotExistGoodException;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.FiniteInputListenerBase;
import pers.zhangyang.easylibrary.util.ItemStackUtil;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.PlayerUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

public class PlayerInputAfterClickManageGoodPageGoodOptionPageSetGoodItemPrice extends FiniteInputListenerBase {

    private final ManageGoodPageGoodOptionPage manageGoodPageGoodOptionPage;

    public PlayerInputAfterClickManageGoodPageGoodOptionPageSetGoodItemPrice(Player player, OfflinePlayer owner, ManageGoodPageGoodOptionPage manageGoodPageGoodOptionPage) {
        super(player, owner, manageGoodPageGoodOptionPage, 1);
        this.manageGoodPageGoodOptionPage = manageGoodPageGoodOptionPage;
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToSetGoodItemPrice"));
    }

    @Override
    public void run() {
        ItemStack itemStack = PlayerUtil.getItemInMainHand(player).clone();
        itemStack.setAmount(1);
        if (itemStack.getType().equals(Material.AIR)) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notItemInMainHand"));

            manageGoodPageGoodOptionPage.send();

            return;
        }

        int price;
        try {
            price = Integer.parseInt(messages[0]);
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
            guiService.setGoodItemPrice(manageGoodPageGoodOptionPage.getGoodMeta().getUuid(), price, ItemStackUtil.itemStackSerialize(itemStack));
            manageGoodPageGoodOptionPage.send();
        } catch (NotExistGoodException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistGood"));
            return;
        }
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.setGoodItemPrice"));
    }
}
