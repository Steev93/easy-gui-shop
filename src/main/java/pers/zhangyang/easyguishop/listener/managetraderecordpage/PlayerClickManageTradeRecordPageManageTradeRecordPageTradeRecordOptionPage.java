package pers.zhangyang.easyguishop.listener.managetraderecordpage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.ManageTradeRecordPage;
import pers.zhangyang.easyguishop.domain.ManageTradeRecordPageTradeRecordOptionPage;
import pers.zhangyang.easyguishop.meta.TradeRecordMeta;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiSerialButtonHandler;

@EventListener
public class PlayerClickManageTradeRecordPageManageTradeRecordPageTradeRecordOptionPage implements Listener {

    @GuiSerialButtonHandler(guiPage = ManageTradeRecordPage.class, from = 0, to = 44)
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        int slot = event.getRawSlot();

        Player player = (Player) event.getWhoClicked();
        ManageTradeRecordPage manageTradeRecordPage = (ManageTradeRecordPage) holder;
        TradeRecordMeta tradeRecordMeta = manageTradeRecordPage.getTradeRecordMetaList().get(slot);
        ManageTradeRecordPageTradeRecordOptionPage manageTradeRecordPageTradeRecordOptionPage
                = new ManageTradeRecordPageTradeRecordOptionPage(manageTradeRecordPage, player, tradeRecordMeta);

        manageTradeRecordPageTradeRecordOptionPage.send();

    }

}