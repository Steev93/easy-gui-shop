package pers.zhangyang.easyguishop.meta;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class GoodMeta {
    private String uuid;
    private long createTime;
    private String shopUuid;
    private String name;
    private String GoodItemStack;
    private String type;
    private boolean system;
    private int stock;
    private Integer limitTime;
    private String currencyItemStack;
    private Integer itemPrice;
    private Double vaultPrice;
    private Integer playerPointsPrice;

    private Integer limitFrequency;

    public Integer getLimitFrequency() {
        return limitFrequency;
    }

    public void setLimitFrequency(Integer limitFrequency) {
        this.limitFrequency = limitFrequency;
    }

    public GoodMeta() {
    }

    public GoodMeta(@NotNull String uuid, @NotNull String name, @NotNull String item_stack, @NotNull String type,
                    long createTime, boolean system, int stock, @NotNull String shop_uuid) {
        this.uuid = uuid;
        this.name = name;
        this.GoodItemStack = item_stack;
        this.type = type;
        this.createTime = createTime;
        this.system = system;
        this.stock = stock;
        this.shopUuid = shop_uuid;
    }

    @NotNull
    public String getUuid() {
        return uuid;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    public String getGoodItemStack() {
        return GoodItemStack;
    }

    public void setGoodItemStack(String goodItemStack) {
        GoodItemStack = goodItemStack;
    }

    @NotNull
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getCreateTime() {
        return createTime;
    }

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @NotNull
    public String getShopUuid() {
        return shopUuid;
    }

    @Nullable
    public Integer getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(@Nullable Integer limitTime) {
        this.limitTime = limitTime;
    }

    @Nullable
    public String getCurrencyItemStack() {
        return currencyItemStack;
    }

    public void setCurrencyItemStack(@Nullable String currencyItemStack) {
        this.currencyItemStack = currencyItemStack;
    }

    @Nullable
    public Integer getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(@Nullable Integer itemPrice) {
        this.itemPrice = itemPrice;
    }

    @Nullable
    public Double getVaultPrice() {
        return vaultPrice;
    }

    public void setVaultPrice(@Nullable Double vaultPrice) {
        this.vaultPrice = vaultPrice;
    }

    @Nullable
    public Integer getPlayerPointsPrice() {
        return playerPointsPrice;
    }

    public void setPlayerPointsPrice(@Nullable Integer playerPointsPrice) {
        this.playerPointsPrice = playerPointsPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoodMeta goodMeta = (GoodMeta) o;
        return createTime == goodMeta.createTime && system == goodMeta.system && stock == goodMeta.stock && Objects.equals(uuid, goodMeta.uuid) && Objects.equals(shopUuid, goodMeta.shopUuid) && Objects.equals(name, goodMeta.name) && Objects.equals(GoodItemStack, goodMeta.GoodItemStack) && Objects.equals(type, goodMeta.type) && Objects.equals(limitTime, goodMeta.limitTime) && Objects.equals(currencyItemStack, goodMeta.currencyItemStack) && Objects.equals(itemPrice, goodMeta.itemPrice) && Objects.equals(vaultPrice, goodMeta.vaultPrice) && Objects.equals(playerPointsPrice, goodMeta.playerPointsPrice) && Objects.equals(limitFrequency, goodMeta.limitFrequency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, createTime, shopUuid, name, GoodItemStack, type, system, stock, limitTime, currencyItemStack, itemPrice, vaultPrice, playerPointsPrice, limitFrequency);
    }
}
