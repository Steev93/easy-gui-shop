package pers.zhangyang.easyguishop.domain;


import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.meta.TradeRecordMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.GuiYaml;
import pers.zhangyang.easylibrary.base.BackAble;
import pers.zhangyang.easylibrary.base.GuiPage;
import pers.zhangyang.easylibrary.base.MultipleGuiPageBase;
import pers.zhangyang.easylibrary.exception.NotApplicableException;
import pers.zhangyang.easylibrary.exception.NotExistNextPageException;
import pers.zhangyang.easylibrary.exception.NotExistPreviousPageException;
import pers.zhangyang.easylibrary.util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ManageTradeRecordPage extends MultipleGuiPageBase implements BackAble {
    private List<TradeRecordMeta> tradeRecordMetaList = new ArrayList<>();
    private int pageIndex;

    public ManageTradeRecordPage(GuiPage previousHolder, Player player) {
        super(GuiYaml.INSTANCE.getString("gui.title.manageTradeRecordPage"), player, previousHolder, previousHolder.getOwner());

    }

    public void send() {
        this.pageIndex = 0;
        refresh();
    }


    public void refresh() {


        this.inventory.clear();

        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
        this.tradeRecordMetaList.clear();
        this.tradeRecordMetaList.addAll(guiService.listPlayerTradeRecord(owner.getUniqueId().toString()));

        if (pageIndex > 0) {
            ItemStack previous = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageTradeRecordPage.previousPage");
            inventory.setItem(45, previous);
        } else {
            inventory.setItem(45, null);
        }
        int maxIndex = PageUtil.computeMaxPageIndex(tradeRecordMetaList.size(), 45);
        if (pageIndex < maxIndex) {
            ItemStack next = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageTradeRecordPage.nextPage");
            inventory.setItem(53, next);
        } else {
            inventory.setItem(53, null);
        }


        this.tradeRecordMetaList = (PageUtil.page(pageIndex, 45, tradeRecordMetaList));
        //设置内容
        for (int i = 0; i < 45; i++) {
            if (i >= tradeRecordMetaList.size()) {
                break;
            }
            TradeRecordMeta shopCommentMeta = tradeRecordMetaList.get(i);
            OfflinePlayer merchant = Bukkit.getOfflinePlayer(UUID.fromString(shopCommentMeta.getMerchantUuid()));
            OfflinePlayer customer = Bukkit.getOfflinePlayer(UUID.fromString(shopCommentMeta.getCustomerUuid()));
            HashMap<String, String> rep = new HashMap<>();
            rep.put("{merchant_name}", merchant.getName() == null ? "/" : merchant.getName());

            rep.put("{customer_name}", merchant.getName() == null ? "/" : merchant.getName());
            rep.put("{good_system}", String.valueOf(shopCommentMeta.isGoodSystem()));
            rep.put("{trade_tax_rate}", String.valueOf(shopCommentMeta.getTradeTaxRate()));
            rep.put("{trade_amount}", String.valueOf(shopCommentMeta.getTradeAmount()));
            rep.put("{trade_time}", TimeUtil.getTimeFromTimeMill(shopCommentMeta.getTradeTime()));
            rep.put("{good_type}", shopCommentMeta.getGoodType());
            ItemStack itemStack;
            if (GuiYaml.INSTANCE.getBooleanDefault("gui.option.enableTradeRecordUseTradeRecordItem")) {
                itemStack = ItemStackUtil.itemStackDeserialize(shopCommentMeta.getGoodItemStack());
                ItemStack tem = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageTradeRecordPage.manageTradeRecordPageTradeRecordOptionPage");
                try {
                    ItemStackUtil.apply(tem, itemStack);
                } catch (NotApplicableException e) {
                    itemStack = tem;
                }
            } else {
                itemStack = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageTradeRecordPage.manageTradeRecordPageTradeRecordOptionPage");
            }
            ReplaceUtil.replaceDisplayName(itemStack, rep);
            ReplaceUtil.replaceLore(itemStack, rep);
            inventory.setItem(i, itemStack);
        }


        ItemStack back = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageTradeRecordPage.back");
        inventory.setItem(49, back);
        viewer.openInventory(this.inventory);
    }


    public void nextPage() throws NotExistNextPageException {
        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
        this.tradeRecordMetaList = guiService.listPlayerTradeRecord(owner.getUniqueId().toString());
        int maxIndex = PageUtil.computeMaxPageIndex(tradeRecordMetaList.size(), 45);
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
    public List<TradeRecordMeta> getTradeRecordMetaList() {
        return new ArrayList<>(tradeRecordMetaList);
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