package pers.zhangyang.easyguishop.meta;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TradeRecordMeta {
    private String uuid;
    private final String customerUuid;
    private final String merchantUuid;
    private  String goodItemStack;
    private int tradeAmount;
    private boolean goodSystem;
    private long tradeTime;
    private double tradeTaxRate;
    private String goodType;
    private String goodCurrencyItemStack;
    private Double goodVaultPrice;
    private Integer goodPlayerPointsPrice;
    private Integer goodItemPrice;

    public TradeRecordMeta(@NotNull String uuid, @NotNull String customerUuid, @NotNull String merchantUuid,
                           @NotNull String goodItemStack, int tradeAmount, boolean goodSystem, long tradeTime,
                           String goodType,double tradeTaxRate) {
        this.tradeTaxRate = tradeTaxRate;
        this.uuid = uuid;
        this.customerUuid = customerUuid;
        this.merchantUuid = merchantUuid;
        this.goodItemStack = goodItemStack;
        this.tradeAmount = tradeAmount;
        this.goodSystem = goodSystem;
        this.tradeTime = tradeTime;
        this.goodType = goodType;
    }

    public double getTradeTaxRate() {
        return tradeTaxRate;
    }

    public void setGoodItemStack(String goodItemStack) {
        this.goodItemStack = goodItemStack;
    }

    @NotNull
    public String getUuid() {
        return uuid;
    }

    public void setUuid(@NotNull String uuid) {
        this.uuid = uuid;
    }

    @NotNull
    public String getCustomerUuid() {
        return customerUuid;
    }

    public long getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(long tradeTime) {
        this.tradeTime = tradeTime;
    }

    @NotNull
    public String getMerchantUuid() {
        return merchantUuid;
    }

    @NotNull
    public String getGoodItemStack() {
        return goodItemStack;
    }

    public int getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(int tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public boolean isGoodSystem() {
        return goodSystem;
    }

    public void setGoodSystem(boolean goodSystem) {
        this.goodSystem = goodSystem;
    }

    @NotNull
    public String getGoodType() {
        return goodType;
    }

    public void setGoodType(@NotNull String goodType) {
        this.goodType = goodType;
    }

    @Nullable
    public String getGoodCurrencyItemStack() {
        return goodCurrencyItemStack;
    }

    public void setGoodCurrencyItemStack(@Nullable String goodCurrencyItemStack) {
        this.goodCurrencyItemStack = goodCurrencyItemStack;
    }

    @Nullable
    public Double getGoodVaultPrice() {
        return goodVaultPrice;
    }

    public void setGoodVaultPrice(@Nullable Double goodVaultPrice) {
        this.goodVaultPrice = goodVaultPrice;
    }

    @Nullable
    public Integer getGoodPlayerPointsPrice() {
        return goodPlayerPointsPrice;
    }

    public void setGoodPlayerPointsPrice(@Nullable Integer goodPlayerPointsPrice) {
        this.goodPlayerPointsPrice = goodPlayerPointsPrice;
    }

    @Nullable
    public Integer getGoodItemPrice() {
        return goodItemPrice;
    }

    public void setGoodItemPrice(@Nullable Integer goodItemPrice) {
        this.goodItemPrice = goodItemPrice;
    }
}
