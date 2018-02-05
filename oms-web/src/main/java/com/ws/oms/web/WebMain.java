package com.ws.oms.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-02-05 10:33
 */
@SpringBootApplication
public class WebMain {

    private static Logger logger = LoggerFactory.getLogger(WebMain.class);

    public static void main(String[] args) {
        logger.info("开始启动web应用......");
        SpringApplication.run(WebMain.class,args);
    }

}
