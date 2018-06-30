package com.baibeiyun.eazyfair.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/10.
 */
public class TimeUtils {

    /**
     * 时间格式化
     *
     * @param time
     * @return
     */
    public static String timeFormat(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(time);
        return format.format(date);
    }

    public static String timeFormat() {
        Date now = new Date();
        SimpleDateFormat da = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = da.format(now);
        return format;
    }


}
