package pers.zhangyang.easyguishop.meta;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ShopCollectorMeta {
    private String shopUuid;
    private String collectorUuid;

    public ShopCollectorMeta() {
    }

    public ShopCollectorMeta(@NotNull String shopUuid, @NotNull String collectorUuid) {
        this.shopUuid = shopUuid;
        this.collectorUuid = collectorUuid;
    }

    @NotNull
    public String getShopUuid() {
        return shopUuid;
    }

    @NotNull
    public String getCollectorUuid() {
        return collectorUuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShopCollectorMeta that = (ShopCollectorMeta) o;
        return Objects.equals(shopUuid, that.shopUuid) && Objects.equals(collectorUuid, that.collectorUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shopUuid, collectorUuid);
    }
}
