package pers.zhangyang.easyguishop.meta;

public class VersionMeta {
    private final int big;
    private final int middle;
    private final int small;

    public VersionMeta(int big, int middle, int small) {
        this.big = big;
        this.middle = middle;
        this.small = small;
    }

    public int getBig() {
        return big;
    }

    public int getMiddle() {
        return middle;
    }

    public int getSmall() {
        return small;
    }
}
