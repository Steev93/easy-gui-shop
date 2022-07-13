package pers.zhangyang.easyguishop.domain;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.exception.NotApplicableException;
import pers.zhangyang.easyguishop.exception.NotExistNextException;
import pers.zhangyang.easyguishop.exception.NotExistPreviousException;
import pers.zhangyang.easyguishop.meta.ItemStockMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.ItemStackUtil;
import pers.zhangyang.easyguishop.util.PageUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.GuiYaml;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManageItemStockPage implements InventoryHolder {

    private final Inventory inventory;
    private final List<ItemStockMeta> itemStockMetaList = new ArrayList<>();
    private final InventoryHolder previousHolder;
    private final Player player;
    private int pageIndex;

    public ManageItemStockPage(InventoryHolder previousHolder, Player player) {
        this.player = player;
        String title = GuiYaml.INSTANCE.getString("gui.title.manageItemStockPage");
        if (title == null) {
            this.inventory = Bukkit.createInventory(this, 54);
        } else {
            this.inventory = Bukkit.createInventory(this, 54, ChatColor.translateAlternateColorCodes('&', title));
        }
        initMenuBarWithoutChangePage();
        this.previousHolder = previousHolder;
    }

    public void send() throws SQLException {
        this.pageIndex = 0;
        refresh();
    }


    public void refresh() throws SQLException {
        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();
        this.itemStockMetaList.clear();
        this.itemStockMetaList.addAll(guiService.listPlayerItemStock(player.getUniqueId().toString()));

        refreshContent();
        if (pageIndex > 0) {
            ItemStack previous = GuiYaml.INSTANCE.getButton("gui.button.manageItemStockPage.previous");
            inventory.setItem(45, previous);
        } else {
            inventory.setItem(45, null);
        }
        int maxIndex = PageUtil.computeMaxPageIndex(itemStockMetaList.size(), 45);
        if (pageIndex < maxIndex) {
            ItemStack next = GuiYaml.INSTANCE.getButton("gui.button.manageItemStockPage.next");
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
        int pageMax = PageUtil.page(pageIndex, 45, new ArrayList<>(itemStockMetaList)).size();
        //设置内容
        for (int i = 45 * pageIndex; i < 45 + 45 * pageIndex; i++) {
            if (i >= pageMax + 45 * pageIndex) {
                break;
            }

            ItemStockMeta itemStockMeta = itemStockMetaList.get(i);
            ItemStack itemStack;
            if (GuiYaml.INSTANCE.getBooleanDefault("gui.option.enableItemStockUseItemStockItem")) {
                itemStack = ItemStackUtil.itemStackDeserialize(itemStockMeta.getItemStack());
                ItemStack tem = GuiYaml.INSTANCE.getButton("gui.button.manageItemStockPage.manageItemStockPageItemStockOptionPage");
                try {
                    ItemStackUtil.apply(tem, itemStack);
                } catch (NotApplicableException e) {
                    itemStack=tem;
                }
            } else {
                itemStack = GuiYaml.INSTANCE.getButton("gui.button.manageItemStockPage.manageItemStockPageItemStockOptionPage");
            }
            inventory.setItem(i - 45 * pageIndex, itemStack);
        }
    }

    //渲染当前页的菜单(不包括翻页)
    private void initMenuBarWithoutChangePage() {
        ItemStack back = GuiYaml.INSTANCE.getButton("gui.button.manageItemStockPage.back");
        inventory.setItem(49, back);
        ItemStack createItemStock = GuiYaml.INSTANCE.getButton("gui.button.manageItemStockPage.createItemStock");
        inventory.setItem(51, createItemStock);
        ItemStack goBankLocation = GuiYaml.INSTANCE.getButton("gui.button.manageItemStockPage.goBankLocation");
        inventory.setItem(47, goBankLocation);

    }


    public void nextPage() throws NotExistNextException, SQLException {
        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();
        this.itemStockMetaList.clear();
        this.itemStockMetaList.addAll(guiService.listPlayerItemStock(player.getUniqueId().toString()));
        int maxIndex = PageUtil.computeMaxPageIndex(itemStockMetaList.size(), 45);
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
    public List<ItemStockMeta> getItemStockMetaList() {
        return new ArrayList<>(itemStockMetaList);
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