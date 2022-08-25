package pers.zhangyang.easyguishop.listener.manageshoppageshopoptionpage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.ManageShopPageShopOptionPage;
import pers.zhangyang.easyguishop.exception.NotExistShopException;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easyguishop.yaml.SettingYaml;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;
import pers.zhangyang.easylibrary.other.vault.Vault;
import pers.zhangyang.easylibrary.util.LocationUtil;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

@EventListener
public class PlayerClickManageShopPageShopOptionPageSetShopLocation implements Listener {

    @GuiDiscreteButtonHandler(guiPage = ManageShopPageShopOptionPage.class, slot = {22},closeGui = false,refreshGui = true)
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        Player player = (Player) event.getWhoClicked();

        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
        ManageShopPageShopOptionPage manageShopPageShopOptionPage = (ManageShopPageShopOptionPage) holder;
        assert manageShopPageShopOptionPage != null;
        ShopMeta shopMeta = manageShopPageShopOptionPage.getShopMeta();

        shopMeta=guiService.getShop(shopMeta.getUuid());
        if (shopMeta==null){
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistShop"));
            return;
        }

        Double cost= SettingYaml.INSTANCE.getNonnegativeDouble("setShopLocationCost");
        if (cost!=null){
            if (Vault.hook()==null){
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notHookVault"));
                return;
            }
            if (Vault.hook().getBalance(manageShopPageShopOptionPage.getOwner())<cost){
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEnoughVaultWhenSetShopLocation"));
                return;
            }
            Vault.hook().withdrawPlayer(manageShopPageShopOptionPage.getOwner(),cost);
        }

        try {
            guiService.setShopLocation(shopMeta.getUuid(), LocationUtil.serializeLocation(player.getLocation()));
        } catch (NotExistShopException e) {
            e.printStackTrace();
        }

        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.setShopLocation"));


    }

}
