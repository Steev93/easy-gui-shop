package pers.zhangyang.easyguishop.domain;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.meta.IconMeta;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.GuiYaml;
import pers.zhangyang.easylibrary.base.BackAble;
import pers.zhangyang.easylibrary.base.GuiPage;
import pers.zhangyang.easylibrary.base.SingleGuiPageBase;
import pers.zhangyang.easylibrary.util.ItemStackUtil;
import pers.zhangyang.easylibrary.util.ReplaceUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

import java.util.HashMap;

public class ManageIconPageIconOptionPage extends SingleGuiPageBase implements BackAble {
    private IconMeta iconMeta;
    private ShopMeta shopMeta;

    public ManageIconPageIconOptionPage(GuiPage previousHolder, Player player, IconMeta iconMeta, ShopMeta shopMeta) {
        super(GuiYaml.INSTANCE.getString("gui.title.manageIconPageIconOptionPage"), player, previousHolder, previousHolder.getOwner());
        this.iconMeta = iconMeta;
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
        this.iconMeta = guiService.getIcon(iconMeta.getUuid());
        if (this.iconMeta == null) {
            backPage.send();
            return;
        }

        this.inventory.clear();
        ItemStack currencyGuide = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageIconPageIconOptionPage.guideIcon");
        inventory.setItem(13, currencyGuide);

        HashMap<String, String> rep = new HashMap<>();
        rep.put("{name}", iconMeta.getName());

        ItemStack information = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageIconPageIconOptionPage.iconInformation");
        ReplaceUtil.replaceLore(information, rep);
        ReplaceUtil.replaceDisplayName(information, rep);
        inventory.setItem(31, information);

        ItemStack icon = ItemStackUtil.itemStackDeserialize(iconMeta.getIconItemStack());
        inventory.setItem(22, icon);

        ItemStack buyIcon = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageIconPageIconOptionPage.useShopIcon");
        inventory.setItem(40, buyIcon);


        ItemStack back = GuiYaml.INSTANCE.getButtonDefault("gui.button.buyIconPageIconOptionPage.back");
        inventory.setItem(49, back);
        viewer.openInventory(this.inventory);
    }

    public ShopMeta getShopMeta() {
        return shopMeta;
    }

    public IconMeta getIconMeta() {
        return iconMeta;
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
