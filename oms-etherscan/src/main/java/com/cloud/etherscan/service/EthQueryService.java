package com.cloud.etherscan.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cloud.etherscan.model.EthHolderDetail;
import com.cloud.etherscan.service.dto.ExportExcelDTO;
import com.cloud.etherscan.tool.HtmlParserUtil;
import com.cloud.etherscan.tool.URLEncoderUtil;
import com.cloud.etherscan.tool.http.HttpTookit;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

    private static ExecutorService networkQueryPool = Executors.newFixedThreadPool(1);
    private static ExecutorService exportExcelPool = Executors.newFixedThreadPool(2);

    private Logger logger = LoggerFactory.getLogger(EthQueryService.class);

    @Resource
    private EthExportService ethExportService;

    @Value("${eth.token.host}")
    private String ethHost;

    @Value("${eth.token.handler.host}")
    private String ethHandlerHost;

    @Value("${eth.token.searchHandler.host}")
    private String ethSearchHandlerHost;


    public void queryTokenByName(List<String> names,boolean perDay){
        List<Future> list = new ArrayList<>(names.size());
        for (String name : names){
            if (name == null || name.trim().length() < 1){
                continue;
            }
            name = name.trim();
            String url = ethSearchHandlerHost + "?term=" + URLEncoderUtil.urlEncode(name);
            try {
                Map<String,String> param = new HashMap<>();
                param.put("term",name);
                int j = 0;
                String get = null;
                while (j < 3){
                    get = HttpTookit.doGet(ethSearchHandlerHost, param);
                    if (get == null){
                        j++;
                    }else {
                        break;
                    }
                }
                if (get != null && !"".equals(get.trim())){
                    JSONArray jsonArray = JSON.parseArray(get);
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
                        if (fullName.trim().equalsIgnoreCase(name)){
                            logger.info("开始抓取[{}]的明细数据!",name);
                            list.add(networkQueryPool.submit(() -> {
                                queryEthTokenList(split[1],fullName,perDay);
                            } ));
                            break;
                        }
                    }
                }
            }catch (Exception e){
                logger.error("根据名称查询token失败! url = "+url+",  name = "+name,e);
            }
        }

        for (Future future : list){
            try {
                future.get();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        logger.info("全部执行完成!");
    }

    public void queryEthTokenList(String token,String ethName,boolean perDay){
        // 查询总数
        String[] tokenCount = getTotalCount(token);
        if (tokenCount == null){
            return;
        }
        // 查询前500条明细
        Map<String,String> param = new HashMap<>();
        param.put("a",token);
        param.put("s",tokenCount[1]);

        List<EthHolderDetail> all = new LinkedList<>();
        for (int i = 1 ; i <= 10; i++){
            param.put("p",String.valueOf(i));
            List<EthHolderDetail> list = queryEthTokenWithRetry(param,ethName);
            if (list != null){
                all.addAll(list);
            }else {
                break;
            }
        }
        if (all.size() != 500){
            logger.info("抓取{}明细数据失败! 只抓取到的明细条数为:{},不生成excel!",ethName,all.size());
            return;
        }
        exportExcelPool.submit(() -> {
            ExportExcelDTO excelDTO = calculateStatistic(all, tokenCount[0], ethName);
            excelDTO.setPerDay(perDay);
            ethExportService.saveToExcel(excelDTO);
        });
    }


    public ExportExcelDTO calculateStatistic(List<EthHolderDetail> list, String totalCount, String ethName){
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
        BigDecimal multi = new BigDecimal(100);
        BigDecimal divide = top10.multiply(multi).divide(totalToken, 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal divide1 = top20.multiply(multi).divide(totalToken, 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal divide2 = top50.multiply(multi).divide(totalToken, 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal divide3 = top100.multiply(multi).divide(totalToken, 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal divide4 = top200.multiply(multi).divide(totalToken, 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal divide5 = top500.multiply(multi).divide(totalToken, 2, BigDecimal.ROUND_HALF_UP);

        ExportExcelDTO excelDTO = new ExportExcelDTO();
        excelDTO.setEthName(ethName);
        excelDTO.setTotalCount(totalCount);
        excelDTO.setList(list);
        excelDTO.setTop10(top10);
        excelDTO.setTop20(top20);
        excelDTO.setTop50(top50);
        excelDTO.setTop100(top100);
        excelDTO.setTop200(top200);
        excelDTO.setTop500(top500);

        excelDTO.setTop10Rate(divide);
        excelDTO.setTop20Rate(divide1);
        excelDTO.setTop50Rate(divide2);
        excelDTO.setTop100Rate(divide3);
        excelDTO.setTop200Rate(divide4);
        excelDTO.setTop500Rate(divide5);
        return excelDTO;
    }

    public List<EthHolderDetail> queryEthTokenWithRetry(Map<String,String> param,String ethName){
        int i = 0;
        try {
            while (i < 5){
                String doGet = HttpTookit.doGet(ethHandlerHost, param);
                if (doGet == null || "".equalsIgnoreCase(doGet.trim())){
                    i++;
                    continue;
                }
                List<EthHolderDetail> list = HtmlParserUtil.parseTokenDetail(doGet);
                logger.info("查询{}明细，第{}次的结果条数为:{}, url = {}, param = {}",ethName,i+1,list.size(),ethHandlerHost,param);
                if (list.isEmpty()){
                    i++;
                    continue;
                }
                return list;
            }
        }catch (Exception e){
            logger.error("查询分页数据出错！ethHandlerHost = " + ethHandlerHost +",  param = " + param + " , i = " + i + " , ethName = " + ethName,e);
        }
        return null;
    }


    public String[] getTotalCount(String token){
        int i = 0;
        String url = ethHost + "/" + token;
        try {
            while (i < 5){
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
