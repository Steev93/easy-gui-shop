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

public class ManageGoodPageGoodOptionPage implements InventoryHolder {
    private final Inventory inventory;
    private final InventoryHolder previousHolder;
    private final Player player;
    private ShopMeta shopMeta;
    private GoodMeta goodMeta;

    public ManageGoodPageGoodOptionPage(InventoryHolder previousHolder, Player player, ShopMeta shopMeta, GoodMeta goodMeta) {
        this.player = player;
        this.shopMeta = shopMeta;
        this.goodMeta = goodMeta;
        this.previousHolder = previousHolder;
        String title = GuiYaml.INSTANCE.getString("gui.title.manageGoodPageGoodOptionPage");
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

        ItemStack currencyGuide = GuiYaml.INSTANCE.getButton("gui.button.manageGoodPageGoodOptionPage.guideGoodAndCurrency");
        inventory.setItem(4, currencyGuide);

        HashMap<String, String> rep = new HashMap<>();
        ItemStack currency = null;
        if (goodMeta.getVaultPrice() != null) {
            currency = GuiYaml.INSTANCE.getButton("gui.button.manageGoodPageGoodOptionPage.vaultCurrency");
            rep.put("{price}", String.valueOf(goodMeta.getVaultPrice()));
        } else if (goodMeta.getPlayerPointsPrice() != null) {
            currency = GuiYaml.INSTANCE.getButton("gui.button.manageGoodPageGoodOptionPage.playerPointsCurrency");
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
        inventory.setItem(5, currency);

        if (goodMeta.getLimitTime() != null) {
            rep.put("{limit_time}", String.valueOf(goodMeta.getLimitTime()));
        } else {
            rep.put("{limit_time}", "\\");
        }
        ItemStack information = GuiYaml.INSTANCE.getButton("gui.button.manageGoodPageGoodOptionPage.goodInformation");
        ReplaceUtil.replaceLore(information, rep);
        ReplaceUtil.replaceDisplayName(information, rep);
        inventory.setItem(31, information);

        ItemStack icon = ItemStackUtil.itemStackDeserialize(goodMeta.getGoodItemStack());
        inventory.setItem(3, icon);

        ItemStack deposit = GuiYaml.INSTANCE.getButton("gui.button.manageGoodPageGoodOptionPage.depositGood");
        inventory.setItem(21, deposit);

        if (goodMeta.getStock() > 0) {
            ItemStack take = GuiYaml.INSTANCE.getButton("gui.button.manageGoodPageGoodOptionPage.takeGood");
            inventory.setItem(23, take);
        }

        ItemStack setName = GuiYaml.INSTANCE.getButton("gui.button.manageGoodPageGoodOptionPage.setGoodName");
        inventory.setItem(22, setName);

        ItemStack createVaultGood = GuiYaml.INSTANCE.getButton("gui.button.manageGoodPageGoodOptionPage.setGoodVaultPrice");
        inventory.setItem(12, createVaultGood);

        ItemStack createPlayerPointsGood = GuiYaml.INSTANCE.getButton("gui.button.manageGoodPageGoodOptionPage.setGoodPlayerPointsPrice");
        inventory.setItem(13, createPlayerPointsGood);

        ItemStack resetLimitTime = GuiYaml.INSTANCE.getButton("gui.button.manageGoodPageGoodOptionPage.resetGoodLimitTime");
        inventory.setItem(39, resetLimitTime);
        ItemStack resetPrice = GuiYaml.INSTANCE.getButton("gui.button.manageGoodPageGoodOptionPage.resetGoodPrice");
        inventory.setItem(41, resetPrice);

        ItemStack setLimitTime = GuiYaml.INSTANCE.getButton("gui.button.manageGoodPageGoodOptionPage.setGoodLimitTime");
        inventory.setItem(30, setLimitTime);

        ItemStack changeTransactionType = GuiYaml.INSTANCE.getButton("gui.button.manageGoodPageGoodOptionPage.toggleGoodType");
        inventory.setItem(32, changeTransactionType);

        ItemStack createItemGood = GuiYaml.INSTANCE.getButton("gui.button.manageGoodPageGoodOptionPage.setGoodItemPrice");
        inventory.setItem(14, createItemGood);


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
