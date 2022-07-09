package pers.zhangyang.easyguishop.domain;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.enumration.BuyIconPageStatsEnum;
import pers.zhangyang.easyguishop.exception.NotExistNextException;
import pers.zhangyang.easyguishop.exception.NotExistPreviousException;
import pers.zhangyang.easyguishop.meta.IconMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.*;
import pers.zhangyang.easyguishop.yaml.GuiYaml;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BuyIconPage implements InventoryHolder {

    private final Inventory inventory;
    private final List<IconMeta> iconMetaList = new ArrayList<>();
    private final InventoryHolder previousHolder;
    private final Player player;
    private int pageIndex;
    private BuyIconPageStatsEnum stats;
    private String searchContent;

    public BuyIconPage(InventoryHolder previousHolder, Player player) {
        this.player = player;
        this.previousHolder = previousHolder;
        String title = GuiYaml.INSTANCE.getString("gui.title.buyIconPage");
        if (title == null) {
            this.inventory = Bukkit.createInventory(this, 54);
        } else {
            this.inventory = Bukkit.createInventory(this, 54, ChatColor.translateAlternateColorCodes('&', title));
        }
        stats = BuyIconPageStatsEnum.NORMAL;
        initMenuBarWithoutChangePage();
    }

    public void send() throws SQLException {
        this.stats = BuyIconPageStatsEnum.NORMAL;
        this.searchContent = null;
        this.pageIndex = 0;
        refresh();
    }

    public void searchByIconName(@NotNull String name) throws SQLException {
        this.stats = BuyIconPageStatsEnum.SEARCH_ICON_NAME;
        this.searchContent = name;
        this.pageIndex = 0;
        refresh();
    }


    public void refresh() throws SQLException {

        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();

        this.iconMetaList.clear();
        this.iconMetaList.addAll(guiService.listIcon());
        if (stats.equals(BuyIconPageStatsEnum.SEARCH_ICON_NAME)) {
            this.iconMetaList.removeIf(shopMeta -> !shopMeta.getName().contains(searchContent));
        }
        int maxIndex = PageUtil.computeMaxPageIndex(this.iconMetaList.size(), 45);

        if (pageIndex > maxIndex) {
            this.pageIndex = maxIndex;
        }

        refreshContent();
        if (pageIndex > 0) {
            ItemStack previous = GuiYaml.INSTANCE.getButton("gui.button.buyIconPage.previous");
            inventory.setItem(45, previous);
        } else {

            inventory.setItem(45, null);
        }
        if (pageIndex < maxIndex) {
            ItemStack next = GuiYaml.INSTANCE.getButton("gui.button.buyIconPage.next");
            inventory.setItem(53, next);
        } else {

            inventory.setItem(53, null);
        }
        player.openInventory(this.inventory);
    }

    //根据shopMetaList渲染当前页的0-44
    private void refreshContent() {
        for (int i = 0; i < 45; i++) {
            inventory.setItem(i, null);
        }

        int pageMax = PageUtil.page(pageIndex, 45, new ArrayList<>(iconMetaList)).size();
        //设置内容
        for (int i = 45 * pageIndex; i < 45 + 45 * pageIndex; i++) {
            if (i >= pageMax + 45 * pageIndex) {
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
                ItemStack tem = GuiYaml.INSTANCE.getButton("gui.button.buyIconPage.buyIconPageIconOptionPage");
                ItemStackUtil.apply(tem, itemStack);
            } else {
                itemStack = GuiYaml.INSTANCE.getButton("gui.button.buyIconPage.buyIconPageIconOptionPage");
            }
            ReplaceUtil.replaceDisplayName(itemStack, rep);
            ReplaceUtil.replaceLore(itemStack, rep);
            inventory.setItem(i - 45 * pageIndex, itemStack);
        }
    }

    //渲染当前页的菜单(不包括翻页)
    private void initMenuBarWithoutChangePage() {
        ItemStack search = GuiYaml.INSTANCE.getButton("gui.button.buyIconPage.searchByIconName");
        inventory.setItem(50, search);
        ItemStack back = GuiYaml.INSTANCE.getButton("gui.button.buyIconPage.back");
        inventory.setItem(49, back);
    }


    public void nextPage() throws NotExistNextException, SQLException {
        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();
        this.iconMetaList.clear();
        this.iconMetaList.addAll(guiService.listIcon());
        int maxIndex = PageUtil.computeMaxPageIndex(iconMetaList.size(), 45);
        if (maxIndex <= pageIndex) {
            throw new NotExistNextException();
        }
        this.pageIndex++;
        refresh();
    }

    public void previousPage() throws NotExistPreviousException, SQLException {
        if (0 >= pageIndex) {
            throw new NotExistPreviousException();
        }
        this.pageIndex--;
        refresh();
    }


    @NotNull
    public List<IconMeta> getIconMetaList() {
        return new ArrayList<>(iconMetaList);
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
