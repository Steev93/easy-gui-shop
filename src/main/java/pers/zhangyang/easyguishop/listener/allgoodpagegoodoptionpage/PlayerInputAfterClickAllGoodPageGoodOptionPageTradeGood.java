package pers.zhangyang.easyguishop.listener.allgoodpagegoodoptionpage;

import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import pers.zhangyang.easyguishop.EasyGuiShop;
import pers.zhangyang.easyguishop.domain.AllGoodPageGoodOptionPage;
import pers.zhangyang.easyguishop.exception.*;
import pers.zhangyang.easyguishop.meta.GoodMeta;
import pers.zhangyang.easyguishop.meta.TradeRecordMeta;
import pers.zhangyang.easyguishop.other.playerpoints.PlayerPoints;
import pers.zhangyang.easyguishop.other.vault.Vault;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.*;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easyguishop.yaml.SettingYaml;

import java.sql.SQLException;
import java.util.UUID;

public class PlayerInputAfterClickAllGoodPageGoodOptionPageTradeGood implements Listener {

    private final Player player;
    private final GoodMeta goodMeta;
    private final AllGoodPageGoodOptionPage allGoodPageGoodOptionPage;

    public PlayerInputAfterClickAllGoodPageGoodOptionPageTradeGood(Player player, GoodMeta goodMeta, AllGoodPageGoodOptionPage manageShopPage) {
        this.allGoodPageGoodOptionPage = manageShopPage;
        this.player = player;
        this.goodMeta = goodMeta;
        Bukkit.getPluginManager().registerEvents(this, EasyGuiShop.instance);
        player.closeInventory();
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToTrade"));
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        if (!player.equals(this.player)) {
            return;
        }
        event.setCancelled(true);
        String input = event.getMessage();
        if (input.equalsIgnoreCase(MessageYaml.INSTANCE.getInput("message.input.cancel"))) {
            unregisterSelf();
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        allGoodPageGoodOptionPage.send();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }.runTask(EasyGuiShop.instance);

            return;
        }

