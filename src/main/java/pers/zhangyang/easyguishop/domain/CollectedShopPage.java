package pers.zhangyang.easyguishop.domain;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.enumration.CollectedShopPageEnum;
import pers.zhangyang.easyguishop.exception.NotApplicableException;
import pers.zhangyang.easyguishop.exception.NotExistNextException;
import pers.zhangyang.easyguishop.exception.NotExistPreviousException;
import pers.zhangyang.easyguishop.meta.IconMeta;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.*;
import pers.zhangyang.easyguishop.yaml.GuiYaml;
import pers.zhangyang.easyguishop.yaml.SettingYaml;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.*;

public class CollectedShopPage implements InventoryHolder {

    private final Inventory inventory;
    private  List<ShopMeta> shopMetaList = new ArrayList<>();
    private final InventoryHolder previousHolder;
    private final Player player;
    private int pageIndex;
    private CollectedShopPageEnum stats;
    private String searchContent;

    public CollectedShopPage(InventoryHolder previousHolder, Player player) {
        this.player = player;
        this.previousHolder = previousHolder;
        String title = GuiYaml.INSTANCE.getString("gui.title.collectedShopPage");
        if (title == null) {
            this.inventory = Bukkit.createInventory(this, 54);
        } else {
            this.inventory = Bukkit.createInventory(this, 54, ChatColor.translateAlternateColorCodes('&', title));
        }
        stats = CollectedShopPageEnum.NORMAL;
        initMenuBarWithoutChangePage();
    }


    public void searchByShopName(@NotNull String name) throws SQLException {
        this.stats = CollectedShopPageEnum.SEARCH_SHOP_NAME;
        this.searchContent = name;
        this.pageIndex = 0;
        refresh();
    }

    public void searchByShopOwnerName(@NotNull String ownerName) throws SQLException {
        this.stats = CollectedShopPageEnum.SEARCH_OWNER_NAME;
        this.searchContent = ownerName;
        this.pageIndex = 0;
        refresh();
    }


    public void refresh() throws SQLException {

        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();

        this.shopMetaList.clear();
        this.shopMetaList.addAll(guiService.listPlayerCollectedShop(player.getUniqueId().toString()));
        refreshContent();
        if (pageIndex > 0) {
            ItemStack previous = GuiYaml.INSTANCE.getButton("gui.button.collectedShopPage.previous");
            inventory.setItem(45, previous);
        } else {
            inventory.setItem(45, null);
        }
        if (stats.equals(CollectedShopPageEnum.SEARCH_SHOP_NAME)) {
            this.shopMetaList.removeIf(shopMeta -> !shopMeta.getName().contains(searchContent));
        }
        if (stats.equals(CollectedShopPageEnum.SEARCH_OWNER_NAME)) {
            this.shopMetaList.removeIf(shopMeta -> !Objects.requireNonNull(Bukkit.getOfflinePlayer(UUID.fromString(shopMeta.getOwnerUuid())).getName()).contains(searchContent));
        }
        int maxIndex = PageUtil.computeMaxPageIndex(shopMetaList.size(), 45);
        if (pageIndex < maxIndex) {
            ItemStack next = GuiYaml.INSTANCE.getButton("gui.button.collectedShopPage.next");
            inventory.setItem(53, next);
        } else {
            inventory.setItem(53, null);
        }
        player.openInventory(this.inventory);
    }

    //根据shopMetaList渲染当前页的0-44

    private void refreshContent() throws SQLException {
        for (int i = 0; i < 45; i++) {
            inventory.setItem(i, null);
        }
        this.shopMetaList=(PageUtil.page(pageIndex, 45,shopMetaList));
        //设置内容
        for (int i = 0; i < 45 ; i++) {
            if (i >= shopMetaList.size()) {
                break;
            }
            ShopMeta shopMeta = shopMetaList.get(i);
            OfflinePlayer owner = Bukkit.getOfflinePlayer(UUID.fromString(shopMeta.getOwnerUuid()));
            HashMap<String, String> rep = new HashMap<>();
            rep.put("{owner_name}", String.valueOf(owner.getName()));
            rep.put("{name}", shopMeta.getName());
            rep.put("{collect_amount}", String.valueOf(shopMeta.getCollectAmount()));
            rep.put("{create_time}", TimeUtil.getTimeFromTimeMill(shopMeta.getCreateTime()));
            rep.put("{popularity}", String.valueOf(shopMeta.getPopularity()));
            rep.put("{page_view}", String.valueOf(shopMeta.getPageView()));
            rep.put("{hot_value}", String.valueOf(shopMeta.getPageView() * SettingYaml.INSTANCE.getHotValueCoefficient("setting.hotValueCoefficient.pageView")
                    + shopMeta.getPopularity() * SettingYaml.INSTANCE.getHotValueCoefficient("setting.hotValueCoefficient.popularity")
                    + shopMeta.getCollectAmount() * SettingYaml.INSTANCE.getHotValueCoefficient("setting.hotValueCoefficient.collectAmount")));
            ItemStack itemStack;

            if (shopMeta.getIconUuid() != null) {
                GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();
                IconMeta iconMeta = guiService.getIcon(shopMeta.getIconUuid());
                if (iconMeta == null) {
                    refresh();
                    return;
                }
                itemStack = ItemStackUtil.itemStackDeserialize(iconMeta.getIconItemStack());
                ItemStack tem = GuiYaml.INSTANCE.getButton("gui.button.collectedShopPage.collectedShopPageShopOptionPage");
                try {
                    ItemStackUtil.apply(tem, itemStack);
                } catch (NotApplicableException e) {
                    itemStack = tem;
                }

            } else {
                if (GuiYaml.INSTANCE.getBooleanDefault("gui.option.enableShopUsePlayerHead")) {
                    itemStack = ItemStackUtil.getPlayerSkullItem(owner);
                    ItemStack tem = GuiYaml.INSTANCE.getButton("gui.button.collectedShopPage.collectedShopPageShopOptionPage");
                    try {
                        ItemStackUtil.apply(tem, itemStack);
                    } catch (NotApplicableException e) {
                        itemStack = tem;
                    }
                } else {
                    itemStack = GuiYaml.INSTANCE.getButton("gui.button.collectedShopPage.collectedShopPageShopOptionPage");
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
    }
    //渲染当前页的菜单(不包括翻页)

    private void initMenuBarWithoutChangePage() {
        ItemStack search = GuiYaml.INSTANCE.getButton("gui.button.collectedShopPage.searchByShopName");
        inventory.setItem(47, search);

        ItemStack back = GuiYaml.INSTANCE.getButton("gui.button.collectedShopPage.back");
        inventory.setItem(49, back);

        ItemStack searchOwner = GuiYaml.INSTANCE.getButton("gui.button.collectedShopPage.searchByShopOwnerName");
        inventory.setItem(51, searchOwner);
    }

    public void send() throws SQLException {
        this.stats = CollectedShopPageEnum.NORMAL;
        this.searchContent = null;
        this.pageIndex = 0;
        refresh();
    }

    public void nextPage() throws NotExistNextException, SQLException {
        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();
        this.shopMetaList.clear();
        this.shopMetaList.addAll(guiService.listPlayerCollectedShop(player.getUniqueId().toString()));
        int maxIndex = PageUtil.computeMaxPageIndex(shopMetaList.size(), 45);
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
    public List<ShopMeta> getShopMetaList() {
        return new ArrayList<>(shopMetaList);
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public InventoryHolder getPreviousHolder() {
        return previousHolder;
    }
}
