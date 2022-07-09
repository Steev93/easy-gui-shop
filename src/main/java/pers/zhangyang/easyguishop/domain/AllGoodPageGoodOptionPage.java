package pers.zhangyang.easyguishop.domain;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.meta.GoodMeta;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.ItemStackUtil;
import pers.zhangyang.easyguishop.util.ReplaceUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.GuiYaml;

import java.sql.SQLException;
import java.util.HashMap;

public class AllGoodPageGoodOptionPage implements InventoryHolder {
    private final Inventory inventory;
    private final InventoryHolder previousHolder;
    private final Player player;
    private ShopMeta shopMeta;
    private GoodMeta goodMeta;

    public AllGoodPageGoodOptionPage(InventoryHolder previousHolder, Player player, ShopMeta shopMeta, GoodMeta goodMeta) {
        this.player = player;
        this.shopMeta = shopMeta;
        this.goodMeta = goodMeta;
        this.previousHolder = previousHolder;
        String title = GuiYaml.INSTANCE.getString("gui.title.allGoodPageGoodOptionPage");
        if (title == null) {
            this.inventory = Bukkit.createInventory(this, 54);
        } else {
            this.inventory = Bukkit.createInventory(this, 54, ChatColor.translateAlternateColorCodes('&', title));
        }
    }

    //根据Shop的情况来设置Button
    public void send() throws SQLException {
        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();
        this.shopMeta = guiService.getShop(shopMeta.getUuid());
        this.goodMeta = guiService.getGood(goodMeta.getUuid());
        if (this.goodMeta == null) {
            ((ManageGoodPage) previousHolder).send();
            return;
        }

        this.inventory.clear();

        ItemStack currencyGuide = GuiYaml.INSTANCE.getButton("gui.button.allGoodPageGoodOptionPage.guideGoodAndCurrency");
        inventory.setItem(13, currencyGuide);

        HashMap<String, String> rep = new HashMap<>();
        ItemStack currency = null;
        if (goodMeta.getVaultPrice() != null) {
            currency = GuiYaml.INSTANCE.getButton("gui.button.allGoodPageGoodOptionPage.vaultCurrency");
            rep.put("{price}", String.valueOf(goodMeta.getVaultPrice()));
        } else if (goodMeta.getPlayerPointsPrice() != null) {
            currency = GuiYaml.INSTANCE.getButton("gui.button.allGoodPageGoodOptionPage.playerPointsCurrency");
            rep.put("{price}", String.valueOf(goodMeta.getPlayerPointsPrice()));
        } else if (goodMeta.getCurrencyItemStack() != null) {
            currency = ItemStackUtil.itemStackDeserialize(goodMeta.getCurrencyItemStack());
            rep.put("{price}", String.valueOf(goodMeta.getItemPrice()));
        } else {
            rep.put("{price}", "\\");
        }
        rep.put("{type}", goodMeta.getType());
        rep.put("{name}", goodMeta.getName());
        if (goodMeta.isSystem()) {
            rep.put("{stock}", GuiYaml.INSTANCE.getStringDefault("gui.replace.systemStock"));
        } else {
            rep.put("{stock}", String.valueOf(goodMeta.getStock()));
        }
        inventory.setItem(22, currency);

        if (goodMeta.getLimitTime() != null) {
            rep.put("{limit_time}", String.valueOf(goodMeta.getLimitTime()));
        } else {
            rep.put("{limit_time}", "\\");
        }
        ItemStack information = GuiYaml.INSTANCE.getButton("gui.button.allGoodPageGoodOptionPage.goodInformation");
        ReplaceUtil.replaceLore(information, rep);
        ReplaceUtil.replaceDisplayName(information, rep);
        inventory.setItem(31, information);

        ItemStack icon = ItemStackUtil.itemStackDeserialize(goodMeta.getGoodItemStack());
        inventory.setItem(4, icon);


        if (currency != null && goodMeta.getStock() > 0) {
            if (goodMeta.getLimitTime() == null) {
                ItemStack trade = GuiYaml.INSTANCE.getButton("gui.button.allGoodPageGoodOptionPage.tradeGood");
                inventory.setItem(40, trade);
            }
            if (goodMeta.getLimitTime() != null && goodMeta.getLimitTime() * 1000 + goodMeta.getCreateTime() > System.currentTimeMillis()) {

                ItemStack trade = GuiYaml.INSTANCE.getButton("gui.button.allGoodPageGoodOptionPage.tradeGood");
                inventory.setItem(40, trade);
            }
        }


        ItemStack back = GuiYaml.INSTANCE.getButton("gui.button.manageGoodPageGoodOptionPage.back");
        inventory.setItem(49, back);
        player.openInventory(inventory);
    }

    public ShopMeta getShopMeta() {
        return shopMeta;
    }

    public GoodMeta getGoodMeta() {
        return goodMeta;
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
