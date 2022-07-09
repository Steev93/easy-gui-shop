package pers.zhangyang.easyguishop.domain;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.meta.TradeRecordMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.ItemStackUtil;
import pers.zhangyang.easyguishop.util.ReplaceUtil;
import pers.zhangyang.easyguishop.util.TimeUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.GuiYaml;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class ManageTradeRecordPageTradeRecordOptionPage implements InventoryHolder {
    private final Inventory inventory;
    private final InventoryHolder previousHolder;
    private final Player player;
    private TradeRecordMeta tradeRecordMeta;

    public ManageTradeRecordPageTradeRecordOptionPage(InventoryHolder previousHolder, Player player, TradeRecordMeta iconMeta) {
        this.player = player;
        this.tradeRecordMeta = iconMeta;
        this.previousHolder = previousHolder;
        String title = GuiYaml.INSTANCE.getString("gui.title.manageTradeRecordPageTradeRecordOptionPage");
        if (title == null) {
            this.inventory = Bukkit.createInventory(this, 54);
        } else {
            this.inventory = Bukkit.createInventory(this, 54, ChatColor.translateAlternateColorCodes('&', title));
        }
    }


    //根据Shop的情况来设置Button
    public void send() throws SQLException {

        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();
        this.tradeRecordMeta = guiService.getTradeRecord(tradeRecordMeta.getUuid());
        if (this.tradeRecordMeta == null) {
            ((ManageTradeRecordPage) previousHolder).send();
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
        player.openInventory(this.inventory);
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