package com.cloud.etherscan.job;

import java.net.URLEncoder;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-05-22 11:12
 */
public class Test {

    public static void main(String[] args) throws Exception{

        String encode = URLEncoder.encode("DEBIT Coin", "UTF-8");
        System.out.println(encode);


    }

}
