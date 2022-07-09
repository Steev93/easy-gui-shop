package pers.zhangyang.easyguishop.domain;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.meta.IconMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.ItemStackUtil;
import pers.zhangyang.easyguishop.util.ReplaceUtil;
import pers.zhangyang.easyguishop.util.TimeUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.GuiYaml;

import java.sql.SQLException;
import java.util.HashMap;

public class BuyIconPageIconOptionPage implements InventoryHolder {
    private final Inventory inventory;
    private final InventoryHolder previousHolder;
    private final Player player;
    private IconMeta iconMeta;

    public BuyIconPageIconOptionPage(InventoryHolder previousHolder, Player player, IconMeta iconMeta) {
        this.player = player;
        this.iconMeta = iconMeta;
        this.previousHolder = previousHolder;
        String title = GuiYaml.INSTANCE.getString("gui.title.buyIconPageIconOptionPage");
        if (title == null) {
            this.inventory = Bukkit.createInventory(this, 54);
        } else {
            this.inventory = Bukkit.createInventory(this, 54, ChatColor.translateAlternateColorCodes('&', title));
        }
    }


    //根据Shop的情况来设置Button
    public void send() throws SQLException {

        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();
        this.iconMeta = guiService.getIcon(iconMeta.getUuid());
        if (this.iconMeta == null) {
            ((BuyIconPage) previousHolder).send();
            return;
        }

        this.inventory.clear();
        ItemStack currencyGuide = GuiYaml.INSTANCE.getButton("gui.button.buyIconPageIconOptionPage.guideIconAndCurrency");
        inventory.setItem(13, currencyGuide);

        HashMap<String, String> rep = new HashMap<>();
        ItemStack currency = null;
        if (iconMeta.getVaultPrice() != null) {
            currency = GuiYaml.INSTANCE.getButton("gui.button.buyIconPageIconOptionPage.vaultCurrency");
            rep.put("{price}", String.valueOf(iconMeta.getVaultPrice()));
        } else if (iconMeta.getPlayerPointsPrice() != null) {
            currency = GuiYaml.INSTANCE.getButton("gui.button.buyIconPageIconOptionPage.playerPointsCurrency");
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
        ItemStack information = GuiYaml.INSTANCE.getButton("gui.button.buyIconPageIconOptionPage.iconInformation");
        ReplaceUtil.replaceLore(information, rep);
        ReplaceUtil.replaceDisplayName(information, rep);
        inventory.setItem(31, information);

        ItemStack icon = ItemStackUtil.itemStackDeserialize(iconMeta.getIconItemStack());
        inventory.setItem(4, icon);

        if (currency != null && iconMeta.getStock() > 0) {
            if (iconMeta.getLimitTime() == null) {
                ItemStack buyIcon = GuiYaml.INSTANCE.getButton("gui.button.buyIconPageIconOptionPage.buyIcon");
                inventory.setItem(40, buyIcon);
            }
            if (iconMeta.getLimitTime() != null && iconMeta.getLimitTime() * 1000 + iconMeta.getCreateTime() > System.currentTimeMillis()) {
                ItemStack buyIcon = GuiYaml.INSTANCE.getButton("gui.button.buyIconPageIconOptionPage.buyIcon");
                inventory.setItem(40, buyIcon);
            }
        }

        ItemStack back = GuiYaml.INSTANCE.getButton("gui.button.buyIconPageIconOptionPage.back");
        inventory.setItem(49, back);
        player.openInventory(this.inventory);
    }

    public IconMeta getIconMeta() {
        return iconMeta;
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