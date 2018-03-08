package com.ws.oms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-02-05 10:33
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ImportResource("classpath:spring/applicationContext-*.xml")
public class WebMain {

    public static void main(String[] args) {
        SpringApplication.run(WebMain.class,args);
    }

}
