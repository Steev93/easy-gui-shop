package pers.zhangyang.easyguishop.listener.managegoodpagegoodoptionpage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.ManageGoodPageGoodOptionPage;
import pers.zhangyang.easyguishop.exception.NotExistGoodException;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

@EventListener
public class PlayerClickManageGoodPageGoodOptionPageResetGoodPrice implements Listener {

    @GuiDiscreteButtonHandler(guiPage = ManageGoodPageGoodOptionPage.class, slot = {24},closeGui = false,refreshGui = true)
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();


        Player player = (Player) event.getWhoClicked();
        ManageGoodPageGoodOptionPage manageGoodPageGoodOptionPage = (ManageGoodPageGoodOptionPage) holder;
        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
        assert manageGoodPageGoodOptionPage != null;
        try {
            guiService.resetGoodPrice(manageGoodPageGoodOptionPage.getGoodMeta().getUuid());

        } catch (NotExistGoodException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistGood"));
            return;
        }
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.resetGoodPrice"));

    }

}