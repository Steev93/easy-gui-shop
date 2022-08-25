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

public class AllShopPageShopOptionPage extends SingleGuiPageBase implements BackAble {
    private ShopMeta shopMeta;

    public AllShopPageShopOptionPage(GuiPage backPage, Player viewer, ShopMeta shopMeta) {
        super(GuiYaml.INSTANCE.getString("gui.title.allShopPageShopOptionPage"), viewer, backPage, backPage.getOwner(),54);
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
            ItemStack cancelCollect = GuiYaml.INSTANCE.getButtonDefault("gui.button.allShopPageShopOptionPage.cancelCollectShop");
            inventory.setItem(4, cancelCollect);

            ItemStack collect = GuiYaml.INSTANCE.getButtonDefault("gui.button.allShopPageShopOptionPage.collectShop");
            inventory.setItem(13, collect);

        ItemStack goShop = GuiYaml.INSTANCE.getButtonDefault("gui.button.allShopPageShopOptionPage.allGoodPage");
        inventory.setItem(31, goShop);
            ItemStack goLocation = GuiYaml.INSTANCE.getButtonDefault("gui.button.allShopPageShopOptionPage.teleportShopLocation");
            inventory.setItem(22, goLocation);



        ItemStack comment = GuiYaml.INSTANCE.getButtonDefault("gui.button.allShopPageShopOptionPage.commentShop");
        inventory.setItem(21, comment);
        ItemStack lookComment = GuiYaml.INSTANCE.getButtonDefault("gui.button.allShopPageShopOptionPage.allShopCommentPage");
        inventory.setItem(23, lookComment);
        ItemStack back = GuiYaml.INSTANCE.getButtonDefault("gui.button.allShopPageShopOptionPage.back");
        inventory.setItem(49, back);
        viewer.openInventory(this.inventory);
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
        return backPage;
    }

    @Override
    public void back() {
        backPage.refresh();
    }



    @Override
    public int getBackSlot() {
        return 49;
    }
}
