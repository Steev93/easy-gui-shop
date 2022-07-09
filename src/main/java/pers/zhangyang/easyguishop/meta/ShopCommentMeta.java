package pers.zhangyang.easyguishop.meta;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ShopCommentMeta {
    private final String uuid;
    private final String commenterUuid;
    private final String shopUuid;
    private final String content;
    private final long commentTime;

    public ShopCommentMeta(@NotNull String uuid, @NotNull String commenterUuid, @NotNull String shopUuid, @NotNull String content, long commentTime) {
        this.uuid = uuid;
        this.commenterUuid = commenterUuid;
        this.shopUuid = shopUuid;
        this.content = content;
        this.commentTime = commentTime;
    }

    @NotNull
    public String getUuid() {
        return uuid;
    }

    @NotNull
    public String getCommenterUuid() {
        return commenterUuid;
    }

    @NotNull
    public String getShopUuid() {
        return shopUuid;
    }

    @NotNull
    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShopCommentMeta that = (ShopCommentMeta) o;
        return commentTime == that.commentTime && Objects.equals(uuid, that.uuid) && Objects.equals(commenterUuid, that.commenterUuid) && Objects.equals(shopUuid, that.shopUuid) && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, commenterUuid, shopUuid, content, commentTime);
    }

    public long getCommentTime() {
        return commentTime;
    }
}
