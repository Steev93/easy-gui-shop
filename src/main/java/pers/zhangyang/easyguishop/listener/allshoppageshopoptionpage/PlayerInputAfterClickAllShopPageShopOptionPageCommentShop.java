package pers.zhangyang.easyguishop.listener.allshoppageshopoptionpage;

import com.google.gson.Gson;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pers.zhangyang.easyguishop.domain.AllShopPageShopOptionPage;
import pers.zhangyang.easyguishop.exception.DuplicateShopCommenterException;
import pers.zhangyang.easyguishop.meta.ShopCommentMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.InfiniteInputListenerBase;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;
import pers.zhangyang.easylibrary.util.UuidUtil;

public class PlayerInputAfterClickAllShopPageShopOptionPageCommentShop extends InfiniteInputListenerBase {

    private final AllShopPageShopOptionPage allShopPageShopOptionPage;

    public PlayerInputAfterClickAllShopPageShopOptionPageCommentShop(Player player, OfflinePlayer owner, AllShopPageShopOptionPage allShopPage) {
        super(player, owner, allShopPage);
        this.allShopPageShopOptionPage = allShopPage;
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.hotToCommentShopInAllShopPageShopOptionPage"));
    }

    @Override
    public void run() {
        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();

        ShopCommentMeta shopCommentMeta = new ShopCommentMeta(UuidUtil.getUUID(), owner.getUniqueId().toString(),
                allShopPageShopOptionPage.getShopMeta().getUuid(), new Gson().toJson(messageList), System.currentTimeMillis());
        try {
            guiService.createShopComment(shopCommentMeta);
        } catch (DuplicateShopCommenterException ignored) {
        }
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.commentShopInAllShopPageShopOptionPage"));

    }
}
