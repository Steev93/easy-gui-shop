package pers.zhangyang.easyguishop.domain;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.enumration.BuyIconPageStatsEnum;
import pers.zhangyang.easyguishop.meta.IconMeta;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.GuiYaml;
import pers.zhangyang.easylibrary.base.BackAble;
import pers.zhangyang.easylibrary.base.GuiPage;
import pers.zhangyang.easylibrary.base.MultipleGuiPageBase;
import pers.zhangyang.easylibrary.exception.NotApplicableException;
import pers.zhangyang.easylibrary.exception.NotExistNextPageException;
import pers.zhangyang.easylibrary.exception.NotExistPreviousPageException;
import pers.zhangyang.easylibrary.util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManageIconPage extends MultipleGuiPageBase implements BackAble {

    private List<IconMeta> iconMetaList = new ArrayList<>();
    private int pageIndex;
    private BuyIconPageStatsEnum stats;
    private String searchContent;
    private ShopMeta shopMeta;

    public ManageIconPage(GuiPage previousHolder, Player player, ShopMeta shopMeta) {
        super(GuiYaml.INSTANCE.getString("gui.title.manageIconPage"), player, previousHolder, previousHolder.getOwner(),54);
        this.shopMeta = shopMeta;
        stats = BuyIconPageStatsEnum.NORMAL;
    }

    public void send() {

        this.stats = BuyIconPageStatsEnum.NORMAL;
        this.searchContent = null;
        this.pageIndex = 0;
        refresh();
    }

    public void searchByIconName(@NotNull String name) {
        this.stats = BuyIconPageStatsEnum.SEARCH_ICON_NAME;
        this.searchContent = name;
        this.pageIndex = 0;
        refresh();
    }


    public void refresh() {

        this.inventory.clear();

        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();


        this.shopMeta = guiService.getShop(shopMeta.getUuid());
        if (this.shopMeta == null) {
            backPage.send();
            return;
        }
        this.iconMetaList.clear();
        this.iconMetaList.addAll(guiService.listPlayerIcon(owner.getUniqueId().toString()));
        if (stats.equals(BuyIconPageStatsEnum.SEARCH_ICON_NAME)) {
            iconMetaList.removeIf(shopMeta2 -> !shopMeta2.getName().contains(searchContent));
        }
        int maxIndex = PageUtil.computeMaxPageIndex(iconMetaList.size(), 45);

        if (pageIndex > maxIndex) {
            this.pageIndex = maxIndex;
        }

        if (pageIndex > 0) {
            ItemStack previous = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageIconPage.previousPage");
            inventory.setItem(45, previous);
        } else {

            inventory.setItem(45, null);
        }
        if (pageIndex < maxIndex) {
            ItemStack next = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageIconPage.nextPage");
            inventory.setItem(53, next);
        } else {

            inventory.setItem(53, null);
        }


        this.iconMetaList = (PageUtil.page(pageIndex, 45, iconMetaList));
        //设置内容
        for (int i = 0; i < 45; i++) {
            if (i >= iconMetaList.size()) {
                break;
            }
            IconMeta iconMeta = iconMetaList.get(i);
            HashMap<String, String> rep = new HashMap<>();
            rep.put("{name}", iconMeta.getName());
            rep.put("{create_time}", TimeUtil.getTimeFromTimeMill(iconMeta.getCreateTime()));
            ItemStack itemStack;
            if (GuiYaml.INSTANCE.getBooleanDefault("gui.option.enableIconUseIconItem")) {
                itemStack = ItemStackUtil.itemStackDeserialize(iconMeta.getIconItemStack());
                ItemStack tem = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageIconPage.manageIconPageIconOptionPage");
                try {
                    ItemStackUtil.apply(tem, itemStack);
                } catch (NotApplicableException e) {
                    itemStack = tem;
                }
            } else {
                itemStack = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageIconPage.manageIconPageIconOptionPage");
            }
            ReplaceUtil.replaceDisplayName(itemStack, rep);
            ReplaceUtil.replaceLore(itemStack, rep);
            inventory.setItem(i, itemStack);
        }


        ItemStack search = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageIconPage.searchByIconName");
        inventory.setItem(50, search);
        ItemStack back = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageIconPage.back");
        inventory.setItem(49, back);
        ItemStack reset = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageIconPage.resetShopIcon");
        inventory.setItem(48, reset);
        viewer.openInventory(this.inventory);
    }


    public void nextPage() throws NotExistNextPageException {
        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();


        this.shopMeta = guiService.getShop(shopMeta.getUuid());
        if (this.shopMeta == null) {
            backPage.send();
            return;
        }
        this.iconMetaList = guiService.listPlayerIcon(owner.getUniqueId().toString());
        int maxIndex = PageUtil.computeMaxPageIndex(iconMetaList.size(), 45);
        if (maxIndex <= pageIndex) {
            throw new NotExistNextPageException();
        }
        this.pageIndex++;
        refresh();
    }

    public void previousPage() throws NotExistPreviousPageException {
        if (0 >= pageIndex) {
            throw new NotExistPreviousPageException();
        }
        this.pageIndex--;
        refresh();
    }

    public ShopMeta getShopMeta() {
        return shopMeta;
    }

    @NotNull
    public List<IconMeta> getIconMetaList() {
        return new ArrayList<>(iconMetaList);
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


    @Override
    public int getPreviousPageSlot() {
        return 45;
    }

    @Override
    public int getNextPageSlot() {
        return 53;
    }
    @Override
    public int getBackSlot() {
        return 49;
    }
}