        unregisterSelf();

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    allGoodPageGoodOptionPage.send();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                int amount;
                try {
                    amount = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.invalidNumber"));
                    return;
                }
                if (amount < 0) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.invalidNumber"));
                    return;
                }


                GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();

                //如果是收购，检查玩家是不是有物品  如果出售，检查玩家是不是有空间
                //检查限购时间 检查是否设置价格  检查是否玩家有货币     然后购买 扣除货币  添加物品

                //限购时间
                Integer limitTime = goodMeta.getLimitTime();
                if (limitTime != null && limitTime * 1000 + goodMeta.getCreateTime() < System.currentTimeMillis()) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.exceedLimitTime"));
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
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notSetPrice"));
                    return;
                }


                if (goodMeta.getType().equalsIgnoreCase("收购")) {
                    //检查玩家手中是否有货
                    int have = InventoryUtil.computeItemHave(ItemStackUtil.itemStackDeserialize(goodMeta.getGoodItemStack()), player);
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
                            TradeRecordMeta tradeRecordMeta = new TradeRecordMeta(UuidUtil.getUUID(), player.getUniqueId().toString(),
                                    merchant.getUniqueId().toString(), goodMeta.getGoodItemStack(), amount, goodMeta.isSystem(),
                                    System.currentTimeMillis(), goodMeta.getType(), taxRate);
                            tradeRecordMeta.setGoodVaultPrice(goodMeta.getVaultPrice());
                            guiService.createTradeRecord(tradeRecordMeta);
                            allGoodPageGoodOptionPage.send();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            return;
                        } catch (NotExistGoodException e) {
                            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistGood"));
                            return;
                        } catch (DuplicateTradeRecordException | NotMoreGoodException ignored) {
                        } catch (StateChangeException e) {
                            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.stateChange"));
                            return;
                        }
                        //扣除货币
                        vault.depositPlayer(player, afterTax);
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
                            TradeRecordMeta tradeRecordMeta = new TradeRecordMeta(UuidUtil.getUUID(), player.getUniqueId().toString(),
                                    merchant.getUniqueId().toString(), goodMeta.getGoodItemStack(), amount, goodMeta.isSystem(),
                                    System.currentTimeMillis(), goodMeta.getType(), taxRate);
                            tradeRecordMeta.setGoodPlayerPointsPrice(goodMeta.getPlayerPointsPrice());
                            guiService.createTradeRecord(tradeRecordMeta);
                            allGoodPageGoodOptionPage.send();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            return;
                        } catch (NotExistGoodException e) {
                            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistGood"));
                            return;
                        } catch (DuplicateTradeRecordException | NotMoreGoodException ignored) {
                        } catch (StateChangeException e) {
                            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.stateChange"));
                            return;
                        }
                        playerPoints.give(player.getUniqueId(), afterTax);

                        if (!goodMeta.isSystem()) {
                            playerPoints.take(merchant.getUniqueId(), beforeTax);
                        }
                    }
                    if (itemPrice != null) {

                        try {
                            double taxRate = SettingYaml.INSTANCE.getTax("setting.tax.item");
                            int beforeTax = itemPrice * amount;
                            int tax = (int) Math.round(beforeTax * taxRate);
                            int afterTax = beforeTax - tax;
                            //检查钱
                            if (!goodMeta.isSystem() && !guiService.hasItemStock(merchant.getUniqueId().toString(), goodMeta.getCurrencyItemStack(), beforeTax)) {
                                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notMoreItemStockWhenTradeGood"));
                                return;
                            }
                            //交易
                            guiService.trade(goodMeta.getUuid(), amount, goodMeta);
                            TradeRecordMeta tradeRecordMeta = new TradeRecordMeta(UuidUtil.getUUID(), player.getUniqueId().toString(),
                                    allGoodPageGoodOptionPage.getShopMeta().getOwnerUuid(),
                                    goodMeta.getGoodItemStack(), amount, goodMeta.isSystem(), System.currentTimeMillis(), goodMeta.getType(),
                                    taxRate);
                            tradeRecordMeta.setGoodItemPrice(goodMeta.getItemPrice());
                            tradeRecordMeta.setGoodCurrencyItemStack(goodMeta.getCurrencyItemStack());
                            guiService.createTradeRecord(tradeRecordMeta);
                            allGoodPageGoodOptionPage.send();

                            //转钱

                            if (!goodMeta.isSystem()) {
                                guiService.takeItemStock(merchant.getUniqueId().toString(), goodMeta.getCurrencyItemStack(), beforeTax);
                            }
                            guiService.depositItemStock(player.getUniqueId().toString(), goodMeta.getCurrencyItemStack(), afterTax);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            return;
                        } catch (NotMoreItemStockException | NotMoreGoodException | DuplicateTradeRecordException | NotExistItemStockException ignored) {
                        } catch (NotExistGoodException e) {
                            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistGood"));
                            return;
                        } catch (StateChangeException e) {
                            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.stateChange"));
                            return;
                        }
                    }
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.tradeGood"));
                    //背包扣除物品
                    InventoryUtil.removeItem(player, ItemStackUtil.itemStackDeserialize(goodMeta.getGoodItemStack()), amount);

                } else if (goodMeta.getType().equalsIgnoreCase("出售")) {
                    //检查是不是有空间
                    int space = InventoryUtil.checkSpace(player, ItemStackUtil.itemStackDeserialize(goodMeta.getGoodItemStack()));
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
                        if (!vault.has(player, beforeTax)) {
                            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEnoughVaultWhenTradeGood"));
                            return;
                        }
                        //交易
                        try {
                            guiService.trade(goodMeta.getUuid(), amount, goodMeta);
                            TradeRecordMeta tradeRecordMeta = new TradeRecordMeta(UuidUtil.getUUID(), player.getUniqueId().toString(),
                                    merchant.getUniqueId().toString(), goodMeta.getGoodItemStack(), amount, goodMeta.isSystem(),
                                    System.currentTimeMillis(), goodMeta.getType(), taxRate);
                            tradeRecordMeta.setGoodVaultPrice(goodMeta.getVaultPrice());
                            guiService.createTradeRecord(tradeRecordMeta);
                            allGoodPageGoodOptionPage.send();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            return;
                        } catch (NotExistGoodException e) {
                            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistGood"));
                            return;
                        } catch (DuplicateTradeRecordException ignored) {
                        } catch (NotMoreGoodException e) {
                            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notMoreGood"));
                            return;
                        } catch (StateChangeException e) {
                            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.stateChange"));
                            return;
                        }
                        vault.withdrawPlayer(player, beforeTax);
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
                        if (playerPoints.look(player.getUniqueId()) < beforeTax) {
                            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEnoughPlayerPointsWhenTradeGood"));
                            return;
                        }


                        try {
                            guiService.trade(goodMeta.getUuid(), amount, goodMeta);
                            TradeRecordMeta tradeRecordMeta = new TradeRecordMeta(UuidUtil.getUUID(), player.getUniqueId().toString(),
                                    merchant.getUniqueId().toString(), goodMeta.getGoodItemStack(), amount, goodMeta.isSystem(),
                                    System.currentTimeMillis(), goodMeta.getType(), taxRate);
                            tradeRecordMeta.setGoodPlayerPointsPrice(goodMeta.getPlayerPointsPrice());
                            guiService.createTradeRecord(tradeRecordMeta);
                            allGoodPageGoodOptionPage.send();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            return;
                        } catch (NotExistGoodException e) {
                            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistGood"));
                            return;
                        } catch (DuplicateTradeRecordException ignored) {
                        } catch (NotMoreGoodException e) {
                            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notMoreGood"));
                            return;
                        } catch (StateChangeException e) {
                            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.stateChange"));
                            return;
                        }
                        playerPoints.take(player.getUniqueId(), beforeTax);

                        if (!goodMeta.isSystem()) {
                            playerPoints.give(merchant.getUniqueId(), afterTax);
                        }

                    }
                    if (itemPrice != null) {
                        double taxRate = SettingYaml.INSTANCE.getTax("setting.tax.item");
                        int beforeTax = itemPrice * amount;
                        int tax = (int) Math.round(beforeTax * taxRate);
                        int afterTax = beforeTax - tax;
                        //扣除货币
                        try {
                            //检查钱
                            if (!guiService.hasItemStock(player.getUniqueId().toString(), goodMeta.getCurrencyItemStack(), beforeTax)) {
                                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEnoughItemStockWhenTradeGood"));
                                return;
                            }
                            //交易
                            guiService.trade(goodMeta.getUuid(), amount, goodMeta);
                            TradeRecordMeta tradeRecordMeta = new TradeRecordMeta(UuidUtil.getUUID(), player.getUniqueId().toString(),
                                    merchant.getUniqueId().toString(), goodMeta.getGoodItemStack(), amount, goodMeta.isSystem(),
                                    System.currentTimeMillis(), goodMeta.getType(), taxRate);
                            tradeRecordMeta.setGoodItemPrice(goodMeta.getItemPrice());
                            tradeRecordMeta.setGoodCurrencyItemStack(goodMeta.getCurrencyItemStack());
                            guiService.createTradeRecord(tradeRecordMeta);
                            //扣钱
                            guiService.takeItemStock(player.getUniqueId().toString(), goodMeta.getCurrencyItemStack(), beforeTax);

                            if (!goodMeta.isSystem()) {
                                guiService.depositItemStock(merchant.getUniqueId().toString(), goodMeta.getCurrencyItemStack(), afterTax);
                            }
                            allGoodPageGoodOptionPage.send();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            return;
                        } catch (NotMoreItemStockException | DuplicateTradeRecordException | NotExistItemStockException ignored) {
                        } catch (NotExistGoodException e) {
                            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistGood"));
                            return;
                        } catch (NotMoreGoodException e) {
                            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notMoreGood"));
                            return;
                        } catch (StateChangeException e) {
                            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.stateChange"));
                            return;
                        }

                    }

                    //背包添加物品
                    InventoryUtil.addItem(player, ItemStackUtil.itemStackDeserialize(goodMeta.getGoodItemStack()), amount);
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.tradeGood"));

                }
            }
        }.runTask(EasyGuiShop.instance);
    }


    private void unregisterSelf() {
        AsyncPlayerChatEvent.getHandlerList().unregister(this);
        PlayerQuitEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!event.getPlayer().equals(this.player)) {
            return;
        }
        unregisterSelf();
    }


}
