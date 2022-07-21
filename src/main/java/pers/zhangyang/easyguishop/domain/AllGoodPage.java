package pers.zhangyang.easyguishop.domain;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.enumration.ManageShopPageStatsEnum;
import pers.zhangyang.easyguishop.exception.NotApplicableException;
import pers.zhangyang.easyguishop.exception.NotExistNextException;
import pers.zhangyang.easyguishop.exception.NotExistPreviousException;
import pers.zhangyang.easyguishop.exception.NotExistShopException;
import pers.zhangyang.easyguishop.meta.GoodMeta;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.*;
import pers.zhangyang.easyguishop.yaml.GuiYaml;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllGoodPage implements InventoryHolder {

    private final Inventory inventory;
    private  List<GoodMeta> goodMetaList = new ArrayList<>();
    private final InventoryHolder previousHolder;
    private final Player viewer;
    private int pageIndex;
    private ManageShopPageStatsEnum stats;
    private String searchContent;
    private ShopMeta shopMeta;


    public AllGoodPage(InventoryHolder previousHolder, Player viewer, ShopMeta shopMeta) {
        this.viewer = viewer;
        this.shopMeta = shopMeta;
        this.previousHolder = previousHolder;
        String title = GuiYaml.INSTANCE.getString("gui.title.allGoodPage");
        if (title == null) {
            this.inventory = Bukkit.createInventory(this, 54);
        } else {
            this.inventory = Bukkit.createInventory(this, 54, ChatColor.translateAlternateColorCodes('&', title));
        }
        stats = ManageShopPageStatsEnum.NORMAL;
        initMenuBarWithoutChangePage();
    }

    public void send() throws SQLException {
        this.stats = ManageShopPageStatsEnum.NORMAL;
        this.searchContent = null;
        this.pageIndex = 0;
        refresh();
    }

    public void searchByGooName(@NotNull String name) throws SQLException {
        this.stats = ManageShopPageStatsEnum.SEARCH_SHOP_NAME;
        this.searchContent = name;
        this.pageIndex = 0;
        refresh();
    }


    public void refresh() throws SQLException {

        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();

        this.shopMeta = guiService.getShop(this.shopMeta.getUuid());
        if (this.shopMeta == null) {
            if (previousHolder instanceof AllShopPageShopOptionPage) {
                ((AllShopPageShopOptionPage) previousHolder).send();
            }
            if (previousHolder instanceof CollectedShopPageShopOptionPage) {
                ((CollectedShopPageShopOptionPage) previousHolder).send();
            }
            return;
        }


        this.goodMetaList.clear();
        try {
            this.goodMetaList.addAll(guiService.listShopGood(shopMeta.getUuid()));
        } catch (NotExistShopException e) {
            if (previousHolder instanceof AllShopPageShopOptionPage) {
                ((AllShopPageShopOptionPage) previousHolder).send();
            }
            if (previousHolder instanceof CollectedShopPageShopOptionPage) {
                ((CollectedShopPageShopOptionPage) previousHolder).send();
            }
            return;
        }

        if (stats.equals(ManageShopPageStatsEnum.SEARCH_SHOP_NAME)) {
            this.goodMetaList.removeIf(shopMeta2 -> !shopMeta2.getName().contains(searchContent));
        }
        int maxIndex = PageUtil.computeMaxPageIndex(this.goodMetaList.size(), 45);

        if (pageIndex > maxIndex) {
            this.pageIndex = maxIndex;
        }

        refreshContent();
        if (pageIndex > 0) {
            ItemStack previous = GuiYaml.INSTANCE.getButton("gui.button.allGoodPage.previous");
            inventory.setItem(45, previous);
        } else {

            inventory.setItem(45, null);
        }
        if (pageIndex < maxIndex) {
            ItemStack next = GuiYaml.INSTANCE.getButton("gui.button.allGoodPage.next");
            inventory.setItem(53, next);
        } else {

            inventory.setItem(53, null);
        }
        viewer.openInventory(this.inventory);
    }

    //根据shopMetaList渲染当前页的0-44
    private void refreshContent() {
        for (int i = 0; i < 45; i++) {
            inventory.setItem(i, null);
        }

        this.goodMetaList=(PageUtil.page(pageIndex, 45,goodMetaList));
        //设置内容
        for (int i = 0 ; i < 45 ; i++) {
            if (i >= goodMetaList.size() ) {
                break;
            }
            GoodMeta goodMeta = goodMetaList.get(i);
            HashMap<String, String> rep = new HashMap<>();
            rep.put("{name}", goodMeta.getName());
            rep.put("{type}", goodMeta.getType());
            if (goodMeta.isSystem()) {
                rep.put("{stock}", GuiYaml.INSTANCE.getStringDefault("gui.replace.systemStock"));
            } else {
                rep.put("{stock}", String.valueOf(goodMeta.getStock()));
            }
            if (goodMeta.getLimitTime() != null) {
                rep.put("{limit_time}", String.valueOf(goodMeta.getLimitTime()));
            } else {
                rep.put("{limit_time}", "\\");
            }
            rep.put("{create_time}", TimeUtil.getTimeFromTimeMill(goodMeta.getCreateTime()));
            ItemStack itemStack;
            if (GuiYaml.INSTANCE.getBooleanDefault("gui.option.enableGoodUseGoodItem")) {
                itemStack = ItemStackUtil.itemStackDeserialize(goodMeta.getGoodItemStack());
                ItemStack tem = GuiYaml.INSTANCE.getButton("gui.button.allGoodPage.allGoodPageGoodOptionPage");
                try {
                    ItemStackUtil.apply(tem, itemStack);
                } catch (NotApplicableException e) {
                    itemStack = tem;
                }
            } else {
                itemStack = GuiYaml.INSTANCE.getButton("gui.button.allGoodPage.allGoodPageGoodOptionPage");
            }
            ReplaceUtil.replaceDisplayName(itemStack, rep);
            ReplaceUtil.replaceLore(itemStack, rep);
            inventory.setItem(i, itemStack);
        }
    }

    //渲染当前页的菜单(不包括翻页)
    private void initMenuBarWithoutChangePage() {
        ItemStack search = GuiYaml.INSTANCE.getButton("gui.button.allGoodPage.searchByGoodName");
        inventory.setItem(47, search);
        ItemStack back = GuiYaml.INSTANCE.getButton("gui.button.allGoodPage.back");
        inventory.setItem(49, back);
    }


    public void nextPage() throws NotExistNextException, SQLException {
        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();
        this.shopMeta = guiService.getShop(this.shopMeta.getUuid());
        if (this.shopMeta == null) {
            if (previousHolder instanceof AllShopPageShopOptionPage) {
                ((AllShopPageShopOptionPage) previousHolder).send();
            }
            if (previousHolder instanceof CollectedShopPageShopOptionPage) {
                ((CollectedShopPageShopOptionPage) previousHolder).send();
            }
            return;
        }
        this.goodMetaList.clear();
        try {
            this.goodMetaList.addAll(guiService.listShopGood(this.shopMeta.getUuid()));
        } catch (NotExistShopException e) {
            if (previousHolder instanceof AllShopPageShopOptionPage) {
                ((AllShopPageShopOptionPage) previousHolder).send();
            }
            if (previousHolder instanceof CollectedShopPageShopOptionPage) {
                ((CollectedShopPageShopOptionPage) previousHolder).send();
            }
            return;
        }

        int maxIndex = PageUtil.computeMaxPageIndex(goodMetaList.size(), 45);
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
    public List<GoodMeta> getGoodMetaList() {
        return new ArrayList<>(goodMetaList);
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

