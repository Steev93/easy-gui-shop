package pers.zhangyang.easyguishop.meta;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class IconOwnerMeta {
    private final String iconUuid;
    private final String ownerUuid;

    public IconOwnerMeta(@NotNull String iconUuid, @NotNull String ownerUuid) {
        this.iconUuid = iconUuid;
        this.ownerUuid = ownerUuid;
    }

    @NotNull
    public String getIconUuid() {
        return iconUuid;
    }

    @NotNull
    public String getOwnerUuid() {
        return ownerUuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IconOwnerMeta that = (IconOwnerMeta) o;
        return Objects.equals(iconUuid, that.iconUuid) && Objects.equals(ownerUuid, that.ownerUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iconUuid, ownerUuid);
    }
}
