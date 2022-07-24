package pers.zhangyang.easyguishop.listener.shopcommentpage;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pers.zhangyang.easyguishop.domain.ShopCommentPage;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.FiniteInputListenerBase;
import pers.zhangyang.easylibrary.util.MessageUtil;

public class PlayerInputAfterClickShopCommentPageSearchByCommenterName extends FiniteInputListenerBase {

    private final ShopCommentPage allShopPageShopOptionPageShopCommentPage;

    public PlayerInputAfterClickShopCommentPageSearchByCommenterName(Player player, OfflinePlayer owner, ShopCommentPage allShopPage) {
        super(player, owner, allShopPage, 1);
        this.allShopPageShopOptionPageShopCommentPage = allShopPage;
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToSearchShopCommenter"));
    }


    @Override
    public void run() {

        allShopPageShopOptionPageShopCommentPage.searchByCommenterName(messages[0]);
    }
}
