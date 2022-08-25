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

public class ManageGoodPageGoodOptionPage extends SingleGuiPageBase implements BackAble {
    private ShopMeta shopMeta;
    private GoodMeta goodMeta;

    public ManageGoodPageGoodOptionPage(GuiPage previousHolder, Player player, ShopMeta shopMeta, GoodMeta goodMeta) {
        super(GuiYaml.INSTANCE.getString("gui.title.manageGoodPageGoodOptionPage"), player, previousHolder, previousHolder.getOwner(),54);
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

        ItemStack currencyGuide = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageGoodPageGoodOptionPage.guideGoodAndCurrency");
        inventory.setItem(4, currencyGuide);

        HashMap<String, String> rep = new HashMap<>();
        ItemStack currency = null;
        if (goodMeta.getVaultPrice() != null) {
            currency = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageGoodPageGoodOptionPage.vaultCurrency");
            rep.put("{price}", String.valueOf(goodMeta.getVaultPrice()));
        } else if (goodMeta.getPlayerPointsPrice() != null) {
            currency = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageGoodPageGoodOptionPage.playerPointsCurrency");
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
        ItemStack information = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageGoodPageGoodOptionPage.goodInformation");
        ReplaceUtil.replaceLore(information, rep);
        ReplaceUtil.replaceDisplayName(information, rep);
        inventory.setItem(31, information);

        ItemStack icon = ItemStackUtil.itemStackDeserialize(goodMeta.getGoodItemStack());
        inventory.setItem(3, icon);

        ItemStack deposit = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageGoodPageGoodOptionPage.depositGood");
        inventory.setItem(21, deposit);

            ItemStack take = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageGoodPageGoodOptionPage.takeGood");
            inventory.setItem(23, take);


        ItemStack setName = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageGoodPageGoodOptionPage.setGoodName");
        inventory.setItem(22, setName);

        ItemStack createVaultGood = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageGoodPageGoodOptionPage.setGoodVaultPrice");
        inventory.setItem(12, createVaultGood);

        ItemStack createPlayerPointsGood = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageGoodPageGoodOptionPage.setGoodPlayerPointsPrice");
        inventory.setItem(13, createPlayerPointsGood);

        ItemStack resetLimitTime = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageGoodPageGoodOptionPage.resetGoodLimitTime");
        inventory.setItem(39, resetLimitTime);
        ItemStack resetPrice = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageGoodPageGoodOptionPage.resetGoodPrice");
        inventory.setItem(41, resetPrice);

        ItemStack setLimitTime = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageGoodPageGoodOptionPage.setGoodLimitTime");
        inventory.setItem(30, setLimitTime);

        ItemStack changeTransactionType = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageGoodPageGoodOptionPage.toggleGoodType");
        inventory.setItem(32, changeTransactionType);

        ItemStack createItemGood = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageGoodPageGoodOptionPage.setGoodItemPrice");
        inventory.setItem(14, createItemGood);


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



    @Override
    public int getBackSlot() {
        return 49;
    }
}
