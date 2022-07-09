package pers.zhangyang.easyguishop.meta;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ShopMeta {
    private final String uuid;
    private String name;
    private final String ownerUuid;
    private final long createTime;
    private int collectAmount;
    private String iconUuid;
    private String location;
    private int popularity;
    private int pageView;
    private String description;


    public ShopMeta(@NotNull String uuid, @NotNull String name, @NotNull String ownerUuid, long createTime,
                    int collectNumber, int popularity, int pageView) {
        this.uuid = uuid;
        this.name = name;
        this.ownerUuid = ownerUuid;
        this.createTime = createTime;
        this.collectAmount = collectNumber;
        this.popularity = popularity;
        this.pageView = pageView;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setShopDescription(@Nullable String description) {
        this.description = description;
    }

    public int getPageView() {
        return pageView;
    }

    public void setPageView(int pageView) {
        this.pageView = pageView;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
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
    public String getOwnerUuid() {
        return ownerUuid;
    }

    public long getCreateTime() {
        return createTime;
    }

    public int getCollectAmount() {
        return collectAmount;
    }

    public void setCollectAmount(int collectAmount) {
        this.collectAmount = collectAmount;
    }

    @Nullable
    public String getIconUuid() {
        return iconUuid;
    }

    public void setIconUuid(@Nullable String iconUuid) {
        this.iconUuid = iconUuid;
    }

    @Nullable
    public String getLocation() {
        return location;
    }

    public void setLocation(@Nullable String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShopMeta shopMeta = (ShopMeta) o;
        return createTime == shopMeta.createTime && collectAmount == shopMeta.collectAmount && popularity == shopMeta.popularity && pageView == shopMeta.pageView && Objects.equals(uuid, shopMeta.uuid) && Objects.equals(name, shopMeta.name) && Objects.equals(ownerUuid, shopMeta.ownerUuid) && Objects.equals(iconUuid, shopMeta.iconUuid) && Objects.equals(location, shopMeta.location) && Objects.equals(description, shopMeta.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, ownerUuid, createTime, collectAmount, iconUuid, location, popularity, pageView, description);
    }
}
