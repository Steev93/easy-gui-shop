package pers.zhangyang.easyguishop.listener.allgoodpage;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.domain.AllGoodPage;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.FiniteInputListenerBase;
import pers.zhangyang.easylibrary.util.MessageUtil;

public class PlayerInputAfterClickAllGoodPageSearchByGoodName extends FiniteInputListenerBase {

    private final AllGoodPage allGoodPage;

    public PlayerInputAfterClickAllGoodPageSearchByGoodName(Player player, OfflinePlayer owner, @NotNull AllGoodPage allGoodPage) {
        super(player, owner, allGoodPage, 1);
        this.allGoodPage = allGoodPage;
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToSearchByGoodNameInAllGoodPage"));
    }

    @Override
    public void run() {
        allGoodPage.searchByGooName(messages[0]);
    }
}
