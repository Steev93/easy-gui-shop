package pers.zhangyang.easyguishop.listener.shopcommentpage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.ShopCommentPage;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;

@EventListener
public class PlayerClickShopCommentPageSearchByCommenter implements Listener {

    @GuiDiscreteButtonHandler(guiPage = ShopCommentPage.class, slot = {50})
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        Player player = (Player) event.getWhoClicked();

        ShopCommentPage allShopPageShopOptionPageShopCommentPage = (ShopCommentPage) holder;
        new PlayerInputAfterClickShopCommentPageSearchByCommenterName(player, allShopPageShopOptionPageShopCommentPage.getOwner(), allShopPageShopOptionPageShopCommentPage);

    }
}