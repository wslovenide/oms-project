package com.ws.oms.chat.netty.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gongmei on 2018/3/5.
 */
public class DateUtil {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String TIME_FORMAT = "HH:mm:ss";

    public static String getNowTimeStr(){
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
        return sdf.format(new Date());
    }

}
