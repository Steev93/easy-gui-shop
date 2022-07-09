package pers.zhangyang.easyguishop.listener.manageshoppage;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easyguishop.domain.ManageShopPage;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.util.PermUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

import java.sql.SQLException;
import java.util.List;

public class PlayerClickManageShopPageCreateShop implements Listener {

    @EventHandler
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof ManageShopPage)) {
            return;
        }
        int slot = event.getRawSlot();
        if (slot != 48) {
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

        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();

        Integer perm = PermUtil.getNumberPerm("EasyGuiShop.ShopAmount.", player);
        if (perm == null) {
            perm = 0;
        }
        List<ShopMeta> shopMetaList;
        try {
            shopMetaList = guiService.listPlayerShop(player.getUniqueId().toString());
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        if (perm <= shopMetaList.size() && !player.isOp()) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.beyondShopAmount"));
            return;
        }
        ManageShopPage manageShopPage = (ManageShopPage) holder;
        new PlayerInputAfterClickManageShopPageCreateShop(player, manageShopPage);
    }

}
