package pers.zhangyang.easyguishop.listener.manageshoppage;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pers.zhangyang.easyguishop.domain.ManageShopPage;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.FiniteInputListenerBase;
import pers.zhangyang.easylibrary.util.MessageUtil;

public class PlayerInputAfterClickManageShopPageSearchByShopName extends FiniteInputListenerBase {

    private final ManageShopPage manageShopPage;

    public PlayerInputAfterClickManageShopPageSearchByShopName(Player player, OfflinePlayer owner, ManageShopPage manageShopPage) {
        super(player, owner, manageShopPage, 1);
        this.manageShopPage = manageShopPage;
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToSearchByShopNameInManageShopPage"));
    }

    @Override
    public void run() {

        manageShopPage.searchByShopName(messages[0]);
    }
}
