package pers.zhangyang.easyguishop.domain;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.exception.NotExistShopException;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.GuiYaml;
import pers.zhangyang.easylibrary.base.BackAble;
import pers.zhangyang.easylibrary.base.GuiPage;
import pers.zhangyang.easylibrary.base.SingleGuiPageBase;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

public class CollectedShopPageShopOptionPage extends SingleGuiPageBase implements BackAble {
    private ShopMeta shopMeta;

    public CollectedShopPageShopOptionPage(GuiPage backPage, Player viewer, ShopMeta shopMeta) {
        super(GuiYaml.INSTANCE.getString("gui.title.collectedShopPageShopOptionPage"), viewer, backPage, backPage.getOwner());
        this.shopMeta = shopMeta;

    }


    @Override
    public void refresh() {
        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
        this.shopMeta = guiService.getShop(shopMeta.getUuid());
        if (this.shopMeta == null) {
            backPage.send();
            return;
        }

        boolean isCollected;
        try {
            isCollected = guiService.getShopCollector(shopMeta.getUuid(), owner.getUniqueId().toString()) != null;
        } catch (NotExistShopException e) {
            backPage.send();
            return;
        }

        this.inventory.clear();
        if (isCollected) {
            ItemStack cancelCollect = GuiYaml.INSTANCE.getButtonDefault("gui.button.collectedShopPageShopOptionPage.cancelCollectShop");
            inventory.setItem(4, cancelCollect);
        } else {
            ItemStack collect = GuiYaml.INSTANCE.getButtonDefault("gui.button.collectedShopPageShopOptionPage.collectShop");
            inventory.setItem(13, collect);
        }

        ItemStack goShop = GuiYaml.INSTANCE.getButtonDefault("gui.button.collectedShopPageShopOptionPage.allGoodPage");
        inventory.setItem(31, goShop);
        if (shopMeta.getLocation() != null) {
            ItemStack goLocation = GuiYaml.INSTANCE.getButtonDefault("gui.button.collectedShopPageShopOptionPage.goShopLocation");
            inventory.setItem(22, goLocation);
        }

        ItemStack comment = GuiYaml.INSTANCE.getButtonDefault("gui.button.collectedShopPageShopOptionPage.commentShop");
        inventory.setItem(21, comment);
        ItemStack lookComment = GuiYaml.INSTANCE.getButtonDefault("gui.button.collectedShopPageShopOptionPage.allShopCommentPage");
        inventory.setItem(23, lookComment);
        ItemStack back = GuiYaml.INSTANCE.getButtonDefault("gui.button.collectedShopPageShopOptionPage.back");
        inventory.setItem(49, back);
        viewer.openInventory(this.inventory);
    }

    public ShopMeta getShopMeta() {
        return shopMeta;
    }

    public InventoryHolder getPreviousHolder() {
        return backPage;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void back() {
        backPage.refresh();
    }
}

