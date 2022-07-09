package pers.zhangyang.easyguishop;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easyguishop.completer.*;
import pers.zhangyang.easyguishop.domain.*;
import pers.zhangyang.easyguishop.executor.*;
import pers.zhangyang.easyguishop.listener.allgoodpage.*;
import pers.zhangyang.easyguishop.listener.allgoodpagegoodoptionpage.PlayerClickAllGoodPageGoodOptionPage;
import pers.zhangyang.easyguishop.listener.allgoodpagegoodoptionpage.PlayerClickAllGoodPageGoodOptionPageBack;
import pers.zhangyang.easyguishop.listener.allgoodpagegoodoptionpage.PlayerClickAllGoodPageGoodOptionPageTradeGood;
import pers.zhangyang.easyguishop.listener.allshoppage.*;
import pers.zhangyang.easyguishop.listener.allshoppageshopoptionpage.*;
import pers.zhangyang.easyguishop.listener.buyiconpage.*;
import pers.zhangyang.easyguishop.listener.buyiconpageiconoptionpage.PlayerClickBuyIconPageIconOptionPage;
import pers.zhangyang.easyguishop.listener.buyiconpageiconoptionpage.PlayerClickBuyIconPageIconOptionPageBack;
import pers.zhangyang.easyguishop.listener.buyiconpageiconoptionpage.PlayerClickBuyIconPageIconOptionPageBuyIcon;
import pers.zhangyang.easyguishop.listener.collectedshoppage.*;
import pers.zhangyang.easyguishop.listener.collectedshoppageshopoptionpage.*;
import pers.zhangyang.easyguishop.listener.managecommentpage.*;
import pers.zhangyang.easyguishop.listener.managegoodpage.*;
import pers.zhangyang.easyguishop.listener.managegoodpagegoodoptionpage.*;
import pers.zhangyang.easyguishop.listener.manageiconpage.*;
import pers.zhangyang.easyguishop.listener.manageiconpageiconoptionpage.PlayerClickManageIconPageIconOptionPage;
import pers.zhangyang.easyguishop.listener.manageiconpageiconoptionpage.PlayerClickManageIconPageIconOptionPageBack;
import pers.zhangyang.easyguishop.listener.manageiconpageiconoptionpage.PlayerClickManageIconPageIconOptionPageUseShopIcon;
import pers.zhangyang.easyguishop.listener.manageitemstockpage.*;
import pers.zhangyang.easyguishop.listener.manageitemstockpageitemstockoptionpage.*;
import pers.zhangyang.easyguishop.listener.manageshoppage.*;
import pers.zhangyang.easyguishop.listener.manageshoppageshopoptionpage.*;
import pers.zhangyang.easyguishop.listener.managetraderecordpage.*;
import pers.zhangyang.easyguishop.listener.managetraderecordpagetraderecordoptionpage.PlayerClickManageTradeRecordPageTradeRecordOptionPage;
import pers.zhangyang.easyguishop.listener.managetraderecordpagetraderecordoptionpage.PlayerClickManageTradeRecordPageTradeRecordOptionPageBack;
import pers.zhangyang.easyguishop.listener.shopcommentpage.*;
import pers.zhangyang.easyguishop.other.bstats.Metrics;
import pers.zhangyang.easyguishop.runnable.NotifyVersionRunnable;
import pers.zhangyang.easyguishop.service.BaseService;
import pers.zhangyang.easyguishop.service.impl.BaseServiceImpl;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.CompleterYaml;
import pers.zhangyang.easyguishop.yaml.GuiYaml;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easyguishop.yaml.SettingYaml;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EasyGuiShop extends JavaPlugin {
    public static EasyGuiShop instance;

    @Override
    public void onEnable() {


        instance = this;
        // 初始化yml,出错直接关闭插件
        try {
            SettingYaml.INSTANCE.init();
            CompleterYaml.INSTANCE.init();
            MessageYaml.INSTANCE.init();
            GuiYaml.INSTANCE.init();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            setEnabled(false);
            return;
        }

        //数据库
        BaseService pluginService = (BaseService) new TransactionInvocationHandler(BaseServiceImpl.INSTANCE).getProxy();

        try {
            pluginService.transform2_0_0();
            pluginService.initDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            setEnabled(false);
            return;
        }

        //注册事件
        registerListener();
        // 到这里插件已经成功可以使用了,提示插件标志
        MessageUtil.sendMessageTo(Bukkit.getConsoleSender(), MessageYaml.INSTANCE.getStringList("message.chat.enablePlugin"));
        // 后台更新提示
        new NotifyVersionRunnable(Bukkit.getConsoleSender()).runTaskAsynchronously(this);
        // bStats统计信息
        new Metrics(EasyGuiShop.instance, 14803);
    }

    @Override
    public void onDisable() {

        for (Player p : Bukkit.getOnlinePlayers()) {
            Inventory inventory = p.getOpenInventory().getTopInventory();
            InventoryHolder holder = inventory.getHolder();
            if ((holder instanceof AllGoodPage) || (holder instanceof AllGoodPageGoodOptionPage)
                    || (holder instanceof AllShopPage) || (holder instanceof AllShopPageShopOptionPage)
                    || (holder instanceof BuyIconPage) || (holder instanceof BuyIconPageIconOptionPage)
                    || (holder instanceof CollectedShopPage) || (holder instanceof CollectedShopPageShopOptionPage)
                    || (holder instanceof ManageCommentPage) || (holder instanceof ManageGoodPage)
                    || (holder instanceof ManageGoodPageGoodOptionPage) || (holder instanceof ManageIconPage)
                    || (holder instanceof ManageIconPageIconOptionPage) || (holder instanceof ManageItemStockPage)
                    || (holder instanceof ManageItemStockPageItemStockOptionPage) || (holder instanceof ManageShopPage)
                    || (holder instanceof ManageShopPageShopOptionPage) || (holder instanceof ManageTradeRecordPage)
                    || (holder instanceof ManageTradeRecordPageTradeRecordOptionPage) || (holder instanceof ShopCommentPage)) {
                p.closeInventory();
            }

        }
        // 关闭提示标志
        MessageUtil.sendMessageTo(Bukkit.getConsoleSender(), MessageYaml.INSTANCE.getStringList("message.chat.disablePlugin"));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "reloadplugin":
                new ReloadPlugin(sender, false, args).process();
                break;
            case "correctyaml":
                new CorrectYamlExecutor(sender, false, args).process();
                break;
            case "help":
                new HelpExecutor(sender, false, args).process();
                break;
            case "opengui":
                new OpenGuiExecutor(sender, true, args).process();
                break;
            case "plusshoppopularity":
                new PlusShopPopularityExecutor(sender, false, args).process();
                break;
            case "subtractshoppopularity":
                new SubtractShopPopularityExecutor(sender, false, args).process();
                break;
            case "createicon":
                new CreateIconExecutor(sender, true, args).process();
                break;
            case "deleteicon":
                new DeleteIconExecutor(sender, false, args).process();
                break;
            case "seticonvaultprice":
                new SetIconVaultPriceExecutor(sender, false, args).process();
                break;
            case "seticonplayerpointsprice":
                new SetIconPlayerPointsPriceExecutor(sender, false, args).process();
                break;
            case "seticonitemprice":
                new SetIconItemPriceExecutor(sender, true, args).process();
                break;
            case "seticonlimittime":
                new SetIconLimitTimeExecutor(sender, false, args).process();
                break;
            case "seticonname":
                new SetIconNameExecutor(sender, false, args).process();
                break;
            case "seticonstock":
                new SetIconStockExecutor(sender, false, args).process();
                break;
            case "seticonsystem":
                new SetIconSystemExecutor(sender, false, args).process();
                break;
            case "setshopsystem":
                new SetShopSystemExecutor(sender, false, args).process();
                break;
            case "setgoodsystem":
                new SetGoodSystemExecutor(sender, false, args).process();
                break;
            case "reseticonlimittime":
                new ResetIconLimitTimeExecutor(sender, false, args).process();
                break;
            case "correctdatabase":
                new CorrectDatabaseExecutor(sender, false, args).process();
                break;
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> list = null;
        if (args.length == 1) {
            list = CompleterYaml.INSTANCE.getStringList("completer.easyGuiShop");
            if (list != null) {
                String ll = args[0].toLowerCase();
                list.removeIf(k -> !k.toLowerCase().startsWith(ll));
            } else {
                list = new ArrayList<>();
            }
        }
        if (args[0].equalsIgnoreCase("createIcon")) {
            return new CreateIconCompleter(sender, args).process();
        }
        if (args[0].equalsIgnoreCase("deleteIcon")) {
            return new DeleteIconCompleter(sender, args).process();
        }
        if (args[0].equalsIgnoreCase("plusShopPopularity")) {
            return new PlusPopularityCompleter(sender, args).process();
        }
        if (args[0].equalsIgnoreCase("SetIconVaultPrice")) {
            return new SetIconVaultPriceCompleter(sender, args).process();
        }
        if (args[0].equalsIgnoreCase("setIconPlayerPointsPrice")) {
            return new SetIconPlayerPointsPriceCompleter(sender, args).process();
        }
        if (args[0].equalsIgnoreCase("setIconItemPrice")) {
            return new SetIconItemPriceCompleter(sender, args).process();
        }
        if (args[0].equalsIgnoreCase("subtractShopPopularity")) {
            return new SubtractPopularityCompleter(sender, args).process();
        }
        if (args[0].equalsIgnoreCase("setIconName")) {
            return new SetIconNameCompleter(sender, args).process();
        }
        if (args[0].equalsIgnoreCase("setIconStock")) {
            return new SetIconStockCompleter(sender, args).process();
        }
        if (args[0].equalsIgnoreCase("setIconSystem")) {
            return new SetIconSystemCompleter(sender, args).process();
        }
        if (args[0].equalsIgnoreCase("setGoodSystem")) {
            return new SetGoodSystemCompleter(sender, args).process();
        }
        if (args[0].equalsIgnoreCase("setShopSystem")) {
            return new SetShopSystemCompleter(sender, args).process();
        }
        if (args[0].equalsIgnoreCase("setIconLimitTime")) {
            return new SetIconLimitTimeCompleter(sender, args).process();
        }
        if (args[0].equalsIgnoreCase("resetIconLimitTime")) {
            return new ResetIconLimitTimeCompleter(sender, args).process();
        }
        return list;
    }

    private void registerListener() {
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageIconPageIconOptionPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageIconPageIconOptionPageBack(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageIconPageIconOptionPageUseShopIcon(), this);

        Bukkit.getPluginManager().registerEvents(new PlayerClickManageTradeRecordPageTradeRecordOptionPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageTradeRecordPageTradeRecordOptionPageBack(), this);

        Bukkit.getPluginManager().registerEvents(new PlayerClickManageTradeRecordPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageTradeRecordPageNext(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageTradeRecordPagePrevious(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageTradeRecordPageBack(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageTradeRecordPageManageTradeRecordPageTradeRecordOptionPage(), this);

        Bukkit.getPluginManager().registerEvents(new PlayerClickAllGoodPageGoodOptionPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickAllGoodPageGoodOptionPageBack(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickAllGoodPageGoodOptionPageTradeGood(), this);

        Bukkit.getPluginManager().registerEvents(new PlayerClickManageItemStockPageItemStockOptionPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageItemStockPageItemStockOptionPageBack(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageItemStockPageItemStockOptionPageDeleteItemStock(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageItemStockPageItemStockOptionPageDepositItemStock(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageItemStockPageItemStockOptionPageTakeItemStock(), this);


        Bukkit.getPluginManager().registerEvents(new PlayerClickManageItemStockPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageItemStockPageNext(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageItemStockPagePrevious(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageItemStockPageBack(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageItemStockCreateItemStock(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageItemStockPageManageItemStockPageItemStockOptionPage(), this);

        Bukkit.getPluginManager().registerEvents(new PlayerClickManageGoodPageGoodOptionPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageGoodPageGoodOptionPageResetGoodPrice(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageGoodPageGoodOptionPageResetGoodLimitTime(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageGoodPageGoodOptionPageDepositGood(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageGoodPageGoodOptionPageTakeGood(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageGoodPageGoodOptionPageSetGoodName(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageGoodPageGoodOptionPageBack(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageGoodPageGoodOptionPageSetGoodVaultPrice(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageGoodPageGoodOptionPageSetGoodPlayerPointsPrice(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageGoodPageGoodOptionPageSetGoodItemPrice(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageGoodPageGoodOptionPageSetGoodLimitTime(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageGoodPageGoodOptionPageToggleGoodType(), this);

        Bukkit.getPluginManager().registerEvents(new PlayerClickAllGoodPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickAllGoodPageBack(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickAllGoodPageSearchByGoodName(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickAllGoodPageAllGoodPageGoodOptionPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickAllGoodPageNext(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickAllGoodPagePrevious(), this);


        Bukkit.getPluginManager().registerEvents(new PlayerClickManageGoodPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageGoodPageBack(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageShopPageShopOptionPageManageIconPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageGoodPageSearchByGoodName(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageGoodPageCreateGood(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageGoodPageDeleteGood(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageGoodPageManageGoodPageGoodOptionPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageGoodPageNext(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageGoodPagePrevious(), this);

        Bukkit.getPluginManager().registerEvents(new PlayerClickManageIconPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageIconPageBack(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageIconPageSearchByIconName(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageIconPageManageIconPageIconOptionPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageIconPageResetShopIcon(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageIconPageNext(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageIconPagePrevious(), this);

        Bukkit.getPluginManager().registerEvents(new PlayerClickBuyIconPageIconOptionPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickBuyIconPageIconOptionPageBack(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickBuyIconPageIconOptionPageBuyIcon(), this);

        Bukkit.getPluginManager().registerEvents(new PlayerClickBuyIconPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickBuyIconPageBack(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickBuyIconPageSearchByIconName(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickBuyIconPageBuyIconPageIconOptionPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickBuyIconPageNext(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickBuyIconPagePrevious(), this);

        Bukkit.getPluginManager().registerEvents(new PlayerClickManageCommentPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageCommentPageBack(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageCommentPageDeleteShopComment(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageCommentPageNext(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageCommentPagePrevious(), this);

        Bukkit.getPluginManager().registerEvents(new PlayerClickAllShopPageShopOptionPageCommentShop(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickShopCommentPageSearchByCommenter(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickShopCommentPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickShopCommentPageBack(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickShopCommentPageNext(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClicShopCommentPagePrevious(), this);

        Bukkit.getPluginManager().registerEvents(new PlayerClickAllShopPageManageItemStockPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickAllShopPageNext(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickAllShopPagePrevious(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickAllShopPageManageShopPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickAllShopPageSearchByShopName(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickAllShopPageSearchByShopOwnerName(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickAllShopPageAllShopPageShopOptionPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickAllShopPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickAllShopPageCollectedShopPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickAllShopPageManageCommentPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickAllShopPageManageTradeRecordPage(), this);

        Bukkit.getPluginManager().registerEvents(new PlayerClickAllShopPageShopOptionPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickAllShopPageShopOptionPageCollectShop(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickAllShopPageShopOptionPageCancelCollectShop(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickAllShopPageShopOptionPageBack(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickAllShopPageShopOptionPageGoLocation(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickAllShopPageShopOptionPageAllGoodPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickAllShopPageShopOptionPageShopCommentPage(), this);


        Bukkit.getPluginManager().registerEvents(new PlayerClickManageShopPageCreateShop(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageShopPageNext(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageShopPagePrevious(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageShopPageManageShopPageShopOptionPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageShopPageBack(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageShopPageSearchByShopName(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageShopPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageShopPageDeleteShop(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageShopPageBuyIconPage(), this);

        Bukkit.getPluginManager().registerEvents(new PlayerClickManageShopPageShopOptionPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageShopPageShopOptionPageSetShopDescription(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageShopPageShopOptionPageSetShopName(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageShopPageShopOptionPageBack(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageShopPageShopOptionPageSetShopLocation(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageShopPageShopOptionPageResetShopLocation(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageShopPageShopOptionPageAddShopDescription(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageShopPageShopOptionPageRemoveShopDescription(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageShopPageShopOptionPageResetShopDescription(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageShopPageShopOptionPageUpdateShopDescription(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageShopPageShopOptionPageManageGoodPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickManageShopPageShopOptionPageShopCommentPage(), this);

        Bukkit.getPluginManager().registerEvents(new PlayerClickAllShopPageCollectedShopPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickCollectedShopPageNext(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickCollectedShopPagePrevious(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickCollectedShopPageSearchByShopName(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickCollectedShopPageSearchByShopOwnerName(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickCollectedShopPageCollectedShopPageShopOptionPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickCollectedShopPageBack(), this);

        Bukkit.getPluginManager().registerEvents(new PlayerClickCollectedShopPageShopOptionPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickCollectedShopPageShopOptionPageBack(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickCollectedShopPageShopOptionPageCancelCollectShop(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickCollectedShopPageShopOptionPageGoLocation(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickCollectedShopPageShopOptionPageAllGoodPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickCollectedShopPageShopOptionPageCommentShop(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickCollectedShopPageShopOptionPageShopCommentPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickCollectedShopPageShopOptionPageCollectShop(), this);

    }

}
