package pers.zhangyang.easyguishop.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    public static String getTimeFromTimeMill(long mill) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = new Date(mill);
        return sdf.format(date);


    }

}
