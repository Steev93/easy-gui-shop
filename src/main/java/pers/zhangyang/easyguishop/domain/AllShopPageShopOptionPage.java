package pers.zhangyang.easyguishop.domain;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.exception.NotExistShopException;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.GuiYaml;

import java.sql.SQLException;

public class AllShopPageShopOptionPage implements InventoryHolder {
    private final Inventory inventory;
    private final InventoryHolder previousHolder;
    private final Player player;
    private ShopMeta shopMeta;

    public AllShopPageShopOptionPage(InventoryHolder previousHolder, Player player, ShopMeta shopMeta) {
        this.shopMeta = shopMeta;
        this.player = player;
        this.previousHolder = previousHolder;
        String title = GuiYaml.INSTANCE.getString("gui.title.allShopPageShopOptionPage");
        if (title == null) {
            this.inventory = Bukkit.createInventory(this, 54);
        } else {
            this.inventory = Bukkit.createInventory(this, 54, ChatColor.translateAlternateColorCodes('&', title));
        }
    }

    //根据Shop的情况来设置Button
    public void send() throws SQLException {
        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();
        this.shopMeta = guiService.getShop(shopMeta.getUuid());
        if (this.shopMeta == null) {
            ((AllShopPage) previousHolder).send();
            return;
        }
        boolean isCollected;
        try {
            isCollected = guiService.getShopCollector(shopMeta.getUuid(), player.getUniqueId().toString()) != null;
        } catch (NotExistShopException e) {
            ((AllShopPage) previousHolder).send();
            return;
        }

        this.inventory.clear();
        if (isCollected) {
            ItemStack cancelCollect = GuiYaml.INSTANCE.getButton("gui.button.allShopPageShopOptionPage.cancelCollectShop");
            inventory.setItem(4, cancelCollect);
        } else {
            ItemStack collect = GuiYaml.INSTANCE.getButton("gui.button.allShopPageShopOptionPage.collectShop");
            inventory.setItem(13, collect);
        }
        ItemStack goShop = GuiYaml.INSTANCE.getButton("gui.button.allShopPageShopOptionPage.allGoodPage");
        inventory.setItem(31, goShop);
        if (shopMeta.getLocation() != null) {
            ItemStack goLocation = GuiYaml.INSTANCE.getButton("gui.button.allShopPageShopOptionPage.goLocation");
            inventory.setItem(22, goLocation);
        }


        ItemStack comment = GuiYaml.INSTANCE.getButton("gui.button.allShopPageShopOptionPage.commentShop");
        inventory.setItem(21, comment);
        ItemStack lookComment = GuiYaml.INSTANCE.getButton("gui.button.allShopPageShopOptionPage.shopCommentPage");
        inventory.setItem(23, lookComment);
        ItemStack back = GuiYaml.INSTANCE.getButton("gui.button.allShopPageShopOptionPage.back");
        inventory.setItem(49, back);


        player.openInventory(this.inventory);
    }

    public ShopMeta getShopMeta() {
        return shopMeta;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public InventoryHolder getPreviousHolder() {
        return previousHolder;
    }
}
