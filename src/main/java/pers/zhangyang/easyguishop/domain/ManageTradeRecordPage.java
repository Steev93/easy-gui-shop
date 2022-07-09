package pers.zhangyang.easyguishop.domain;


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
import pers.zhangyang.easyguishop.meta.TradeRecordMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.*;
import pers.zhangyang.easyguishop.yaml.GuiYaml;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ManageTradeRecordPage implements InventoryHolder {

    private final Inventory inventory;
    private final List<TradeRecordMeta> tradeRecordMetaList = new ArrayList<>();
    private final InventoryHolder previousHolder;
    private final Player player;
    private int pageIndex;

    public ManageTradeRecordPage(InventoryHolder previousHolder, Player player) {
        this.player = player;
        String title = GuiYaml.INSTANCE.getString("gui.title.manageTradeRecordPage");
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
        this.tradeRecordMetaList.clear();
        this.tradeRecordMetaList.addAll(guiService.listPlayerTradeRecord(player.getUniqueId().toString()));

        refreshContent();
        if (pageIndex > 0) {
            ItemStack previous = GuiYaml.INSTANCE.getButton("gui.button.manageTradeRecordPage.previous");
            inventory.setItem(45, previous);
        } else {
            inventory.setItem(45, null);
        }
        int maxIndex = PageUtil.computeMaxPageIndex(tradeRecordMetaList.size(), 45);
        if (pageIndex < maxIndex) {
            ItemStack next = GuiYaml.INSTANCE.getButton("gui.button.manageTradeRecordPage.next");
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
        int pageMax = PageUtil.page(pageIndex, 45, new ArrayList<>(tradeRecordMetaList)).size();
        //设置内容
        for (int i = 45 * pageIndex; i < 45 + 45 * pageIndex; i++) {
            if (i >= pageMax + 45 * pageIndex) {
                break;
            }
            TradeRecordMeta shopCommentMeta = tradeRecordMetaList.get(i);
            OfflinePlayer merchant = Bukkit.getOfflinePlayer(UUID.fromString(shopCommentMeta.getMerchantUuid()));
            OfflinePlayer customer = Bukkit.getOfflinePlayer(UUID.fromString(shopCommentMeta.getCustomerUuid()));
            HashMap<String, String> rep = new HashMap<>();
            rep.put("{merchant_name}", String.valueOf(merchant.getName()));
            rep.put("{customer_name}", String.valueOf(customer.getName()));
            rep.put("{good_system}", String.valueOf(shopCommentMeta.isGoodSystem()));
            rep.put("{trade_tax_rate}", String.valueOf(shopCommentMeta.getTradeTaxRate()));
            rep.put("{trade_amount}", String.valueOf(shopCommentMeta.getTradeAmount()));
            rep.put("{trade_time}", TimeUtil.getTimeFromTimeMill(shopCommentMeta.getTradeTime()));
            rep.put("{good_type}", shopCommentMeta.getGoodType());
            ItemStack itemStack;
            if (GuiYaml.INSTANCE.getBooleanDefault("gui.option.enableTradeRecordUseTradeRecordItem")) {
                itemStack = ItemStackUtil.itemStackDeserialize(shopCommentMeta.getGoodItemStack());
                ItemStack tem = GuiYaml.INSTANCE.getButton("gui.button.manageTradeRecordPage.manageTradeRecordPageTradeRecordOptionPage");
                ItemStackUtil.apply(tem, itemStack);
            } else {
                itemStack = GuiYaml.INSTANCE.getButton("gui.button.manageTradeRecordPage.manageTradeRecordPageTradeRecordOptionPage");
            }
            ReplaceUtil.replaceDisplayName(itemStack, rep);
            ReplaceUtil.replaceLore(itemStack, rep);
            inventory.setItem(i - 45 * pageIndex, itemStack);
        }
    }

    //渲染当前页的菜单(不包括翻页)
    private void initMenuBarWithoutChangePage() {
        ItemStack back = GuiYaml.INSTANCE.getButton("gui.button.manageTradeRecordPage.back");
        inventory.setItem(49, back);

    }


    public void nextPage() throws NotExistNextException, SQLException {
        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();
        this.tradeRecordMetaList.clear();
        this.tradeRecordMetaList.addAll(guiService.listPlayerTradeRecord(player.getUniqueId().toString()));
        int maxIndex = PageUtil.computeMaxPageIndex(tradeRecordMetaList.size(), 45);
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
    public List<TradeRecordMeta> getTradeRecordMetaList() {
        return new ArrayList<>(tradeRecordMetaList);
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