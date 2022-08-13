package pers.zhangyang.easyguishop.domain;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.enumration.ManageShopPageStatsEnum;
import pers.zhangyang.easyguishop.meta.IconMeta;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.GuiYaml;
import pers.zhangyang.easyguishop.yaml.SettingYaml;
import pers.zhangyang.easylibrary.base.BackAble;
import pers.zhangyang.easylibrary.base.GuiPage;
import pers.zhangyang.easylibrary.base.MultipleGuiPageBase;
import pers.zhangyang.easylibrary.exception.NotApplicableException;
import pers.zhangyang.easylibrary.exception.NotExistNextPageException;
import pers.zhangyang.easylibrary.exception.NotExistPreviousPageException;
import pers.zhangyang.easylibrary.util.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ManageShopPage extends MultipleGuiPageBase implements BackAble {
    private List<ShopMeta> shopMetaList = new ArrayList<>();
    private int pageIndex;
    private ManageShopPageStatsEnum stats;
    private String searchContent;

    public ManageShopPage(GuiPage previousHolder, Player player) {
        super(GuiYaml.INSTANCE.getString("gui.title.manageShopPage"), player, previousHolder, previousHolder.getOwner());
        stats = ManageShopPageStatsEnum.NORMAL;
    }

    public void send() {
        this.stats = ManageShopPageStatsEnum.NORMAL;
        this.searchContent = null;
        this.pageIndex = 0;
        refresh();
    }

    public void searchByShopName(@NotNull String name) {
        this.stats = ManageShopPageStatsEnum.SEARCH_SHOP_NAME;
        this.searchContent = name;
        this.pageIndex = 0;
        refresh();
    }


    public void refresh() {

        this.inventory.clear();

        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();

        this.shopMetaList.clear();
        this.shopMetaList.addAll(guiService.listPlayerShop(owner.getUniqueId().toString()));

        if (stats.equals(ManageShopPageStatsEnum.SEARCH_SHOP_NAME)) {
            this.shopMetaList.removeIf(shopMeta -> !shopMeta.getName().contains(searchContent));
        }
        int maxIndex = PageUtil.computeMaxPageIndex(this.shopMetaList.size(), 45);

        if (pageIndex > maxIndex) {
            this.pageIndex = maxIndex;
        }


        if (pageIndex > 0) {
            ItemStack previous = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageShopPage.previousPage");
            inventory.setItem(45, previous);
        } else {

            inventory.setItem(45, null);
        }
        if (pageIndex < maxIndex) {
            ItemStack next = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageShopPage.nextPage");
            inventory.setItem(53, next);
        } else {

            inventory.setItem(53, null);
        }


        this.shopMetaList = (PageUtil.page(pageIndex, 45, shopMetaList));
        //设置内容
        for (int i = 0; i < 45; i++) {
            if (i >= shopMetaList.size()) {
                break;
            }
            ShopMeta shopMeta = shopMetaList.get(i);
            OfflinePlayer owner = Bukkit.getOfflinePlayer(UUID.fromString(shopMeta.getOwnerUuid()));
            HashMap<String, String> rep = new HashMap<>();
            rep.put("{owner_name}", owner.getName() == null ? "/" : owner.getName());
            rep.put("{name}", shopMeta.getName());
            rep.put("{collect_amount}", String.valueOf(shopMeta.getCollectAmount()));
            rep.put("{create_time}", TimeUtil.getTimeFromTimeMill(shopMeta.getCreateTime()));
            rep.put("{popularity}", String.valueOf(shopMeta.getPopularity()));
            rep.put("{page_view}", String.valueOf(shopMeta.getPageView()));
            rep.put("{hot_value}", String.valueOf(shopMeta.getPageView() * SettingYaml.INSTANCE.getNonnegativeIntegerDefault("setting.hotValueCoefficient.pageView")
                    + shopMeta.getPopularity() * SettingYaml.INSTANCE.getNonnegativeIntegerDefault("setting.hotValueCoefficient.popularity")
                    + shopMeta.getCollectAmount() * SettingYaml.INSTANCE.getNonnegativeIntegerDefault("setting.hotValueCoefficient.collectAmount")));
            ItemStack itemStack;
            if (shopMeta.getIconUuid() != null) {
                IconMeta iconMeta = guiService.getIcon(shopMeta.getIconUuid());
                if (iconMeta == null) {
                    refresh();
                    return;
                }
                itemStack = ItemStackUtil.itemStackDeserialize(iconMeta.getIconItemStack());
                ItemStack tem = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageShopPage.manageShopPageShopOptionPage");
                try {
                    ItemStackUtil.apply(tem, itemStack);
                } catch (NotApplicableException e) {
                    itemStack = tem;
                }

            } else {
                if (GuiYaml.INSTANCE.getBooleanDefault("gui.option.enableShopUsePlayerHead")) {
                    itemStack = PlayerUtil.getPlayerSkullItem(owner);
                    ItemStack tem = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageShopPage.manageShopPageShopOptionPage");
                    try {
                        ItemStackUtil.apply(tem, itemStack);
                    } catch (NotApplicableException e) {
                        itemStack = tem;
                    }
                } else {
                    itemStack = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageShopPage.manageShopPageShopOptionPage");
                }
            }
            Gson gson = new Gson();
            Type stringListType = new TypeToken<ArrayList<String>>() {
            }.getType();
            List<String> stringList = gson.fromJson(shopMeta.getDescription(), stringListType);
            ReplaceUtil.formatLore(itemStack, "{(description)}", stringList);

            ReplaceUtil.replaceDisplayName(itemStack, rep);
            ReplaceUtil.replaceLore(itemStack, rep);
            inventory.setItem(i, itemStack);
        }


        ItemStack collected = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageShopPage.collectedShopPage");
        inventory.setItem(46, collected);
        ItemStack search = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageShopPage.searchByShopName");
        inventory.setItem(47, search);
        ItemStack createShop = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageShopPage.createShop");
        inventory.setItem(48, createShop);
        ItemStack manager = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageShopPage.deleteShop");
        inventory.setItem(50, manager);
        ItemStack back = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageShopPage.back");
        inventory.setItem(49, back);
        ItemStack buyIcon = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageShopPage.buyIconPage");
        inventory.setItem(51, buyIcon);
        viewer.openInventory(this.inventory);
    }


    public void nextPage() throws NotExistNextPageException {
        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();

        this.shopMetaList = guiService.listPlayerShop(owner.getUniqueId().toString());
        int maxIndex = PageUtil.computeMaxPageIndex(shopMetaList.size(), 45);
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
    public List<ShopMeta> getShopMetaList() {
        return new ArrayList<>(shopMetaList);
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
