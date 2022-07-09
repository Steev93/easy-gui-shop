package pers.zhangyang.easyguishop.listener.allgoodpage;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easyguishop.domain.AllGoodPage;
import pers.zhangyang.easyguishop.domain.AllGoodPageGoodOptionPage;
import pers.zhangyang.easyguishop.meta.GoodMeta;
import pers.zhangyang.easyguishop.meta.ShopMeta;

import java.sql.SQLException;

public class PlayerClickAllGoodPageAllGoodPageGoodOptionPage implements Listener {

    @EventHandler
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof AllGoodPage)) {
            return;
        }
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 44) {
            return;
        }
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || itemStack.getType().equals(Material.AIR)) {
            return;
        }
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        AllGoodPage allGoodPage = (AllGoodPage) holder;
        Player player = (Player) event.getWhoClicked();
        ShopMeta shopMeta = allGoodPage.getShopMeta();
        GoodMeta goodMeta = allGoodPage.getGoodMetaList().get(slot);
        AllGoodPageGoodOptionPage allGoodPageGoodOptionPage = new AllGoodPageGoodOptionPage(allGoodPage, player, shopMeta, goodMeta);
        try {
            allGoodPageGoodOptionPage.send();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}