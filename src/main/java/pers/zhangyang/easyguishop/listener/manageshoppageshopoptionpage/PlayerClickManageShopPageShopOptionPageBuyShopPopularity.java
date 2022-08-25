package pers.zhangyang.easyguishop.listener.manageshoppageshopoptionpage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.ManageShopPageShopOptionPage;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;


@EventListener
public class PlayerClickManageShopPageShopOptionPageBuyShopPopularity implements Listener {
    @GuiDiscreteButtonHandler(guiPage = ManageShopPageShopOptionPage.class, slot = {3},closeGui = true,refreshGui = false)
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        Player player = (Player) event.getWhoClicked();

        ManageShopPageShopOptionPage manageShopPageShopOptionPage = (ManageShopPageShopOptionPage) holder;
        ShopMeta shopMeta = manageShopPageShopOptionPage.getShopMeta();
        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();

        new PlayerInputAfterClickManageShopPageShopOptionPageBuyShopPopularity(player,manageShopPageShopOptionPage.getOwner(),
                shopMeta,manageShopPageShopOptionPage);


    }
}
