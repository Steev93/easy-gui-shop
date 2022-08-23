package pers.zhangyang.easyguishop.domain;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.meta.ItemStockMeta;
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

public class ManageItemStockPageItemStockOptionPage extends SingleGuiPageBase implements BackAble {
    private ItemStockMeta itemStockMeta;

    public ManageItemStockPageItemStockOptionPage(GuiPage previousHolder, Player player, ItemStockMeta shopMeta) {
        super(GuiYaml.INSTANCE.getString("gui.title.manageItemStockPageItemStockOptionPage"), player, previousHolder, previousHolder.getOwner(),54);
        this.itemStockMeta = shopMeta;
    }


    @Override
    public void refresh() {
        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
        this.itemStockMeta = guiService.getItemStock(itemStockMeta.getPlayerUuid(), itemStockMeta.getItemStack());
        if (this.itemStockMeta == null) {
            backPage.send();
            return;
        }
        this.inventory.clear();
        ItemStack itemStock = ItemStackUtil.itemStackDeserialize(itemStockMeta.getItemStack());
        inventory.setItem(4, itemStock);
        ItemStack guideItemStock = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageItemStockPageItemStockOptionPage.guideItemStock");
        inventory.setItem(13, guideItemStock);

        ItemStack information = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageItemStockPageItemStockOptionPage.itemStockInformation");
        HashMap<String, String> rep = new HashMap<>();
        rep.put("{amount}", String.valueOf(itemStockMeta.getAmount()));
        ReplaceUtil.replaceDisplayName(information, rep);
        ReplaceUtil.replaceLore(information, rep);
        inventory.setItem(31, information);
        ItemStack depositItemStock = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageItemStockPageItemStockOptionPage.depositItemStock");
        inventory.setItem(22, depositItemStock);

        if (itemStockMeta.getAmount() > 0) {
            ItemStack takeItemStock = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageItemStockPageItemStockOptionPage.takeItemStock");
            inventory.setItem(21, takeItemStock);
        }
        if (itemStockMeta.getAmount() <= 0) {
            ItemStack deleteItemStock = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageItemStockPageItemStockOptionPage.deleteItemStock");
            inventory.setItem(23, deleteItemStock);
        }
        ItemStack back = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageItemStockPageItemStockOptionPage.back");
        inventory.setItem(49, back);


        viewer.openInventory(this.inventory);
    }

    public ItemStockMeta getItemStockMeta() {
        return itemStockMeta;
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
