package com.baibeiyun.eazyfair.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static String getDate(){
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return f.format(date);
}
}
