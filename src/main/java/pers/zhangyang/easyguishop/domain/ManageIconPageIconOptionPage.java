package pers.zhangyang.easyguishop.domain;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.meta.IconMeta;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.ItemStackUtil;
import pers.zhangyang.easyguishop.util.ReplaceUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.GuiYaml;

import java.sql.SQLException;
import java.util.HashMap;

public class ManageIconPageIconOptionPage implements InventoryHolder {
    private final Inventory inventory;
    private final InventoryHolder previousHolder;
    private final Player player;
    private IconMeta iconMeta;
    private ShopMeta shopMeta;

    public ManageIconPageIconOptionPage(InventoryHolder previousHolder, Player player, IconMeta iconMeta, ShopMeta shopMeta) {
        this.player = player;
        this.iconMeta = iconMeta;
        this.previousHolder = previousHolder;
        this.shopMeta = shopMeta;
        String title = GuiYaml.INSTANCE.getString("gui.title.manageIconPageIconOptionPage");
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
        if (this.shopMeta == null) {
            ((ManageIconPage) previousHolder).send();
            return;
        }
        this.iconMeta = guiService.getIcon(iconMeta.getUuid());
        if (this.iconMeta == null) {
            ((ManageIconPage) previousHolder).send();
            return;
        }

        this.inventory.clear();
        ItemStack currencyGuide = GuiYaml.INSTANCE.getButton("gui.button.manageIconPageIconOptionPage.guideIcon");
        inventory.setItem(13, currencyGuide);

        HashMap<String, String> rep = new HashMap<>();
        rep.put("{name}", iconMeta.getName());

        ItemStack information = GuiYaml.INSTANCE.getButton("gui.button.manageIconPageIconOptionPage.iconInformation");
        ReplaceUtil.replaceLore(information, rep);
        ReplaceUtil.replaceDisplayName(information, rep);
        inventory.setItem(31, information);

        ItemStack icon = ItemStackUtil.itemStackDeserialize(iconMeta.getIconItemStack());
        inventory.setItem(22, icon);

        ItemStack buyIcon = GuiYaml.INSTANCE.getButton("gui.button.manageIconPageIconOptionPage.useShopIcon");
        inventory.setItem(40, buyIcon);


        ItemStack back = GuiYaml.INSTANCE.getButton("gui.button.buyIconPageIconOptionPage.back");
        inventory.setItem(49, back);
        player.openInventory(this.inventory);
    }

    public ShopMeta getShopMeta() {
        return shopMeta;
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
