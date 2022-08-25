package pers.zhangyang.easyguishop.listener.manageshoppageshopoptionpage;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pers.zhangyang.easyguishop.domain.ManageShopPageShopOptionPage;
import pers.zhangyang.easyguishop.exception.NotExistLineException;
import pers.zhangyang.easyguishop.exception.NotExistShopException;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easyguishop.yaml.SettingYaml;
import pers.zhangyang.easylibrary.base.FiniteInputListenerBase;
import pers.zhangyang.easylibrary.other.vault.Vault;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

public class PlayerInputAfterClickManageShopPageShopOptionPageBuyShopPopularity extends FiniteInputListenerBase {

    private final ShopMeta shopMeta;
    private final ManageShopPageShopOptionPage manageShopPageShopOptionPage;

    public PlayerInputAfterClickManageShopPageShopOptionPageBuyShopPopularity(Player player, OfflinePlayer owner, ShopMeta shopMeta, ManageShopPageShopOptionPage manageShopPage) {
        super(player, owner, manageShopPage, 1);
        this.manageShopPageShopOptionPage = manageShopPage;
        this.shopMeta = shopMeta;
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToBuyShopPopularity"));
    }


    @Override
    public void run() {

        int lineIndex;
        try {
            lineIndex= Integer.parseInt(messages[0]);
        }catch (NumberFormatException e){
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.invalidNumber"));
            return;
        }
        if (lineIndex<0){
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.invalidNumber"));
            return;
        }
        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();


        if (guiService.getShop(shopMeta.getUuid())==null){
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistShop"));
            return;
        }



        Double perCost= SettingYaml.INSTANCE.getNonnegativeDouble("setting.perPopularityCost");

        if (perCost!=null){

            if (Vault.hook()==null){
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notHookVault"));
                return;
            }
            if (Vault.hook().getBalance(owner)<perCost*lineIndex){
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEnoughVaultWhenBuyShopPopularity"));
                return;
            }
            Vault.hook().withdrawPlayer(owner,perCost*lineIndex);

        }



        try {
            guiService.plusShopPopularity(shopMeta.getUuid(), lineIndex);
        }  catch (NotExistShopException e) {
            e.printStackTrace();
        }
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.buyShopPopularity"));

    }
}
