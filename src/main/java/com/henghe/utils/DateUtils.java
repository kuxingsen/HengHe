package com.henghe.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils{
    public static String today(String pattern) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
}
