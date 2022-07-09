package pers.zhangyang.easyguishop.listener.manageshoppageshopoptionpage;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easyguishop.domain.ManageShopPageShopOptionPage;
import pers.zhangyang.easyguishop.exception.NotExistLineException;
import pers.zhangyang.easyguishop.exception.NotExistShopException;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

import java.sql.SQLException;

public class PlayerClickManageShopPageShopOptionPageRemoveShopDescription implements Listener {

    @EventHandler
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof ManageShopPageShopOptionPage)) {
            return;
        }
        int slot = event.getRawSlot();
        if (slot != 32) {
            return;
        }
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || itemStack.getType().equals(Material.AIR)) {
            return;
        }

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        ManageShopPageShopOptionPage manageShopPageShopOptionPage = (ManageShopPageShopOptionPage) holder;
        ShopMeta shopMeta = manageShopPageShopOptionPage.getShopMeta();
        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();

        try {
            manageShopPageShopOptionPage.send();
            guiService.removeShopDescription(shopMeta.getUuid());
            manageShopPageShopOptionPage.send();
        } catch (NotExistShopException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistShop"));
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        } catch (NotExistLineException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistLine"));
            return;
        }
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.removeShopDescription"));

    }

}
