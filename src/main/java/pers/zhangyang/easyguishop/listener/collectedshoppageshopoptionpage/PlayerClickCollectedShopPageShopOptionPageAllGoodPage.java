package pers.zhangyang.easyguishop.listener.collectedshoppageshopoptionpage;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easyguishop.domain.AllGoodPage;
import pers.zhangyang.easyguishop.domain.CollectedShopPageShopOptionPage;
import pers.zhangyang.easyguishop.exception.NotExistShopException;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

import java.sql.SQLException;

public class PlayerClickCollectedShopPageShopOptionPageAllGoodPage implements Listener {

    @EventHandler
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof CollectedShopPageShopOptionPage)) {
            return;
        }
        int slot = event.getRawSlot();
        if (slot != 31) {
            return;
        }
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || itemStack.getType().equals(Material.AIR)) {
            return;
        }
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        CollectedShopPageShopOptionPage collectedShopPageShopPotionPage = (CollectedShopPageShopOptionPage) holder;

        Player player = (Player) event.getWhoClicked();
        ShopMeta shopMeta = collectedShopPageShopPotionPage.getShopMeta();
        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();
        AllGoodPage allGoodPage = new AllGoodPage(collectedShopPageShopPotionPage, player, shopMeta);
        try {
            allGoodPage.send();
            guiService.viewShop(shopMeta.getUuid(), 1);
            allGoodPage.send();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NotExistShopException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistShop"));
        }

    }

}