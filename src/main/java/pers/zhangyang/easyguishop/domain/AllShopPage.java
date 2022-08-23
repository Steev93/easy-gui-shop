package pers.zhangyang.easyguishop.domain;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.enumration.AllShopPageStatsEnum;
import pers.zhangyang.easyguishop.meta.IconMeta;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.GuiYaml;
import pers.zhangyang.easyguishop.yaml.SettingYaml;
import pers.zhangyang.easylibrary.base.BackAble;
import pers.zhangyang.easylibrary.base.MultipleGuiPageBase;
import pers.zhangyang.easylibrary.exception.NotApplicableException;
import pers.zhangyang.easylibrary.exception.NotExistNextPageException;
import pers.zhangyang.easylibrary.exception.NotExistPreviousPageException;
import pers.zhangyang.easylibrary.util.*;

import java.lang.reflect.Type;
import java.util.*;

public class AllShopPage extends MultipleGuiPageBase implements BackAble {

    private List<ShopMeta> shopMetaList = new ArrayList<>();
    private int pageIndex;
    private AllShopPageStatsEnum stats;
    private String searchContent;

    public AllShopPage(Player player, OfflinePlayer owner) {
        super(GuiYaml.INSTANCE.getString("gui.title.allShopPage"), player, null, owner,54);
        stats = AllShopPageStatsEnum.NORMAL;
    }

    public void searchByShopName(@NotNull String name) {
        this.stats = AllShopPageStatsEnum.SEARCH_SHOP_NAME;
        this.searchContent = name;
        this.pageIndex = 0;
        refresh();
    }

    public void searchByShopOwnerName(@NotNull String ownerName) {
        this.stats = AllShopPageStatsEnum.SEARCH_OWNER_NAME;
        this.searchContent = ownerName;
        this.pageIndex = 0;
        refresh();
    }

    //根据当前的状态来从数据库里获取
    public void refresh() {


        this.inventory.clear();

        this.shopMetaList.clear();
        if (this.stats.equals(AllShopPageStatsEnum.NORMAL)) {
            GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
            this.shopMetaList.addAll(guiService.listShop());
        }
        if (this.stats.equals(AllShopPageStatsEnum.SEARCH_SHOP_NAME)) {
            GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
            this.shopMetaList.addAll(guiService.listShop());
            this.shopMetaList.removeIf(shopMeta -> !shopMeta.getName().contains(searchContent));
        }
        if (this.stats.equals(AllShopPageStatsEnum.SEARCH_OWNER_NAME)) {
            GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
            this.shopMetaList.addAll(guiService.listShop());
            this.shopMetaList.removeIf(shopMeta -> !Objects.requireNonNull(Bukkit.getOfflinePlayer(UUID.fromString(shopMeta.getOwnerUuid())).getName()).contains(searchContent));
        }
        int maxIndex = PageUtil.computeMaxPageIndex(shopMetaList.size(), 45);
        if (pageIndex > 0) {
            ItemStack previous = GuiYaml.INSTANCE.getButtonDefault("gui.button.allShopPage.previousPage");
            inventory.setItem(45, previous);
        } else {
            inventory.setItem(45, null);
        }
        if (pageIndex < maxIndex) {
            ItemStack next = GuiYaml.INSTANCE.getButtonDefault("gui.button.allShopPage.nextPage");
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
                GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
                IconMeta iconMeta = guiService.getIcon(shopMeta.getIconUuid());
                if (iconMeta == null) {
                    refresh();
                    return;
                }
                itemStack = ItemStackUtil.itemStackDeserialize(iconMeta.getIconItemStack());
                ItemStack tem = GuiYaml.INSTANCE.getButtonDefault("gui.button.allShopPage.allShopPageShopOptionPage");
                try {
                    ItemStackUtil.apply(tem, itemStack);
                } catch (NotApplicableException e) {
                    itemStack = tem;
                }

            } else {
                if (GuiYaml.INSTANCE.getBooleanDefault("gui.option.enableShopUsePlayerHead")) {
                    itemStack = PlayerUtil.getPlayerSkullItem(owner);
                    ItemStack tem = GuiYaml.INSTANCE.getButtonDefault("gui.button.allShopPage.allShopPageShopOptionPage");
                    try {
                        ItemStackUtil.apply(tem, itemStack);
                    } catch (NotApplicableException e) {
                        itemStack = tem;
                    }
                } else {
                    itemStack = GuiYaml.INSTANCE.getButtonDefault("gui.button.allShopPage.allShopPageShopOptionPage");
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


        ItemStack search = GuiYaml.INSTANCE.getButtonDefault("gui.button.allShopPage.searchByShopName");
        inventory.setItem(47, search);
        ItemStack tradeRecord = GuiYaml.INSTANCE.getButtonDefault("gui.button.allShopPage.manageTradeRecordPage");
        inventory.setItem(46, tradeRecord);
        ItemStack manager = GuiYaml.INSTANCE.getButtonDefault("gui.button.allShopPage.manageShopPage");
        inventory.setItem(48, manager);

        ItemStack myCommentPage = GuiYaml.INSTANCE.getButtonDefault("gui.button.allShopPage.manageShopCommentPage");
        inventory.setItem(52, myCommentPage);
        ItemStack collect = GuiYaml.INSTANCE.getButtonDefault("gui.button.allShopPage.back");
        inventory.setItem(49, collect);
        ItemStack manageItemStock = GuiYaml.INSTANCE.getButtonDefault("gui.button.allShopPage.manageItemStockPage");
        inventory.setItem(50, manageItemStock);
        ItemStack searchOwner = GuiYaml.INSTANCE.getButtonDefault("gui.button.allShopPage.searchByShopOwnerName");
        inventory.setItem(51, searchOwner);
        viewer.openInventory(inventory);
    }

    public void send() {
        this.stats = AllShopPageStatsEnum.NORMAL;
        this.searchContent = null;
        this.pageIndex = 0;
        refresh();
    }

    public void nextPage() throws NotExistNextPageException {
        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
        this.shopMetaList = guiService.listShop();
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

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }


    @Override
    public void back() {
        List<String> cmdList = GuiYaml.INSTANCE.getStringList("gui.firstPageBackCommand");
        if (cmdList == null) {
            return;
        }
        CommandUtil.dispatchCommandList(viewer, cmdList);
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
