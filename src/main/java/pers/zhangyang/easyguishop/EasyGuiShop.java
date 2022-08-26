package pers.zhangyang.easyguishop;

import org.bstats.bukkit.Metrics;
import pers.zhangyang.easyguishop.service.BaseService;
import pers.zhangyang.easyguishop.service.impl.BaseServiceImpl;
import pers.zhangyang.easylibrary.EasyPlugin;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

public class EasyGuiShop extends EasyPlugin {

    @Override
    public void onOpen() {
        //数据库更新 此时还没有初始化数据库
        BaseService pluginService = (BaseService) new TransactionInvocationHandler(new BaseServiceImpl()).getProxy();
        pluginService.transform2_0_0();
        pluginService.transform2_2_4();
        pluginService.transform2_8_1();

        // bStats统计信息
        new Metrics(EasyGuiShop.instance, 14803);
    }

    @Override
    public void onClose() {

    }


}
