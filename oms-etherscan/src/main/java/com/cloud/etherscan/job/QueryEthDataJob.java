package com.cloud.etherscan.job;

import com.cloud.etherscan.service.EthQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-05-22 10:08
 */
@Service
public class QueryEthDataJob {

    private Logger logger = LoggerFactory.getLogger(QueryEthDataJob.class);

    @Value("${eth.token.file.path}")
    private String filePath;

    @Resource
    private EthQueryService ethService;

    @Scheduled(cron = "*/30 * * * * ?")
    public void queryEth(){
        try {
//            if (!validateTokenFile()){
//                return;
//            }
//            List<String> list = Files.readAllLines(Paths.get(filePath), Charset.forName("UTF-8"));
//            logger.info("待抓取的token有:{}",list);
//            if (CollectionUtils.isEmpty(list)){
//                logger.info("待抓取的token为空!");
//                return;
//            }
            List<String> list = new ArrayList<>();
            list.add("eos");
            ethService.queryTokenByName(list);
        }catch (Exception e){
            logger.error("抓取数据出错!",e);
        }
    }

    private boolean validateTokenFile(){
        logger.info("token的文件路径为:{}",filePath);
        File file = new File(filePath);
        if (file.exists() && file.isFile() && file.canRead()){
            return true;
        }
        logger.info("文件不存在! file = {}",filePath);
        return false;
    }


}
