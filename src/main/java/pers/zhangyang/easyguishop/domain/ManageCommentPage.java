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
import pers.zhangyang.easyguishop.exception.NotExistNextException;
import pers.zhangyang.easyguishop.exception.NotExistPreviousException;
import pers.zhangyang.easyguishop.meta.ShopCommentMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.PageUtil;
import pers.zhangyang.easyguishop.util.ReplaceUtil;
import pers.zhangyang.easyguishop.util.TimeUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.GuiYaml;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ManageCommentPage implements InventoryHolder {

    private final Inventory inventory;
    private  List<ShopCommentMeta> shopCommentMetaList = new ArrayList<>();
    private final InventoryHolder previousHolder;
    private final Player player;
    private int pageIndex;

    public ManageCommentPage(InventoryHolder previousHolder, Player player) {
        this.player = player;
        String title = GuiYaml.INSTANCE.getString("gui.title.manageCommentPage");
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
        this.shopCommentMetaList.clear();
        this.shopCommentMetaList.addAll(guiService.listPlayerComment(player.getUniqueId().toString()));

        refreshContent();
        if (pageIndex > 0) {
            ItemStack previous = GuiYaml.INSTANCE.getButton("gui.button.manageCommentPage.previous");
            inventory.setItem(45, previous);
        } else {
            inventory.setItem(45, null);
        }
        int maxIndex = PageUtil.computeMaxPageIndex(shopCommentMetaList.size(), 45);
        if (pageIndex < maxIndex) {
            ItemStack next = GuiYaml.INSTANCE.getButton("gui.button.manageCommentPage.next");
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
        this.shopCommentMetaList=(PageUtil.page(pageIndex, 45,shopCommentMetaList));
        //设置内容
        for (int i = 0; i < 45 ; i++) {
            if (i >= shopCommentMetaList.size()) {
                break;
            }
            ShopCommentMeta shopCommentMeta = shopCommentMetaList.get(i);
            OfflinePlayer commenter = Bukkit.getOfflinePlayer(UUID.fromString(shopCommentMeta.getCommenterUuid()));
            HashMap<String, String> rep = new HashMap<>();
            rep.put("{commenter_name}", commenter.getName());
            rep.put("{comment_time}", TimeUtil.getTimeFromTimeMill(shopCommentMeta.getCommentTime()));
            ItemStack itemStack = GuiYaml.INSTANCE.getButton("gui.button.manageCommentPage.deleteShopComment");
            Gson gson = new Gson();
            Type stringListType = new TypeToken<ArrayList<String>>() {
            }.getType();
            List<String> stringList = gson.fromJson(shopCommentMeta.getContent(), stringListType);
            ReplaceUtil.formatLore(itemStack, "{(content)}", stringList);

            ReplaceUtil.replaceDisplayName(itemStack, rep);
            ReplaceUtil.replaceLore(itemStack, rep);
            inventory.setItem(i , itemStack);
        }
    }

    //渲染当前页的菜单(不包括翻页)
    private void initMenuBarWithoutChangePage() {
        ItemStack back = GuiYaml.INSTANCE.getButton("gui.button.manageCommentPage.back");
        inventory.setItem(49, back);

    }


    public void nextPage() throws NotExistNextException, SQLException {
        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();
        this.shopCommentMetaList.clear();
        this.shopCommentMetaList.addAll(guiService.listPlayerComment(player.getUniqueId().toString()));
        int maxIndex = PageUtil.computeMaxPageIndex(shopCommentMetaList.size(), 45);
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
    public List<ShopCommentMeta> getShopCommentMetaList() {
        return new ArrayList<>(shopCommentMetaList);
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