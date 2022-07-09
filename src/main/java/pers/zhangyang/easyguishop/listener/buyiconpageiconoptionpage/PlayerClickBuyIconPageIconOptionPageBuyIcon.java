package pers.zhangyang.easyguishop.listener.buyiconpageiconoptionpage;

import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easyguishop.domain.BuyIconPageIconOptionPage;
import pers.zhangyang.easyguishop.exception.*;
import pers.zhangyang.easyguishop.meta.IconMeta;
import pers.zhangyang.easyguishop.other.playerpoints.PlayerPoints;
import pers.zhangyang.easyguishop.other.vault.Vault;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

import java.sql.SQLException;

public class PlayerClickBuyIconPageIconOptionPageBuyIcon implements Listener {

    @EventHandler
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof BuyIconPageIconOptionPage)) {
            return;
        }
        int slot = event.getRawSlot();
        if (slot != 40) {
            return;
        }
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || itemStack.getType().equals(Material.AIR)) {
            return;
        }
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        BuyIconPageIconOptionPage buyIconPageIconOptionPage = (BuyIconPageIconOptionPage) holder;
        Player player = (Player) event.getWhoClicked();
        GuiService guiService = (GuiService) new TransactionInvocationHandler(GuiServiceImpl.INSTANCE).getProxy();
        IconMeta iconMeta = buyIconPageIconOptionPage.getIconMeta();


        //检查是否限购
        Integer limitTime = iconMeta.getLimitTime();
        if (limitTime != null && limitTime * 1000 + iconMeta.getCreateTime() < System.currentTimeMillis()) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.exceedLimitTime"));
            return;
        }

        //检查货币
        Double vaultPrice = iconMeta.getVaultPrice();
        Integer itemPrice = iconMeta.getItemPrice();
        Integer playerPointsPrice = iconMeta.getPlayerPointsPrice();
        Economy vault = Vault.hook();
        PlayerPointsAPI playerPoints = PlayerPoints.hook();
        if (vaultPrice != null) {
            if (vault == null) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notHookVault"));
                return;
            }
            if (!vault.has(player, vaultPrice)) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEnoughVaultWhenBuyIcon"));
                return;
            }

            //得到图标
            try {
                buyIconPageIconOptionPage.send();
                guiService.buyIcon(player.getUniqueId().toString(), iconMeta.getUuid(), iconMeta);
                buyIconPageIconOptionPage.send();
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            } catch (DuplicateIconOwnerException e) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.duplicateIconOwnerWhenBuyIcon"));
                return;
            } catch (NotExistIconException e) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistIcon"));
                return;
            } catch (NotMoreIconException e) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notMoreIcon"));
                return;
            } catch (StateChangeException e) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.stateChange"));
                return;
            }

            vault.withdrawPlayer(player, vaultPrice);
        } else if (playerPointsPrice != null) {
            if (playerPoints == null) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notHookPlayerPoints"));
                return;
            }
            if (playerPoints.look(player.getUniqueId()) < playerPointsPrice) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEnoughPlayerPointsWhenBuyIcon"));
                return;
            }
            //得到图标
            try {
                buyIconPageIconOptionPage.send();
                guiService.buyIcon(player.getUniqueId().toString(), iconMeta.getUuid(), iconMeta);
                buyIconPageIconOptionPage.send();
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            } catch (DuplicateIconOwnerException e) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.duplicateIconOwnerWhenBuyIcon"));
                return;
            } catch (NotExistIconException e) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistIcon"));
                return;
            } catch (NotMoreIconException e) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notMoreIcon"));
                return;
            } catch (StateChangeException e) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.stateChange"));
                return;
            }

            playerPoints.take(player.getUniqueId(), playerPointsPrice);
        } else if (itemPrice != null) {
            //得到图标
            try {
                buyIconPageIconOptionPage.send();
                if (!guiService.hasItemStock(player.getUniqueId().toString(), iconMeta.getCurrencyItemStack(), itemPrice)) {
                    MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEnoughItemStockWhenBuyIcon"));
                    return;
                }
                guiService.buyIcon(player.getUniqueId().toString(), iconMeta.getUuid(), iconMeta);
                guiService.takeItemStock(player.getUniqueId().toString(), iconMeta.getCurrencyItemStack(), itemPrice);
                buyIconPageIconOptionPage.send();
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            } catch (DuplicateIconOwnerException e) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.duplicateIconOwnerWhenBuyIcon"));
                return;
            } catch (NotExistIconException e) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistIcon"));
                return;
            } catch (NotMoreIconException e) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notMoreIcon"));
                return;
            } catch (StateChangeException e) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.stateChange"));
                return;
            } catch (NotMoreItemStockException | NotExistItemStockException ignored) {
            }
        } else {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notSetPrice"));
            return;
        }

        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.buyIcon"));

    }


}
