package pers.zhangyang.easyguishop.domain;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.enumration.BuyIconPageStatsEnum;
import pers.zhangyang.easyguishop.exception.NotApplicableException;
import pers.zhangyang.easyguishop.exception.NotExistNextException;
import pers.zhangyang.easyguishop.exception.NotExistPreviousException;
import pers.zhangyang.easyguishop.exception.NotExistShopException;
import pers.zhangyang.easyguishop.meta.IconMeta;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.*;
import pers.zhangyang.easyguishop.yaml.GuiYaml;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManageIconPage implements InventoryHolder {

    private final Inventory inventory;
    private final List<IconMeta> iconMetaList = new ArrayList<>();
    private final InventoryHolder previousHolder;
    private final Player player;
    private int pageIndex;
    private BuyIconPageStatsEnum stats;
    private String searchContent;
    private ShopMeta shopMeta;

    public ManageIconPage(InventoryHolder previousHolder, Player player, ShopMeta shopMeta) {
        this.player = player;
        this.shopMeta = shopMeta;
        this.previousHolder = previousHolder;
        String title = GuiYaml.INSTANCE.getString("gui.title.manageIconPage");
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

    public void searchByIconName(@NotNull String name) throws NotExistShopException, SQLException {
        this.stats = BuyIconPageStatsEnum.SEARCH_ICON_NAME;
        this.searchContent = name;
        this.pageIndex = 0;
        refresh();
    }


    public void refresh() throws SQLException {
        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();


        this.shopMeta = guiService.getShop(shopMeta.getUuid());
        if (this.shopMeta == null) {
            ((ManageShopPageShopOptionPage) previousHolder).send();
            return;
        }
        this.iconMetaList.clear();
        this.iconMetaList.addAll(guiService.listPlayerIcon(player.getUniqueId().toString()));
        if (stats.equals(BuyIconPageStatsEnum.SEARCH_ICON_NAME)) {
            iconMetaList.removeIf(shopMeta2 -> !shopMeta2.getName().contains(searchContent));
        }
        int maxIndex = PageUtil.computeMaxPageIndex(iconMetaList.size(), 45);

        if (pageIndex > maxIndex) {
            this.pageIndex = maxIndex;
        }

        refreshContent();
        if (pageIndex > 0) {
            ItemStack previous = GuiYaml.INSTANCE.getButton("gui.button.manageIconPage.previous");
            inventory.setItem(45, previous);
        } else {

            inventory.setItem(45, null);
        }
        if (pageIndex < maxIndex) {
            ItemStack next = GuiYaml.INSTANCE.getButton("gui.button.manageIconPage.next");
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
            rep.put("{create_time}", TimeUtil.getTimeFromTimeMill(iconMeta.getCreateTime()));
            ItemStack itemStack;
            if (GuiYaml.INSTANCE.getBooleanDefault("gui.option.enableIconUseIconItem")) {
                itemStack = ItemStackUtil.itemStackDeserialize(iconMeta.getIconItemStack());
                ItemStack tem = GuiYaml.INSTANCE.getButton("gui.button.manageIconPage.manageIconPageIconOptionPage");
                try {
                    ItemStackUtil.apply(tem, itemStack);
                } catch (NotApplicableException e) {
                    itemStack = tem;
                }
            } else {
                itemStack = GuiYaml.INSTANCE.getButton("gui.button.manageIconPage.manageIconPageIconOptionPage");
            }
            ReplaceUtil.replaceDisplayName(itemStack, rep);
            ReplaceUtil.replaceLore(itemStack, rep);
            inventory.setItem(i - 45 * pageIndex, itemStack);
        }
    }

    //渲染当前页的菜单(不包括翻页)
    private void initMenuBarWithoutChangePage() {
        ItemStack search = GuiYaml.INSTANCE.getButton("gui.button.manageIconPage.searchByIconName");
        inventory.setItem(50, search);
        ItemStack back = GuiYaml.INSTANCE.getButton("gui.button.manageIconPage.back");
        inventory.setItem(49, back);
        ItemStack reset = GuiYaml.INSTANCE.getButton("gui.button.manageIconPage.resetShopIcon");
        inventory.setItem(48, reset);
    }


    public void nextPage() throws NotExistNextException, SQLException {
        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();


        this.shopMeta = guiService.getShop(shopMeta.getUuid());
        if (this.shopMeta == null) {
            ((ManageShopPageShopOptionPage) previousHolder).send();
            return;
        }
        this.iconMetaList.clear();
        this.iconMetaList.addAll(guiService.listPlayerIcon(player.getUniqueId().toString()));
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

    public ShopMeta getShopMeta() {
        return shopMeta;
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
