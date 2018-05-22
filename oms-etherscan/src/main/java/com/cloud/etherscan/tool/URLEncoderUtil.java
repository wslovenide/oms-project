package com.cloud.etherscan.tool;

import java.net.URLEncoder;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-05-22 11:16
 */
public class URLEncoderUtil {

    public static String urlEncode(String url){
        String encode = null;
        try {
            encode = URLEncoder.encode(url, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        return encode;
    }

}
