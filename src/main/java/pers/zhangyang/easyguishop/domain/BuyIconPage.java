package pers.zhangyang.easyguishop.domain;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.enumration.BuyIconPageStatsEnum;
import pers.zhangyang.easyguishop.meta.IconMeta;
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

public class BuyIconPage extends MultipleGuiPageBase implements BackAble {

    private List<IconMeta> iconMetaList = new ArrayList<>();
    private int pageIndex;
    private BuyIconPageStatsEnum stats;
    private String searchContent;

    public BuyIconPage(GuiPage backPage, Player player) {
        super(GuiYaml.INSTANCE.getString("gui.title.buyIconPage"), player, backPage, backPage.getOwner(),54);
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

        this.iconMetaList.clear();
        this.iconMetaList.addAll(guiService.listIcon());
        if (stats.equals(BuyIconPageStatsEnum.SEARCH_ICON_NAME)) {
            this.iconMetaList.removeIf(shopMeta -> !shopMeta.getName().contains(searchContent));
        }
        int maxIndex = PageUtil.computeMaxPageIndex(this.iconMetaList.size(), 45);

        if (pageIndex > maxIndex) {
            this.pageIndex = maxIndex;
        }

        if (pageIndex > 0) {
            ItemStack previous = GuiYaml.INSTANCE.getButtonDefault("gui.button.buyIconPage.previousPage");
            inventory.setItem(45, previous);
        } else {

            inventory.setItem(45, null);
        }
        if (pageIndex < maxIndex) {
            ItemStack next = GuiYaml.INSTANCE.getButtonDefault("gui.button.buyIconPage.nextPage");
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
            if (iconMeta.isSystem()) {
                rep.put("{stock}", GuiYaml.INSTANCE.getStringDefault("gui.replace.systemStock"));
            } else {
                rep.put("{stock}", String.valueOf(iconMeta.getStock()));
            }
            if (iconMeta.getLimitTime() != null) {
                rep.put("{limit_time}", String.valueOf(iconMeta.getLimitTime()));
            } else {
                rep.put("{limit_time}", "\\");
            }
            rep.put("{create_time}", TimeUtil.getTimeFromTimeMill(iconMeta.getCreateTime()));
            ItemStack itemStack;
            if (GuiYaml.INSTANCE.getBooleanDefault("gui.option.enableIconUseIconItem")) {
                itemStack = ItemStackUtil.itemStackDeserialize(iconMeta.getIconItemStack());
                ItemStack tem = GuiYaml.INSTANCE.getButtonDefault("gui.button.buyIconPage.buyIconPageIconOptionPage");
                try {
                    ItemStackUtil.apply(tem, itemStack);
                } catch (NotApplicableException e) {
                    itemStack = tem;
                }
            } else {
                itemStack = GuiYaml.INSTANCE.getButtonDefault("gui.button.buyIconPage.buyIconPageIconOptionPage");
            }
            ReplaceUtil.replaceDisplayName(itemStack, rep);
            ReplaceUtil.replaceLore(itemStack, rep);
            inventory.setItem(i, itemStack);
        }


        ItemStack search = GuiYaml.INSTANCE.getButtonDefault("gui.button.buyIconPage.searchByIconName");
        inventory.setItem(50, search);
        ItemStack back = GuiYaml.INSTANCE.getButtonDefault("gui.button.buyIconPage.back");
        inventory.setItem(49, back);
        viewer.openInventory(this.inventory);
    }


    public void nextPage() throws NotExistNextPageException {
        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
        this.iconMetaList = guiService.listIcon();
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
