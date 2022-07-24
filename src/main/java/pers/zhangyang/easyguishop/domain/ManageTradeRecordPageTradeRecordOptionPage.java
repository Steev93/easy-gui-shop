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
import pers.zhangyang.easylibrary.base.SingleGuiPageBase;
import pers.zhangyang.easylibrary.util.ItemStackUtil;
import pers.zhangyang.easylibrary.util.ReplaceUtil;
import pers.zhangyang.easylibrary.util.TimeUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

import java.util.HashMap;
import java.util.UUID;

public class ManageTradeRecordPageTradeRecordOptionPage extends SingleGuiPageBase implements BackAble {
    private TradeRecordMeta tradeRecordMeta;

    public ManageTradeRecordPageTradeRecordOptionPage(GuiPage previousHolder, Player player, TradeRecordMeta iconMeta) {
        super(GuiYaml.INSTANCE.getString("gui.title.manageTradeRecordPageTradeRecordOptionPage"), player, previousHolder, previousHolder.getOwner());

        this.tradeRecordMeta = iconMeta;

    }


    //根据Shop的情况来设置Button
    public void send() {
        refresh();
    }

    @Override
    public void refresh() {

        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
        this.tradeRecordMeta = guiService.getTradeRecord(tradeRecordMeta.getUuid());
        if (this.tradeRecordMeta == null) {
            backPage.send();
            return;
        }

        this.inventory.clear();
        ItemStack currencyGuide = GuiYaml.INSTANCE.getButton("gui.button.manageTradeRecordPageTradeRecordOptionPage.guideGoodAndCurrency");
        inventory.setItem(13, currencyGuide);

        HashMap<String, String> rep = new HashMap<>();
        ItemStack currency = null;
        if (tradeRecordMeta.getGoodVaultPrice() != null) {
            currency = GuiYaml.INSTANCE.getButton("gui.button.manageTradeRecordPageTradeRecordOptionPage.vaultCurrency");
            rep.put("{price}", String.valueOf(tradeRecordMeta.getGoodVaultPrice()));
        } else if (tradeRecordMeta.getGoodPlayerPointsPrice() != null) {
            currency = GuiYaml.INSTANCE.getButton("gui.button.manageTradeRecordPageTradeRecordOptionPage.playerPointsCurrency");
            rep.put("{price}", String.valueOf(tradeRecordMeta.getGoodPlayerPointsPrice()));
        } else if (tradeRecordMeta.getGoodCurrencyItemStack() != null) {
            currency = ItemStackUtil.itemStackDeserialize(tradeRecordMeta.getGoodCurrencyItemStack());
            rep.put("{price}", String.valueOf(tradeRecordMeta.getGoodItemPrice()));
        } else {
            rep.put("{price}", "\\");
        }
        OfflinePlayer merchant = Bukkit.getOfflinePlayer(UUID.fromString(tradeRecordMeta.getMerchantUuid()));
        OfflinePlayer customer = Bukkit.getOfflinePlayer(UUID.fromString(tradeRecordMeta.getCustomerUuid()));
        rep.put("{merchant_name}", String.valueOf(merchant.getName()));
        rep.put("{customer_name}", String.valueOf(customer.getName()));
        rep.put("{good_system}", String.valueOf(tradeRecordMeta.isGoodSystem()));
        rep.put("{good_type}", tradeRecordMeta.getGoodType());
        rep.put("{trade_amount}", String.valueOf(tradeRecordMeta.getTradeAmount()));
        rep.put("{trade_tax_rate}", String.valueOf(tradeRecordMeta.getTradeTaxRate()));
        inventory.setItem(22, currency);
        rep.put("{trade_time}", TimeUtil.getTimeFromTimeMill(tradeRecordMeta.getTradeTime()));

        ItemStack information = GuiYaml.INSTANCE.getButton("gui.button.manageTradeRecordPageTradeRecordOptionPage.tradeRecordInformation");
        ReplaceUtil.replaceLore(information, rep);
        ReplaceUtil.replaceDisplayName(information, rep);
        inventory.setItem(31, information);

        ItemStack icon = ItemStackUtil.itemStackDeserialize(tradeRecordMeta.getGoodItemStack());
        inventory.setItem(4, icon);


        ItemStack back = GuiYaml.INSTANCE.getButton("gui.button.manageTradeRecordPageTradeRecordOptionPage.back");
        inventory.setItem(49, back);
        viewer.openInventory(this.inventory);
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