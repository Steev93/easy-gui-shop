package pers.zhangyang.easyguishop.meta;

import org.jetbrains.annotations.NotNull;

public class ItemStockMeta {
    private String playerUuid;
    private String itemStack;
    private int amount;

    public ItemStockMeta() {
    }

    public ItemStockMeta(@NotNull String playerUuid, @NotNull String itemStack, int amount) {
        this.playerUuid = playerUuid;
        this.itemStack = itemStack;
        this.amount = amount;
    }

    @NotNull
    public String getPlayerUuid() {
        return playerUuid;
    }

    @NotNull
    public String getItemStack() {
        return itemStack;
    }

    public void setItemStack(String itemStack) {
        this.itemStack = itemStack;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
