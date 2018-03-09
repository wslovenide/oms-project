package com.ws.oms.chat.netty.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by gongmei on 2018/3/9.
 */
public class ZhuanYiCharUtil {

    public static String zhuanYi(String msg){
        if (StringUtils.isBlank(msg)){
            return msg;
        }
        msg = msg.replaceAll("<","&lt;");
        msg = msg.replaceAll(">","&gt;");
        return msg;
    }


}
