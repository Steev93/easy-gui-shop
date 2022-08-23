package pers.zhangyang.easyguishop.domain;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.meta.IconMeta;
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

public class BuyIconPageIconOptionPage extends SingleGuiPageBase implements BackAble {
    private IconMeta iconMeta;

    public BuyIconPageIconOptionPage(GuiPage backPage, Player player, IconMeta iconMeta) {
        super(GuiYaml.INSTANCE.getString("gui.title.buyIconPageIconOptionPage"), player, backPage, backPage.getOwner(),54);
        this.iconMeta = iconMeta;
    }


    @Override
    public void refresh() {
        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
        this.iconMeta = guiService.getIcon(iconMeta.getUuid());
        if (this.iconMeta == null) {
            backPage.send();
            return;
        }

        this.inventory.clear();
        ItemStack currencyGuide = GuiYaml.INSTANCE.getButtonDefault("gui.button.buyIconPageIconOptionPage.guideIconAndCurrency");
        inventory.setItem(13, currencyGuide);

        HashMap<String, String> rep = new HashMap<>();
        ItemStack currency = null;
        if (iconMeta.getVaultPrice() != null) {
            currency = GuiYaml.INSTANCE.getButtonDefault("gui.button.buyIconPageIconOptionPage.vaultCurrency");
            rep.put("{price}", String.valueOf(iconMeta.getVaultPrice()));
        } else if (iconMeta.getPlayerPointsPrice() != null) {
            currency = GuiYaml.INSTANCE.getButtonDefault("gui.button.buyIconPageIconOptionPage.playerPointsCurrency");
            rep.put("{price}", String.valueOf(iconMeta.getPlayerPointsPrice()));
        } else if (iconMeta.getCurrencyItemStack() != null) {
            currency = ItemStackUtil.itemStackDeserialize(iconMeta.getCurrencyItemStack());
            rep.put("{price}", String.valueOf(iconMeta.getItemPrice()));
        } else {
            rep.put("{price}", "\\");
        }
        if (iconMeta.isSystem()) {
            rep.put("{stock}", GuiYaml.INSTANCE.getStringDefault("gui.replace.systemStock"));
        } else {
            rep.put("{stock}", String.valueOf(iconMeta.getStock()));
        }
        rep.put("{name}", iconMeta.getName());
        rep.put("{create_time}", TimeUtil.getTimeFromTimeMill(iconMeta.getCreateTime()));
        inventory.setItem(22, currency);

        if (iconMeta.getLimitTime() != null) {
            rep.put("{limit_time}", String.valueOf(iconMeta.getLimitTime()));
        } else {
            rep.put("{limit_time}", "\\");
        }
        ItemStack information = GuiYaml.INSTANCE.getButtonDefault("gui.button.buyIconPageIconOptionPage.iconInformation");
        ReplaceUtil.replaceLore(information, rep);
        ReplaceUtil.replaceDisplayName(information, rep);
        inventory.setItem(31, information);

        ItemStack icon = ItemStackUtil.itemStackDeserialize(iconMeta.getIconItemStack());
        inventory.setItem(4, icon);

        if (currency != null) {
            if (iconMeta.isSystem()) {
                if (iconMeta.getLimitTime() == null) {
                    ItemStack buyIcon = GuiYaml.INSTANCE.getButtonDefault("gui.button.buyIconPageIconOptionPage.buyIcon");
                    inventory.setItem(40, buyIcon);
                }
                if (iconMeta.getLimitTime() != null && iconMeta.getLimitTime() * 1000 + iconMeta.getCreateTime() > System.currentTimeMillis()) {
                    ItemStack buyIcon = GuiYaml.INSTANCE.getButtonDefault("gui.button.buyIconPageIconOptionPage.buyIcon");
                    inventory.setItem(40, buyIcon);
                }
            }

            if (!iconMeta.isSystem() && iconMeta.getStock() > 0) {
                if (iconMeta.getLimitTime() == null) {
                    ItemStack buyIcon = GuiYaml.INSTANCE.getButtonDefault("gui.button.buyIconPageIconOptionPage.buyIcon");
                    inventory.setItem(40, buyIcon);
                }
                if (iconMeta.getLimitTime() != null && iconMeta.getLimitTime() * 1000 + iconMeta.getCreateTime() > System.currentTimeMillis()) {
                    ItemStack buyIcon = GuiYaml.INSTANCE.getButtonDefault("gui.button.buyIconPageIconOptionPage.buyIcon");
                    inventory.setItem(40, buyIcon);
                }
            }


        }

        ItemStack back = GuiYaml.INSTANCE.getButtonDefault("gui.button.buyIconPageIconOptionPage.back");
        inventory.setItem(49, back);
        viewer.openInventory(this.inventory);
    }

    public IconMeta getIconMeta() {
        return iconMeta;
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



    @Override
    public int getBackSlot() {
        return 49;
    }
}