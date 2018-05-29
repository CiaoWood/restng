package com.microdev.automation.restng.util.datetime;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private DateUtil() {
    }

    public static String today() {
        Date now = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd");
        return ft.format(now);
    }

}
