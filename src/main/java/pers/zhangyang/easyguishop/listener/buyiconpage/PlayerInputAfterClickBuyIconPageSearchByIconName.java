package pers.zhangyang.easyguishop.listener.buyiconpage;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pers.zhangyang.easyguishop.domain.BuyIconPage;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.FiniteInputListenerBase;
import pers.zhangyang.easylibrary.util.MessageUtil;

public class PlayerInputAfterClickBuyIconPageSearchByIconName extends FiniteInputListenerBase {

    private final BuyIconPage allShopPage;

    public PlayerInputAfterClickBuyIconPageSearchByIconName(Player player, OfflinePlayer owner, BuyIconPage allShopPage) {
        super(player, owner, allShopPage, 1);
        this.allShopPage = allShopPage;
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToSearchByIconNameInBuyIconPage"));
    }

    @Override
    public void run() {

        allShopPage.searchByIconName(messages[0]);
    }
}
