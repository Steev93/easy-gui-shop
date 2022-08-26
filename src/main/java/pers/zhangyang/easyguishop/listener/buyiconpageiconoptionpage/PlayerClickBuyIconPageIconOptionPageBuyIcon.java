package pers.zhangyang.easyguishop.listener.buyiconpageiconoptionpage;

import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.zhangyang.easyguishop.domain.BuyIconPageIconOptionPage;
import pers.zhangyang.easyguishop.exception.*;
import pers.zhangyang.easyguishop.meta.IconMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;
import pers.zhangyang.easylibrary.other.playerpoints.PlayerPoints;
import pers.zhangyang.easylibrary.other.vault.Vault;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

@EventListener
public class PlayerClickBuyIconPageIconOptionPageBuyIcon implements Listener {

    @GuiDiscreteButtonHandler(guiPage = BuyIconPageIconOptionPage.class, slot = {40},closeGui = false,refreshGui = true)
    public void onPlayerClickAllShopNextPage(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        BuyIconPageIconOptionPage buyIconPageIconOptionPage = (BuyIconPageIconOptionPage) holder;
        Player player = (Player) event.getWhoClicked();
        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
        IconMeta iconMeta = buyIconPageIconOptionPage.getIconMeta();


        //检查是否限购
        Integer limitTime = iconMeta.getLimitTime();
        if (limitTime != null && limitTime * 1000 + iconMeta.getCreateTime() < System.currentTimeMillis()) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.exceedLimitTimeWhenBuyIcon"));
            return;
        }

        //检查货币
        Double vaultPrice = iconMeta.getVaultPrice();
        Integer itemPrice = iconMeta.getItemPrice();
        Integer playerPointsPrice = iconMeta.getPlayerPointsPrice();
        Economy vault = Vault.hook();
        PlayerPointsAPI playerPoints = PlayerPoints.hook();
        if (vaultPrice == null && playerPointsPrice == null && itemPrice == null) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notSetPriceWhenBuyIcon"));
            return;
        }
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
                guiService.buyIcon(player.getUniqueId().toString(), iconMeta.getUuid(), iconMeta);
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
            } finally {
                buyIconPageIconOptionPage.send();
            }

            vault.withdrawPlayer(player, vaultPrice);
        }
        if (playerPointsPrice != null) {
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
                guiService.buyIcon(player.getUniqueId().toString(), iconMeta.getUuid(), iconMeta);
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
            } finally {
                buyIconPageIconOptionPage.send();
            }

            playerPoints.take(player.getUniqueId(), playerPointsPrice);
        }
        if (itemPrice != null) {
            //得到图标
            try {
                guiService.buyIconItem(player.getUniqueId().toString(), iconMeta.getUuid(), iconMeta);
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
            } catch (NotEnoughItemStockException e) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEnoughItemStockWhenBuyIcon"));
                return;
            }
        }


        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.buyIcon"));

    }


}
