package pers.zhangyang.easyguishop.listener.manageitemstockpageitemstockoptionpage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.ManageItemStockPageItemStockOptionPage;
import pers.zhangyang.easyguishop.exception.ItemStockNotEmptyException;
import pers.zhangyang.easyguishop.exception.NotExistItemStockException;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

@EventListener
public class PlayerClickManageItemStockPageItemStockOptionPageDeleteItemStock implements Listener {

    @GuiDiscreteButtonHandler(guiPage = ManageItemStockPageItemStockOptionPage.class, slot = {23},closeGui = false,refreshGui = true)
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
        Player player = (Player) event.getWhoClicked();
        ManageItemStockPageItemStockOptionPage manageItemStockPageItemStockOptionPage = (ManageItemStockPageItemStockOptionPage) holder;
        try {
            guiService.deleteItemStock(player.getUniqueId().toString(), manageItemStockPageItemStockOptionPage.getItemStockMeta().getItemStack());
        } catch (NotExistItemStockException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistItemStock"));
            return;
        } catch (ItemStockNotEmptyException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.itemStockNotEmpty"));
            return;
        } finally {
            manageItemStockPageItemStockOptionPage.refresh();
        }
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.deleteItemStock"));

    }

}
