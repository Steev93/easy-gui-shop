package pers.zhangyang.easyguishop.listener.allgoodpagegoodoptionpage;

import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pers.zhangyang.easyguishop.domain.AllGoodPageGoodOptionPage;
import pers.zhangyang.easyguishop.exception.*;
import pers.zhangyang.easyguishop.meta.GoodMeta;
import pers.zhangyang.easyguishop.meta.TradeRecordMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easyguishop.yaml.SettingYaml;
import pers.zhangyang.easylibrary.base.FiniteInputListenerBase;
import pers.zhangyang.easylibrary.other.playerpoints.PlayerPoints;
import pers.zhangyang.easylibrary.other.vault.Vault;
import pers.zhangyang.easylibrary.util.*;

import java.util.UUID;

public class PlayerInputAfterClickAllGoodPageGoodOptionPageTradeGood extends FiniteInputListenerBase {

    private final GoodMeta goodMeta;
    private final AllGoodPageGoodOptionPage allGoodPageGoodOptionPage;

    public PlayerInputAfterClickAllGoodPageGoodOptionPageTradeGood(Player player, OfflinePlayer owner, GoodMeta goodMeta, AllGoodPageGoodOptionPage manageShopPage) {
        super(player, owner, manageShopPage, 1);
        this.allGoodPageGoodOptionPage = manageShopPage;
        this.goodMeta = goodMeta;
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToTrade"));
    }

    @Override
    public void run() {
        int amount;
        try {
            amount = Integer.parseInt(messages[0]);
        } catch (NumberFormatException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.invalidNumber"));
            return;
        }
        if (amount < 0) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.invalidNumber"));
            return;
        }


        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();

        //如果是收购，检查玩家是不是有物品  如果出售，检查玩家是不是有空间
        //检查限购时间 检查是否设置价格  检查是否玩家有货币     然后购买 扣除货币  添加物品

