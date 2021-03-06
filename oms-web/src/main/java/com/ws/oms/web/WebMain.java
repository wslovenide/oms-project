package com.ws.oms.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-02-05 10:33
 */
@SpringBootApplication
@ImportResource("classpath:shiro.xml")
public class WebMain {

    public static void main(String[] args) {
        SpringApplication.run(WebMain.class,args);
    }

}
