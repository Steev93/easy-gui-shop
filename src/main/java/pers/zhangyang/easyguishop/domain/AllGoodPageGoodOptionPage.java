package pers.zhangyang.easyguishop.domain;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.meta.GoodMeta;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.GuiYaml;
import pers.zhangyang.easylibrary.base.BackAble;
import pers.zhangyang.easylibrary.base.GuiPage;
import pers.zhangyang.easylibrary.base.SingleGuiPageBase;
import pers.zhangyang.easylibrary.util.ItemStackUtil;
import pers.zhangyang.easylibrary.util.ReplaceUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

import java.util.HashMap;

public class AllGoodPageGoodOptionPage extends SingleGuiPageBase implements BackAble {
    private ShopMeta shopMeta;
    private GoodMeta goodMeta;

    public AllGoodPageGoodOptionPage(GuiPage backPage, Player player, ShopMeta shopMeta, GoodMeta goodMeta) {
        super(GuiYaml.INSTANCE.getString("gui.title.allGoodPageGoodOptionPage"), player, backPage, backPage.getOwner());
        this.shopMeta = shopMeta;
        this.goodMeta = goodMeta;

    }

    @Override
    public void refresh() {
        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
        this.shopMeta = guiService.getShop(shopMeta.getUuid());
        this.goodMeta = guiService.getGood(goodMeta.getUuid());
        if (this.goodMeta == null) {
            backPage.send();
            return;
        }

        this.inventory.clear();

        ItemStack currencyGuide = GuiYaml.INSTANCE.getButtonDefault("gui.button.allGoodPageGoodOptionPage.guideGoodAndCurrency");
        inventory.setItem(13, currencyGuide);

        HashMap<String, String> rep = new HashMap<>();
        ItemStack currency = null;
        if (goodMeta.getVaultPrice() != null) {
            currency = GuiYaml.INSTANCE.getButtonDefault("gui.button.allGoodPageGoodOptionPage.vaultCurrency");
            rep.put("{price}", String.valueOf(goodMeta.getVaultPrice()));
        } else if (goodMeta.getPlayerPointsPrice() != null) {
            currency = GuiYaml.INSTANCE.getButtonDefault("gui.button.allGoodPageGoodOptionPage.playerPointsCurrency");
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
        ItemStack information = GuiYaml.INSTANCE.getButtonDefault("gui.button.allGoodPageGoodOptionPage.goodInformation");
        ReplaceUtil.replaceLore(information, rep);
        ReplaceUtil.replaceDisplayName(information, rep);
        inventory.setItem(31, information);

        ItemStack icon = ItemStackUtil.itemStackDeserialize(goodMeta.getGoodItemStack());
        inventory.setItem(4, icon);


        if (currency != null) {
            if (goodMeta.getType().equalsIgnoreCase("收购")) {
                if (goodMeta.getLimitTime() == null) {
                    ItemStack trade = GuiYaml.INSTANCE.getButtonDefault("gui.button.allGoodPageGoodOptionPage.tradeGood");
                    inventory.setItem(40, trade);
                }
                if (goodMeta.getLimitTime() != null && goodMeta.getLimitTime() * 1000 + goodMeta.getCreateTime() > System.currentTimeMillis()) {

                    ItemStack trade = GuiYaml.INSTANCE.getButtonDefault("gui.button.allGoodPageGoodOptionPage.tradeGood");
                    inventory.setItem(40, trade);
                }
            }

            if (goodMeta.getType().equalsIgnoreCase("出售")) {
                if (goodMeta.isSystem()) {
                    if (goodMeta.getLimitTime() == null) {
                        ItemStack trade = GuiYaml.INSTANCE.getButtonDefault("gui.button.allGoodPageGoodOptionPage.tradeGood");
                        inventory.setItem(40, trade);
                    }
                    if (goodMeta.getLimitTime() != null && goodMeta.getLimitTime() * 1000 + goodMeta.getCreateTime() > System.currentTimeMillis()) {

                        ItemStack trade = GuiYaml.INSTANCE.getButtonDefault("gui.button.allGoodPageGoodOptionPage.tradeGood");
                        inventory.setItem(40, trade);
                    }
                }
                if (!goodMeta.isSystem() && goodMeta.getStock() > 0) {
                    if (goodMeta.getLimitTime() == null) {
                        ItemStack trade = GuiYaml.INSTANCE.getButtonDefault("gui.button.allGoodPageGoodOptionPage.tradeGood");
                        inventory.setItem(40, trade);
                    }
                    if (goodMeta.getLimitTime() != null && goodMeta.getLimitTime() * 1000 + goodMeta.getCreateTime() > System.currentTimeMillis()) {

                        ItemStack trade = GuiYaml.INSTANCE.getButtonDefault("gui.button.allGoodPageGoodOptionPage.tradeGood");
                        inventory.setItem(40, trade);
                    }
                }
            }

        }


        ItemStack back = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageGoodPageGoodOptionPage.back");
        inventory.setItem(49, back);
        viewer.openInventory(inventory);
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
        return backPage;
    }

    @Override
    public void back() {
        backPage.refresh();
    }
}
