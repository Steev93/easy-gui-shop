package pers.zhangyang.easyguishop.listener.collectedshoppageshopoptionpage;

import com.google.gson.Gson;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pers.zhangyang.easyguishop.domain.CollectedShopPageShopOptionPage;
import pers.zhangyang.easyguishop.exception.DuplicateShopCommenterException;
import pers.zhangyang.easyguishop.meta.ShopCommentMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.InfiniteInputListenerBase;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;
import pers.zhangyang.easylibrary.util.UuidUtil;

public class PlayerInputAfterClickCollectedShopPageShopOptionPageCommentShop extends InfiniteInputListenerBase {

    private final CollectedShopPageShopOptionPage collectedShopPageShopOptionPage;

    public PlayerInputAfterClickCollectedShopPageShopOptionPageCommentShop(Player player, OfflinePlayer owner, CollectedShopPageShopOptionPage allShopPage) {
        super(player, owner, allShopPage);
        this.collectedShopPageShopOptionPage = allShopPage;
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.howToCommentShopInCollectedShopPageShopOptionPage"));
    }

    @Override
    public void run() {
        collectedShopPageShopOptionPage.send();
        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();

        ShopCommentMeta shopCommentMeta = new ShopCommentMeta(UuidUtil.getUUID(), owner.getUniqueId().toString(),
                collectedShopPageShopOptionPage.getShopMeta().getUuid(), new Gson().toJson(messageList), System.currentTimeMillis());
        try {
            guiService.createShopComment(shopCommentMeta);
            collectedShopPageShopOptionPage.send();
        } catch (DuplicateShopCommenterException ignored) {
        }
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.commentShopInCollectedShopPageShopOptionPage"));

    }

}
