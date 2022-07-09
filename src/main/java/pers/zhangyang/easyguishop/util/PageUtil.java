package pers.zhangyang.easyguishop.util;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PageUtil {

    //返回第x页的内容,
    public static <T> List<T> page(int pageIndex, int capacity, @NotNull List<T> list) {
        List<T> rl = new ArrayList<>();
        for (int i = pageIndex * capacity; i < pageIndex * capacity + capacity; i++) {
            if (list.size() <= i) {
                break;
            }
            rl.add(list.get(i));
        }
        return rl;
    }

    //由所有物品数量和每页能容纳的物品数量，计算出最后一页的下标 最少为0
    public static int computeMaxPageIndex(int all, int capacity) {
        int maxPage = 0;
        if (all != 0) {
            maxPage = (all % capacity == 0 ? all / capacity - 1 : all / capacity);
        }
        return maxPage;
    }
}