        //限购时间
        Integer limitTime = goodMeta.getLimitTime();
        if (limitTime != null && limitTime * 1000 + goodMeta.getCreateTime() < System.currentTimeMillis()) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.exceedLimitTimeWhenTradeGood"));
            return;
        }

        //检查是否设置价格
        Double vaultPrice = goodMeta.getVaultPrice();
        Integer itemPrice = goodMeta.getItemPrice();
        Integer playerPointsPrice = goodMeta.getPlayerPointsPrice();
        Economy vault = Vault.hook();
        PlayerPointsAPI playerPoints = PlayerPoints.hook();
        OfflinePlayer merchant = Bukkit.getOfflinePlayer(UUID.fromString(allGoodPageGoodOptionPage.getShopMeta().getOwnerUuid()));

        if (vaultPrice == null && itemPrice == null && playerPointsPrice == null) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notSetPriceWhenTrade"));
            return;
        }


        if (goodMeta.getType().equalsIgnoreCase("收购")) {
            //检查玩家手中是否有货
            int have = PlayerUtil.computeItemHave(ItemStackUtil.itemStackDeserialize(goodMeta.getGoodItemStack()), player);
            if (have < amount) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEnoughGoodWhenTradeGood"));
                return;
            }

            if (vaultPrice != null) {
                if (vault == null) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notHookVault"));
                    return;
                }
                double taxRate = SettingYaml.INSTANCE.getTax("setting.tax.vault");
                double beforeTax = vaultPrice * amount;
                double tax = beforeTax * taxRate;
                double afterTax = beforeTax - tax;
                //检查店主是否有钱
                if (!goodMeta.isSystem() && !vault.has(merchant, beforeTax)) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notMoreVaultWhenTradeGood"));
                    return;
                }
                //交易
                try {
                    guiService.trade(goodMeta.getUuid(), amount, goodMeta);
                    TradeRecordMeta tradeRecordMeta = new TradeRecordMeta(UuidUtil.getUUID(), owner.getUniqueId().toString(),
                            merchant.getUniqueId().toString(), goodMeta.getGoodItemStack(), amount, goodMeta.isSystem(),
                            System.currentTimeMillis(), goodMeta.getType(), taxRate);
                    tradeRecordMeta.setGoodVaultPrice(goodMeta.getVaultPrice());
                    guiService.createTradeRecord(tradeRecordMeta);
                } catch (NotExistGoodException e) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistGood"));
                    return;
                } catch (DuplicateTradeRecordException | NotMoreGoodException ignored) {
                } catch (StateChangeException e) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.stateChange"));
                    return;
                }
                //扣除货币
                vault.depositPlayer(owner, afterTax);
                if (!goodMeta.isSystem()) {
                    vault.withdrawPlayer(merchant, beforeTax);
                }
            }
            if (playerPointsPrice != null) {
                if (playerPoints == null) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notHookPlayerPoints"));
                    return;
                }
                double taxRate = SettingYaml.INSTANCE.getTax("setting.tax.playerPoints");
                int beforeTax = playerPointsPrice * amount;
                int tax = (int) Math.round(beforeTax * taxRate);
                int afterTax = beforeTax - tax;
                //检查钱
                if (!goodMeta.isSystem() && playerPoints.look(merchant.getUniqueId()) < beforeTax) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notMorePlayerPointsWhenTradeGood"));
                    return;
                }

                try {
                    guiService.trade(goodMeta.getUuid(), amount, goodMeta);
                    TradeRecordMeta tradeRecordMeta = new TradeRecordMeta(UuidUtil.getUUID(), owner.getUniqueId().toString(),
                            merchant.getUniqueId().toString(), goodMeta.getGoodItemStack(), amount, goodMeta.isSystem(),
                            System.currentTimeMillis(), goodMeta.getType(), taxRate);
                    tradeRecordMeta.setGoodPlayerPointsPrice(goodMeta.getPlayerPointsPrice());
                    guiService.createTradeRecord(tradeRecordMeta);
                } catch (NotExistGoodException e) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistGood"));
                    return;
                } catch (DuplicateTradeRecordException | NotMoreGoodException ignored) {
                } catch (StateChangeException e) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.stateChange"));
                    return;
                }
                playerPoints.give(owner.getUniqueId(), afterTax);

                if (!goodMeta.isSystem()) {
                    playerPoints.take(merchant.getUniqueId(), beforeTax);
                }
            }
            if (itemPrice != null) {

                try {
                    double taxRate = SettingYaml.INSTANCE.getTax("setting.tax.item");

                    //交易
                    guiService.tradeItem(goodMeta.getUuid(), amount, goodMeta, merchant.getUniqueId().toString(), owner.getUniqueId().toString());
                    TradeRecordMeta tradeRecordMeta = new TradeRecordMeta(UuidUtil.getUUID(), owner.getUniqueId().toString(),
                            merchant.getUniqueId().toString(),
                            goodMeta.getGoodItemStack(), amount, goodMeta.isSystem(), System.currentTimeMillis(), goodMeta.getType(),
                            taxRate);
                    tradeRecordMeta.setGoodItemPrice(goodMeta.getItemPrice());
                    tradeRecordMeta.setGoodCurrencyItemStack(goodMeta.getCurrencyItemStack());
                    guiService.createTradeRecord(tradeRecordMeta);

                } catch (NotMoreGoodException | DuplicateTradeRecordException | NotEnoughItemStockException ignored) {
                } catch (NotExistGoodException e) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistGood"));
                    return;
                } catch (StateChangeException e) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.stateChange"));
                    return;
                } catch (NotMoreItemStockException e) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notMoreItemStockWhenTradeGood"));
                    return;
                }
            }
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.tradeGood"));
            //背包扣除物品
            PlayerUtil.removeItem(player, ItemStackUtil.itemStackDeserialize(goodMeta.getGoodItemStack()), amount);

        }
        if (goodMeta.getType().equalsIgnoreCase("出售")) {
            //检查是不是有空间
            int space = PlayerUtil.checkSpace(player, ItemStackUtil.itemStackDeserialize(goodMeta.getGoodItemStack()));
            if (space < amount) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEnoughSpaceWhenTradeGood"));
                return;
            }

            if (vaultPrice != null) {
                if (vault == null) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notHookVault"));
                    return;
                }
                double taxRate = SettingYaml.INSTANCE.getTax("setting.tax.vault");
                double beforeTax = vaultPrice * amount;
                double tax = beforeTax * taxRate;
                double afterTax = beforeTax - tax;
                //检查是否有钱
                if (!vault.has(owner, beforeTax)) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEnoughVaultWhenTradeGood"));
                    return;
                }
                //交易
                try {
                    guiService.trade(goodMeta.getUuid(), amount, goodMeta);
                    TradeRecordMeta tradeRecordMeta = new TradeRecordMeta(UuidUtil.getUUID(), owner.getUniqueId().toString(),
                            merchant.getUniqueId().toString(), goodMeta.getGoodItemStack(), amount, goodMeta.isSystem(),
                            System.currentTimeMillis(), goodMeta.getType(), taxRate);
                    tradeRecordMeta.setGoodVaultPrice(goodMeta.getVaultPrice());
                    guiService.createTradeRecord(tradeRecordMeta);
                } catch (NotExistGoodException e) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistGood"));
                    return;
                } catch (DuplicateTradeRecordException ignored) {
                } catch (NotMoreGoodException e) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notMoreGoodWhenTradeGood"));
                    return;
                } catch (StateChangeException e) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.stateChange"));
                    return;
                }
                vault.withdrawPlayer(owner, beforeTax);
                if (!goodMeta.isSystem()) {
                    vault.depositPlayer(merchant, afterTax);
                }

            }
            if (playerPointsPrice != null) {
                if (playerPoints == null) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notHookPlayerPoints"));
                    return;
                }

                double taxRate = SettingYaml.INSTANCE.getTax("setting.tax.playerPoints");
                int beforeTax = playerPointsPrice * amount;
                int tax = (int) Math.round(beforeTax * taxRate);
                int afterTax = beforeTax - tax;
                //检查钱
                if (playerPoints.look(owner.getUniqueId()) < beforeTax) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEnoughPlayerPointsWhenTradeGood"));
                    return;
                }


                try {
                    guiService.trade(goodMeta.getUuid(), amount, goodMeta);
                    TradeRecordMeta tradeRecordMeta = new TradeRecordMeta(UuidUtil.getUUID(), owner.getUniqueId().toString(),
                            merchant.getUniqueId().toString(), goodMeta.getGoodItemStack(), amount, goodMeta.isSystem(),
                            System.currentTimeMillis(), goodMeta.getType(), taxRate);
                    tradeRecordMeta.setGoodPlayerPointsPrice(goodMeta.getPlayerPointsPrice());
                    guiService.createTradeRecord(tradeRecordMeta);
                } catch (NotExistGoodException e) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistGood"));
                    return;
                } catch (DuplicateTradeRecordException ignored) {
                } catch (NotMoreGoodException e) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notMoreGoodWhenTradeGood"));
                    return;
                } catch (StateChangeException e) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.stateChange"));
                    return;
                }
                playerPoints.take(owner.getUniqueId(), beforeTax);

                if (!goodMeta.isSystem()) {
                    playerPoints.give(merchant.getUniqueId(), afterTax);
                }

            }
            if (itemPrice != null) {
                double taxRate = SettingYaml.INSTANCE.getTax("setting.tax.item");
                //扣除货币
                try {

                    //交易
                    guiService.tradeItem(goodMeta.getUuid(), amount, goodMeta, merchant.getUniqueId().toString(), owner.getUniqueId().toString());
                    TradeRecordMeta tradeRecordMeta = new TradeRecordMeta(UuidUtil.getUUID(), owner.getUniqueId().toString(),
                            merchant.getUniqueId().toString(), goodMeta.getGoodItemStack(), amount, goodMeta.isSystem(),
                            System.currentTimeMillis(), goodMeta.getType(), taxRate);
                    tradeRecordMeta.setGoodItemPrice(goodMeta.getItemPrice());
                    tradeRecordMeta.setGoodCurrencyItemStack(goodMeta.getCurrencyItemStack());
                    guiService.createTradeRecord(tradeRecordMeta);

                } catch (NotMoreItemStockException | DuplicateTradeRecordException ignored) {
                } catch (NotExistGoodException e) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistGood"));
                    return;
                } catch (NotMoreGoodException e) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notMoreGoodWhenTradeGood"));
                    return;
                } catch (StateChangeException e) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.stateChange"));
                    return;
                } catch (NotEnoughItemStockException e) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEnoughItemStockWhenTradeGood"));
                    return;
                }

            }

            //背包添加物品
            PlayerUtil.addItem(player, ItemStackUtil.itemStackDeserialize(goodMeta.getGoodItemStack()), amount);
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.tradeGood"));

        }
    }
}
