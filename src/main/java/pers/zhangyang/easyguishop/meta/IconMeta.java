package pers.zhangyang.easyguishop.meta;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class IconMeta {
    private String uuid;
    private long createTime;
    private String iconItemStack;
    private String name;
    private int stock;
    private String currencyItemStack;
    private boolean system;

    private Integer limitTime;
    private Integer itemPrice;
    private Double vaultPrice;
    private Integer playerPointsPrice;

    public IconMeta() {
    }

    public IconMeta(@NotNull String uuid, @NotNull String name, long createTime, int stock, @NotNull String iconItemStack, boolean system) {
        this.uuid = uuid;
        this.name = name;
        this.createTime = createTime;
        this.system = system;
        this.stock = stock;
        this.iconItemStack = iconItemStack;
    }

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    @Nullable
    public String getCurrencyItemStack() {
        return currencyItemStack;
    }

    public void setCurrencyItemStack(@Nullable String currencyItemStack) {
        this.currencyItemStack = currencyItemStack;
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

    public long getCreateTime() {
        return createTime;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @NotNull
    public String getIconItemStack() {
        return iconItemStack;
    }

    @Nullable
    public Integer getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(@Nullable Integer limitTime) {
        this.limitTime = limitTime;
    }

    @Nullable
    public Integer getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(@Nullable Integer itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Double getVaultPrice() {
        return vaultPrice;
    }

    public void setVaultPrice(@Nullable Double vaultPrice) {
        this.vaultPrice = vaultPrice;
    }

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
        IconMeta iconMeta = (IconMeta) o;
        return createTime == iconMeta.createTime && stock == iconMeta.stock && system == iconMeta.system && Objects.equals(uuid, iconMeta.uuid) && Objects.equals(name, iconMeta.name) && Objects.equals(iconItemStack, iconMeta.iconItemStack) && Objects.equals(currencyItemStack, iconMeta.currencyItemStack) && Objects.equals(limitTime, iconMeta.limitTime) && Objects.equals(itemPrice, iconMeta.itemPrice) && Objects.equals(vaultPrice, iconMeta.vaultPrice) && Objects.equals(playerPointsPrice, iconMeta.playerPointsPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, createTime, stock, iconItemStack, currencyItemStack, system, limitTime, itemPrice, vaultPrice, playerPointsPrice);
    }
}
