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
import pers.zhangyang.easyguishop.exception.NotExistNextException;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

import java.sql.SQLException;

public class PlayerClickManageShopPageNext implements Listener {

    @EventHandler
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof ManageShopPage)) {
            return;
        }
        int slot = event.getRawSlot();
        if (slot != 53) {
            return;
        }
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || itemStack.getType().equals(Material.AIR)) {
            return;
        }
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ManageShopPage manageShopPage = (ManageShopPage) holder;
        try {
            manageShopPage.nextPage();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NotExistNextException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistNext"));
        }


    }

}