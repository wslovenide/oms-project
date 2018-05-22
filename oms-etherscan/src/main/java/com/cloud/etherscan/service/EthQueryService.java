package com.cloud.etherscan.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cloud.etherscan.model.EthHolderDetail;
import com.cloud.etherscan.tool.HtmlParserUtil;
import com.cloud.etherscan.tool.URLEncoderUtil;
import com.cloud.etherscan.tool.http.HttpTookit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
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
public class EthQueryService {

    private static ExecutorService threadPool = Executors.newFixedThreadPool(30);

    private Logger logger = LoggerFactory.getLogger(EthQueryService.class);

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

        // 查询前500条明细
        Map<String,String> param = new HashMap<>();
        param.put("a",token);
        param.put("s","1000000000000000000000000000");

        List<EthHolderDetail> all = new LinkedList<>();
        for (int i = 1 ; i <= 10; i++){
            param.put("p",String.valueOf(i));
            List<EthHolderDetail> list = queryEthTokenWithRetry(param);
            if (list != null){
                all.addAll(list);
            }
        }

        // 查询总数
        String tokenCount = getTotalCount(token);

        calculateStatistic(all,tokenCount);

    }


    public void calculateStatistic(List<EthHolderDetail> list,String totalCount){
        BigDecimal top10  = BigDecimal.ZERO , top20 = BigDecimal.ZERO, top50 = BigDecimal.ZERO;
        BigDecimal top100 = BigDecimal.ZERO, top200 = BigDecimal.ZERO, top500 = BigDecimal.ZERO;

        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0 ; i < list.size(); i++){
            EthHolderDetail detail = list.get(i);
            BigDecimal bigDecimal = new BigDecimal(detail.getQuantity());
            total = total.add(bigDecimal);

            if (i == 9){
                top10 = total;
            }else if (i == 19){
                top20 = total;
            }else if (i == 49){
                top50 = total;
            }else if(i == 99){
                top100 = total;
            }else if (i == 199){
                top200 = total;
            }else if (i == 499){
                top500 = total;
            }
        }
        BigDecimal totalToken = new BigDecimal(totalCount);
        top10.divide(totalToken,4,BigDecimal.ROUND_HALF_UP);
        top20.divide(totalToken,4,BigDecimal.ROUND_HALF_UP);
        top50.divide(totalToken,4,BigDecimal.ROUND_HALF_UP);
        top100.divide(totalToken,4,BigDecimal.ROUND_HALF_UP);
        top200.divide(totalToken,4,BigDecimal.ROUND_HALF_UP);
        top500.divide(totalToken,4,BigDecimal.ROUND_HALF_UP);

    }





    public List<EthHolderDetail> queryEthTokenWithRetry(Map<String,String> param){
        int i = 0;
        try {
            while (i < 3){
                String doGet = HttpTookit.doGet(ethHandlerHost, param);
                if (doGet == null || "".equalsIgnoreCase(doGet.trim())){
                    i++;
                    continue;
                }
                return HtmlParserUtil.parseTokenDetail(doGet);
            }
        }catch (Exception e){
            logger.error("查询分页数据出错！ param = " + param + " , i = " + i,e);
        }
        return null;
    }


    public String getTotalCount(String token){
        int i = 0;
        String url = ethHost + "/" + token;
        try {
            while (i < 3){
                String doGet = HttpTookit.doGet(url, new HashMap<>());
                if (doGet == null || "".equalsIgnoreCase(doGet.trim())){
                    i++;
                    continue;
                }
                return HtmlParserUtil.parseTokenCount(doGet);
            }
        }catch (Exception e){
            logger.error("查询总数数据出错！ url = " + url + " , i = " + i,e);
        }
        return null;
    }


}
