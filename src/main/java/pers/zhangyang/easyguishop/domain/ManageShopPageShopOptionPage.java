package pers.zhangyang.easyguishop.domain;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.meta.ShopMeta;
import pers.zhangyang.easyguishop.service.GuiService;
import pers.zhangyang.easyguishop.service.impl.GuiServiceImpl;
import pers.zhangyang.easyguishop.yaml.GuiYaml;
import pers.zhangyang.easylibrary.base.BackAble;
import pers.zhangyang.easylibrary.base.GuiPage;
import pers.zhangyang.easylibrary.base.SingleGuiPageBase;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ManageShopPageShopOptionPage extends SingleGuiPageBase implements BackAble {
    private ShopMeta shopMeta;

    public ManageShopPageShopOptionPage(GuiPage previousHolder, Player player, ShopMeta shopMeta) {
        super(GuiYaml.INSTANCE.getString("gui.title.manageShopPageShopOptionPage"), player, previousHolder, previousHolder.getOwner());

        this.shopMeta = shopMeta;

    }

    //根据Shop的情况来设置Button
    public void send() {
        refresh();
    }

    @Override
    public void refresh() {
        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
        this.shopMeta = guiService.getShop(shopMeta.getUuid());
        if (this.shopMeta == null) {
            backPage.send();
            return;
        }
        this.inventory.clear();
        ItemStack setLocation = GuiYaml.INSTANCE.getButton("gui.button.manageShopPageShopOptionPage.setShopLocation");
        inventory.setItem(22, setLocation);
        Gson gson = new Gson();
        Type stringListType = new TypeToken<ArrayList<String>>() {
        }.getType();
        List<String> stringList = gson.fromJson(shopMeta.getDescription(), stringListType);

        if (stringList != null && !stringList.isEmpty()) {
            ItemStack setShopDescription = GuiYaml.INSTANCE.getButton("gui.button.manageShopPageShopOptionPage.updateShopDescription");
            inventory.setItem(30, setShopDescription);
        }

        ItemStack setName = GuiYaml.INSTANCE.getButton("gui.button.manageShopPageShopOptionPage.setShopName");
        inventory.setItem(21, setName);

        ItemStack manageShop = GuiYaml.INSTANCE.getButton("gui.button.manageShopPageShopOptionPage.manageGoodPage");
        inventory.setItem(31, manageShop);

        ItemStack searchOwner = GuiYaml.INSTANCE.getButton("gui.button.manageShopPageShopOptionPage.manageIconPage");
        inventory.setItem(40, searchOwner);
        if (shopMeta.getLocation() != null) {
            ItemStack resetLocation = GuiYaml.INSTANCE.getButton("gui.button.manageShopPageShopOptionPage.resetShopLocation");
            inventory.setItem(39, resetLocation);
        }

        if (stringList != null && !stringList.isEmpty()) {
            ItemStack resetShopDescription = GuiYaml.INSTANCE.getButton("gui.button.manageShopPageShopOptionPage.resetShopDescription");
            inventory.setItem(41, resetShopDescription);
        }


        ItemStack set = GuiYaml.INSTANCE.getButton("gui.button.manageShopPageShopOptionPage.setShopDescription");
        inventory.setItem(4, set);
        ItemStack addShopDescription = GuiYaml.INSTANCE.getButton("gui.button.manageShopPageShopOptionPage.addShopDescription");
        inventory.setItem(13, addShopDescription);


        if (stringList != null && !stringList.isEmpty()) {
            ItemStack removeShopDescription = GuiYaml.INSTANCE.getButton("gui.button.manageShopPageShopOptionPage.removeShopDescription");
            inventory.setItem(32, removeShopDescription);
        }
        ItemStack lookComment = GuiYaml.INSTANCE.getButton("gui.button.manageShopPageShopOptionPage.shopCommentPage");
        inventory.setItem(23, lookComment);
        ItemStack back = GuiYaml.INSTANCE.getButton("gui.button.manageShopPageShopOptionPage.back");
        inventory.setItem(49, back);
        viewer.openInventory(inventory);
    }

    @NotNull
    public ShopMeta getShopMeta() {
        return shopMeta;
    }


    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public InventoryHolder getPreviousHolder() {
        return backPage;
    }

    @Override
    public void back() {
        backPage.refresh();
    }
}

