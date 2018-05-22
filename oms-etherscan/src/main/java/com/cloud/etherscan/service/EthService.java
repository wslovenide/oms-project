package com.cloud.etherscan.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cloud.etherscan.tool.URLEncoderUtil;
import com.cloud.etherscan.tool.http.HttpTookit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-05-22 10:31
 */
@Service
public class EthService {


    private static ExecutorService threadPool = Executors.newFixedThreadPool(10);

    private Logger logger = LoggerFactory.getLogger(EthService.class);

    @Value("${eth.token.host}")
    private String ethHost;

    @Value("${eth.token.handler.host}")
    private String ethHandlerHost;

    @Value("${eth.token.searchHandler.host}")
    private String ethSearchHandlerHost;

    public void queryTokenByName(List<String> names){
        for (String name : names){
            String url = ethSearchHandlerHost + "?term=" + URLEncoderUtil.urlEncode(name);
            try {
                Map<String,String> param = new HashMap<>();
                param.put("term",name);
                String get = HttpTookit.doGet(ethSearchHandlerHost, param);
                if (get != null && !"".equals(get.trim())){
                    JSONArray jsonArray = JSON.parseArray(get);
                    System.out.println(jsonArray);
                    if (jsonArray.size() == 0){
                        continue;
                    }
                    for (int i=0; i< jsonArray.size();i++){
                        String object = (String) jsonArray.get(i);
                        String[] split = object.split("\t");
                        String fullName;
                        int index = split[0].indexOf("(");
                        if (index > 0){
                            fullName = split[0].substring(0,index );
                        }else {
                            fullName = split[0];
                        }
                        if (fullName.trim().equalsIgnoreCase(name.trim())){
                            threadPool.submit(() -> {
                                queryEthTokenList(split[1],fullName);
                            } );
                            break;
                        }
                    }
                }
            }catch (Exception e){
                logger.error("根据名称查询token失败! url = "+url+",  name = "+name,e);
            }
        }
    }

    public void queryEthTokenList(String token,String ethName){
        Map<String,String> param = new HashMap<>();
        param.put("a",token);
        param.put("s","1000000000000000000000000000");
        for (int i = 1 ; i <= 10;){
            param.put("p",String.valueOf(i));
            String doGet = HttpTookit.doGet(ethHandlerHost, param);
            if (doGet == null || "".equalsIgnoreCase(doGet.trim())){
                continue;
            }
            i++;
        }
    }



}
