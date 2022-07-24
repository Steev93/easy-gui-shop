package pers.zhangyang.easyguishop.listener.manageiconpage;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pers.zhangyang.easyguishop.domain.ManageIconPage;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.FiniteInputListenerBase;
import pers.zhangyang.easylibrary.util.MessageUtil;

public class PlayerInputAfterClickManageIconPageSearchByIconName extends FiniteInputListenerBase {

    private final ManageIconPage manageIconPage;

    public PlayerInputAfterClickManageIconPageSearchByIconName(Player player, OfflinePlayer owner, ManageIconPage manageIconPage) {
        super(player, owner, manageIconPage, 1);
        this.manageIconPage = manageIconPage;
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToSearchByIconNameInManageIconPage"));
    }


    @Override
    public void run() {

        manageIconPage.searchByIconName(messages[0]);
    }
}
