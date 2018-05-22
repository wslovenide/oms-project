package com.cloud.etherscan.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cloud.etherscan.model.EthHolderDetail;
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

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
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

    private static ExecutorService threadPool = Executors.newFixedThreadPool(20);

    private Logger logger = LoggerFactory.getLogger(EthQueryService.class);

    @Value("${eth.token.host}")
    private String ethHost;

    @Value("${eth.token.handler.host}")
    private String ethHandlerHost;

    @Value("${eth.token.searchHandler.host}")
    private String ethSearchHandlerHost;

    @Value("${eth.token.save.path}")
    private String savePath;

    public void queryTokenByName(List<String> names){
        for (String name : names){
            if (name == null || name.trim().length() < 1){
                continue;
            }
            name = name.trim();
            String url = ethSearchHandlerHost + "?term=" + URLEncoderUtil.urlEncode(name);
            try {
                Map<String,String> param = new HashMap<>();
                param.put("term",name);
                String get = HttpTookit.doGet(ethSearchHandlerHost, param);
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

        calculateStatistic(all,tokenCount,ethName);

        System.out.println("生成 [" + ethName +"] 完成");

    }


    public void calculateStatistic(List<EthHolderDetail> list,String totalCount,String ethName){
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

        try {
            String date = new SimpleDateFormat("MM月dd日HH时mm分").format(new Date());

            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet(date);

            HSSFRow row = sheet.createRow(0);
            HSSFCell cell = row.createCell(1);
            cell.setCellValue(ethName+"持有数据汇总("+date+")");


            row.createCell(2).setCellValue("数量");
            row.createCell(3).setCellValue("占比");


            HSSFRow row1 = sheet.createRow(1);
            row1.createCell(1).setCellValue("发行总量");
            row1.createCell(2).setCellValue(totalCount);
            row1.createCell(3).setCellValue("100%");

            HSSFRow row2 = sheet.createRow(2);
            row2.createCell(1).setCellValue("TOP10持有量");
            row2.createCell(2).setCellValue(top10.toString());
            row2.createCell(3).setCellValue(divide.toString()+"%");


            HSSFRow row3 = sheet.createRow(3);
            row3.createCell(1).setCellValue("TOP20持有量");
            row3.createCell(2).setCellValue(top20.toString());
            row3.createCell(3).setCellValue(divide1.toString()+"%");


            HSSFRow row4 = sheet.createRow(4);
            row4.createCell(1).setCellValue("TOP50持有量");
            row4.createCell(2).setCellValue(top50.toString());
            row4.createCell(3).setCellValue(divide2.toString()+"%");


            HSSFRow row5 = sheet.createRow(5);
            row5.createCell(1).setCellValue("TOP100持有量");
            row5.createCell(2).setCellValue(top100.toString());
            row5.createCell(3).setCellValue(divide3.toString()+"%");

            HSSFRow row6 = sheet.createRow(6);
            row6.createCell(1).setCellValue("TOP200持有量");
            row6.createCell(2).setCellValue(top200.toString());
            row6.createCell(3).setCellValue(divide4.toString()+"%");

            HSSFRow row7 = sheet.createRow(7);
            row7.createCell(1).setCellValue("TOP500持有量");
            row7.createCell(2).setCellValue(top500.toString());
            row7.createCell(3).setCellValue(divide5.toString()+"%");


            HSSFRow row9 = sheet.createRow(9);
            row9.createCell(0).setCellValue("排名");
            row9.createCell(1).setCellValue("持有者地址");
            row9.createCell(2).setCellValue("数量");
            row9.createCell(3).setCellValue("百分比");

            for (int i = 0 ; i < list.size(); i++){
                HSSFRow row10 = sheet.createRow(10+i);

                EthHolderDetail detail = list.get(i);

                row10.createCell(0).setCellValue(i+1);
                row10.createCell(1).setCellValue(detail.getAddress());
                row10.createCell(2).setCellValue(detail.getQuantity());
                row10.createCell(3).setCellValue(detail.getPercentage());

            }
            File file = new File(new File(savePath),ethName+date+".xlsx");


            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);

            workbook.write(file);
            workbook.close();
        }catch (Exception e){
            logger.error("生成文件出错!",e);
        }
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
            logger.error("查询分页数据出错！ethHandlerHost = " + ethHandlerHost +",  param = " + param + " , i = " + i,e);
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
