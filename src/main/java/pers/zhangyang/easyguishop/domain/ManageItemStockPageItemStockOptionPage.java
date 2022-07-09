package pers.zhangyang.easyguishop.domain;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.meta.ItemStockMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.ItemStackUtil;
import pers.zhangyang.easyguishop.util.ReplaceUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.GuiYaml;

import java.sql.SQLException;
import java.util.HashMap;

public class ManageItemStockPageItemStockOptionPage implements InventoryHolder {
    private final Inventory inventory;
    private final InventoryHolder previousHolder;
    private final Player player;
    private ItemStockMeta itemStockMeta;

    public ManageItemStockPageItemStockOptionPage(InventoryHolder previousHolder, Player player, ItemStockMeta shopMeta) {
        this.itemStockMeta = shopMeta;
        this.player = player;
        this.previousHolder = previousHolder;
        String title = GuiYaml.INSTANCE.getString("gui.title.manageItemStockPageItemStockOptionPage");
        if (title == null) {
            this.inventory = Bukkit.createInventory(this, 54);
        } else {
            this.inventory = Bukkit.createInventory(this, 54, ChatColor.translateAlternateColorCodes('&', title));
        }
    }

    //根据Shop的情况来设置Button
    public void send() throws SQLException {
        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();
        this.itemStockMeta = guiService.getItemStock(itemStockMeta.getPlayerUuid(), itemStockMeta.getItemStack());
        if (this.itemStockMeta == null) {
            ((ManageItemStockPage) previousHolder).send();
            return;
        }
        this.inventory.clear();
        ItemStack itemStock = ItemStackUtil.itemStackDeserialize(itemStockMeta.getItemStack());
        inventory.setItem(4, itemStock);
        ItemStack guideItemStock = GuiYaml.INSTANCE.getButton("gui.button.manageItemStockPageItemStockOptionPage.guideItemStock");
        inventory.setItem(13, guideItemStock);

        ItemStack information = GuiYaml.INSTANCE.getButton("gui.button.manageItemStockPageItemStockOptionPage.itemStockInformation");
        HashMap<String, String> rep = new HashMap<>();
        rep.put("{amount}", String.valueOf(itemStockMeta.getAmount()));
        ReplaceUtil.replaceDisplayName(information, rep);
        ReplaceUtil.replaceLore(information, rep);
        inventory.setItem(31, information);
        ItemStack depositItemStock = GuiYaml.INSTANCE.getButton("gui.button.manageItemStockPageItemStockOptionPage.depositItemStock");
        inventory.setItem(22, depositItemStock);

        if (itemStockMeta.getAmount() > 0) {
            ItemStack takeItemStock = GuiYaml.INSTANCE.getButton("gui.button.manageItemStockPageItemStockOptionPage.takeItemStock");
            inventory.setItem(21, takeItemStock);
        }
        if (itemStockMeta.getAmount() <= 0) {
            ItemStack deleteItemStock = GuiYaml.INSTANCE.getButton("gui.button.manageItemStockPageItemStockOptionPage.deleteItemStock");
            inventory.setItem(23, deleteItemStock);
        }
        ItemStack back = GuiYaml.INSTANCE.getButton("gui.button.manageItemStockPageItemStockOptionPage.back");
        inventory.setItem(49, back);


        player.openInventory(this.inventory);
    }

    public ItemStockMeta getItemStockMeta() {
        return itemStockMeta;
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
