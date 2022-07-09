package pers.zhangyang.easyguishop.listener.manageitemstockpageitemstockoptionpage;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easyguishop.domain.ManageItemStockPageItemStockOptionPage;
import pers.zhangyang.easyguishop.exception.ItemStockNotEmptyException;
import pers.zhangyang.easyguishop.exception.NotExistItemStockException;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

import java.sql.SQLException;

public class PlayerClickManageItemStockPageItemStockOptionPageDeleteItemStock implements Listener {

    @EventHandler
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof ManageItemStockPageItemStockOptionPage)) {
            return;
        }
        int slot = event.getRawSlot();
        if (slot != 23) {
            return;
        }
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || itemStack.getType().equals(Material.AIR)) {
            return;
        }
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();
        Player player = (Player) event.getWhoClicked();
        ManageItemStockPageItemStockOptionPage manageItemStockPageItemStockOptionPage = (ManageItemStockPageItemStockOptionPage) holder;
        try {
            manageItemStockPageItemStockOptionPage.send();
            guiService.deleteItemStock(player.getUniqueId().toString(), manageItemStockPageItemStockOptionPage.getItemStockMeta().getItemStack());
            manageItemStockPageItemStockOptionPage.send();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        } catch (NotExistItemStockException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistItemStock"));
            return;
        } catch (ItemStockNotEmptyException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.itemStockNotEmpty"));
            return;
        }
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.deleteItemStock"));

    }

}
