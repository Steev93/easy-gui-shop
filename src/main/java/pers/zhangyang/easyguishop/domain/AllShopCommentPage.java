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
import pers.zhangyang.easyguishop.enumration.ShopCommentPageStatsEnum;
import pers.zhangyang.easyguishop.exception.NotExistShopException;
import pers.zhangyang.easyguishop.meta.ShopCommentMeta;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.GuiYaml;
import pers.zhangyang.easylibrary.base.BackAble;
import pers.zhangyang.easylibrary.base.GuiPage;
import pers.zhangyang.easylibrary.base.MultipleGuiPageBase;
import pers.zhangyang.easylibrary.exception.NotExistNextPageException;
import pers.zhangyang.easylibrary.exception.NotExistPreviousPageException;
import pers.zhangyang.easylibrary.util.PageUtil;
import pers.zhangyang.easylibrary.util.ReplaceUtil;
import pers.zhangyang.easylibrary.util.TimeUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

import java.lang.reflect.Type;
import java.util.*;

public class AllShopCommentPage extends MultipleGuiPageBase implements BackAble {

    private List<ShopCommentMeta> shopCommentMetaList = new ArrayList<>();
    private int pageIndex;
    private ShopCommentPageStatsEnum stats;
    private String searchContent;
    private ShopMeta shopMeta;

    public AllShopCommentPage(GuiPage previousHolder, Player player, ShopMeta shopMeta) {
        super(GuiYaml.INSTANCE.getString("gui.title.allShopCommentPage"), player, previousHolder, previousHolder.getOwner(),54);
        this.shopMeta = shopMeta;
        stats = ShopCommentPageStatsEnum.NORMAL;
    }

    public void send() {
        this.stats = ShopCommentPageStatsEnum.NORMAL;
        this.searchContent = null;
        this.pageIndex = 0;
        refresh();

    }

    public void searchByCommenterName(@NotNull String ownerName) {
        this.stats = ShopCommentPageStatsEnum.SEARCH_COMMENTER_NAME;
        this.searchContent = ownerName;
        this.pageIndex = 0;
        refresh();
    }

    public void refresh() {

        this.inventory.clear();

        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
        this.shopMeta = guiService.getShop(this.shopMeta.getUuid());
        if (this.shopMeta == null) {
            if (backPage instanceof AllShopPageShopOptionPage) {
                backPage.send();
            }
            if (backPage instanceof CollectedShopPageShopOptionPage) {
                backPage.send();
            }
            if (backPage instanceof ManageShopPageShopOptionPage) {
                backPage.send();
            }
            return;
        }
        shopCommentMetaList.clear();
        try {
            shopCommentMetaList.addAll(guiService.listShopComment(shopMeta.getUuid()));
        } catch (NotExistShopException e) {
            if (backPage instanceof AllShopPageShopOptionPage) {
                backPage.send();
            }
            if (backPage instanceof CollectedShopPageShopOptionPage) {
                backPage.send();
            }
            if (backPage instanceof ManageShopPageShopOptionPage) {
                backPage.send();
            }
            return;
        }

        if (stats.equals(ShopCommentPageStatsEnum.SEARCH_COMMENTER_NAME)) {
            shopCommentMetaList.removeIf(shopCommentMeta -> !Objects.requireNonNull(Bukkit.getOfflinePlayer(UUID.fromString(shopCommentMeta.getCommenterUuid())).getName()).contains(searchContent));
        }
        int maxIndex = PageUtil.computeMaxPageIndex(shopCommentMetaList.size(), 45);
        if (pageIndex > maxIndex) {
            this.pageIndex = maxIndex;
        }


        if (pageIndex > 0) {
            ItemStack previous = GuiYaml.INSTANCE.getButtonDefault("gui.button.allShopCommentPage.previousPage");
            inventory.setItem(45, previous);
        } else {
            inventory.setItem(45, null);
        }
        if (pageIndex < maxIndex) {
            ItemStack next = GuiYaml.INSTANCE.getButtonDefault("gui.button.allShopCommentPage.nextPage");
            inventory.setItem(53, next);
        } else {
            inventory.setItem(53, null);
        }


        this.shopCommentMetaList = (PageUtil.page(pageIndex, 45, shopCommentMetaList));
        //设置内容
        for (int i = 0; i < 45; i++) {
            if (i >= shopCommentMetaList.size()) {
                break;
            }
            ShopCommentMeta shopCommentMeta = shopCommentMetaList.get(i);
            OfflinePlayer commenter = Bukkit.getOfflinePlayer(UUID.fromString(shopCommentMeta.getCommenterUuid()));
            HashMap<String, String> rep = new HashMap<>();
            rep.put("{commenter_name}", commenter.getName() == null ? "/" : commenter.getName());
            rep.put("{comment_time}", TimeUtil.getTimeFromTimeMill(shopCommentMeta.getCommentTime()));
            ItemStack itemStack = GuiYaml.INSTANCE.getButtonDefault("gui.button.allShopCommentPage.shopComment");
            Gson gson = new Gson();
            Type stringListType = new TypeToken<ArrayList<String>>() {
            }.getType();
            List<String> stringList = gson.fromJson(shopCommentMeta.getContent(), stringListType);
            ReplaceUtil.formatLore(itemStack, "{(content)}", stringList);
            ReplaceUtil.replaceDisplayName(itemStack, rep);
            ReplaceUtil.replaceLore(itemStack, rep);
            inventory.setItem(i, itemStack);
        }

        ItemStack back = GuiYaml.INSTANCE.getButtonDefault("gui.button.allShopCommentPage.back");
        inventory.setItem(49, back);


        ItemStack search = GuiYaml.INSTANCE.getButtonDefault("gui.button.allShopCommentPage.searchByCommenterName");
        inventory.setItem(50, search);
        viewer.openInventory(this.inventory);
    }


    public void nextPage() throws NotExistNextPageException {
        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
        this.shopMeta = guiService.getShop(this.shopMeta.getUuid());
        if (this.shopMeta == null) {
            if (backPage instanceof AllShopPageShopOptionPage) {
                backPage.send();
            }
            if (backPage instanceof CollectedShopPageShopOptionPage) {
                backPage.send();
            }
            if (backPage instanceof ManageShopPageShopOptionPage) {
                backPage.send();
            }
            return;
        }
        try {
            shopCommentMetaList = guiService.listShopComment(shopMeta.getUuid());
        } catch (NotExistShopException e) {
            if (backPage instanceof AllShopPageShopOptionPage) {
                backPage.send();
            }
            if (backPage instanceof CollectedShopPageShopOptionPage) {
                backPage.send();
            }
            if (backPage instanceof ManageShopPageShopOptionPage) {
                backPage.send();
            }
            return;
        }
        int maxIndex = PageUtil.computeMaxPageIndex(shopCommentMetaList.size(), 45);
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
