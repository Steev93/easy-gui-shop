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

public class CollectedShopPageShopOptionPage implements InventoryHolder {
    private final Inventory inventory;
    private final InventoryHolder previousHolder;
    private final Player player;
    private ShopMeta shopMeta;

    public CollectedShopPageShopOptionPage(InventoryHolder previousHolder, Player player, ShopMeta shopMeta) {
        this.player = player;
        this.shopMeta = shopMeta;
        this.previousHolder = previousHolder;
        String title = GuiYaml.INSTANCE.getString("gui.title.collectedShopPageShopOptionPage");
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
            ((CollectedShopPage) previousHolder).send();
            return;
        }

        boolean isCollected;
        try {
            isCollected = guiService.getShopCollector(shopMeta.getUuid(), player.getUniqueId().toString()) != null;
        } catch (NotExistShopException e) {
            ((CollectedShopPage) previousHolder).send();
            return;
        }

        this.inventory.clear();
        if (isCollected) {
            ItemStack cancelCollect = GuiYaml.INSTANCE.getButton("gui.button.collectedShopPageShopOptionPage.cancelCollectShop");
            inventory.setItem(4, cancelCollect);
        } else {
            ItemStack collect = GuiYaml.INSTANCE.getButton("gui.button.collectedShopPageShopOptionPage.collectShop");
            inventory.setItem(13, collect);
        }

        ItemStack goShop = GuiYaml.INSTANCE.getButton("gui.button.collectedShopPageShopOptionPage.allGoodPage");
        inventory.setItem(31, goShop);
        if (shopMeta.getLocation() != null) {
            ItemStack goLocation = GuiYaml.INSTANCE.getButton("gui.button.collectedShopPageShopOptionPage.goLocation");
            inventory.setItem(22, goLocation);
        }

        ItemStack comment = GuiYaml.INSTANCE.getButton("gui.button.collectedShopPageShopOptionPage.commentShop");
        inventory.setItem(21, comment);
        ItemStack lookComment = GuiYaml.INSTANCE.getButton("gui.button.collectedShopPageShopOptionPage.shopCommentPage");
        inventory.setItem(23, lookComment);
        ItemStack back = GuiYaml.INSTANCE.getButton("gui.button.collectedShopPageShopOptionPage.back");
        inventory.setItem(49, back);
        player.openInventory(this.inventory);
    }

    public ShopMeta getShopMeta() {
        return shopMeta;
    }

    public InventoryHolder getPreviousHolder() {
        return previousHolder;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}

