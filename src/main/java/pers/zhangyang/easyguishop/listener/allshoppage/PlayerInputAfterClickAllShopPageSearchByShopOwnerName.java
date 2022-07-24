package pers.zhangyang.easyguishop.listener.allshoppage;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pers.zhangyang.easyguishop.domain.AllShopPage;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.FiniteInputListenerBase;
import pers.zhangyang.easylibrary.util.MessageUtil;

public class PlayerInputAfterClickAllShopPageSearchByShopOwnerName extends FiniteInputListenerBase {

    private final AllShopPage allShopPage;

    public PlayerInputAfterClickAllShopPageSearchByShopOwnerName(Player player, OfflinePlayer owner, AllShopPage allShopPage) {
        super(player, owner, allShopPage, 1);
        this.allShopPage = allShopPage;
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToSearchByShopOwnerNameInAllShopPage"));
    }

    @Override
    public void run() {
        allShopPage.searchByShopOwnerName(messages[0]);
    }
}
