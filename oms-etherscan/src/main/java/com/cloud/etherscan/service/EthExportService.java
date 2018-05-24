package com.cloud.etherscan.service;

import com.cloud.etherscan.model.EthHolderDetail;
import com.cloud.etherscan.service.dto.ExportExcelDTO;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gongmei on 2018/5/23.
 */
@Service
public class EthExportService {

    private Logger logger = LoggerFactory.getLogger(EthExportService.class);

    @Value("${eth.token.save.path}")
    private String savePath;

    public void saveToExcel(ExportExcelDTO excelDTO){
        String date = new SimpleDateFormat("MM月dd日").format(new Date());
        String time = new SimpleDateFormat("HH时mm分").format(new Date());
        String path = savePath+ "/" + excelDTO.getEthName().trim() + "/" + date;
        File filePath = new File(path);
        if (!filePath.exists()){
            filePath.mkdirs();
        }
        saveExcelDetail(excelDTO,path,date,time);

        try {
            saveDayStatistic(excelDTO,path,date,time);
            logger.info("保存到当日统计文件完成! ethName = {}, date = {}",excelDTO.getEthName(),date);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveExcelDetail(ExportExcelDTO excelDTO,String filePath,String date,String time){
        try {
            String dateTime = date + time;
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet(dateTime);

            HSSFRow row = sheet.createRow(0);
            HSSFCell cell = row.createCell(1);
            cell.setCellValue(excelDTO.getEthName()+"持有数据汇总("+dateTime+")");

            row.createCell(2).setCellValue("数量");
            row.createCell(3).setCellValue("占比");

            HSSFRow row1 = sheet.createRow(1);
            row1.createCell(1).setCellValue("发行总量");
            row1.createCell(2).setCellValue(excelDTO.getTotalCount());
            row1.createCell(3).setCellValue("100%");

            HSSFRow row2 = sheet.createRow(2);
            row2.createCell(1).setCellValue("TOP10持有量");
            row2.createCell(2).setCellValue(excelDTO.getTop10().toString());
            row2.createCell(3).setCellValue(excelDTO.getTop10Rate().toString()+"%");

            HSSFRow row3 = sheet.createRow(3);
            row3.createCell(1).setCellValue("TOP20持有量");
            row3.createCell(2).setCellValue(excelDTO.getTop20().toString());
            row3.createCell(3).setCellValue(excelDTO.getTop20Rate().toString()+"%");

            HSSFRow row4 = sheet.createRow(4);
            row4.createCell(1).setCellValue("TOP50持有量");
            row4.createCell(2).setCellValue(excelDTO.getTop50().toString());
            row4.createCell(3).setCellValue(excelDTO.getTop50Rate().toString()+"%");

            HSSFRow row5 = sheet.createRow(5);
            row5.createCell(1).setCellValue("TOP100持有量");
            row5.createCell(2).setCellValue(excelDTO.getTop100().toString());
            row5.createCell(3).setCellValue(excelDTO.getTop100Rate().toString()+"%");

            HSSFRow row6 = sheet.createRow(6);
            row6.createCell(1).setCellValue("TOP200持有量");
            row6.createCell(2).setCellValue(excelDTO.getTop200().toString());
            row6.createCell(3).setCellValue(excelDTO.getTop200Rate().toString()+"%");

            HSSFRow row7 = sheet.createRow(7);
            row7.createCell(1).setCellValue("TOP500持有量");
            row7.createCell(2).setCellValue(excelDTO.getTop500().toString());
            row7.createCell(3).setCellValue(excelDTO.getTop500Rate().toString()+"%");

            HSSFRow row9 = sheet.createRow(9);
            row9.createCell(0).setCellValue("排名");
            row9.createCell(1).setCellValue("持有者地址");
            row9.createCell(2).setCellValue("数量");
            row9.createCell(3).setCellValue("百分比");

            for (int i = 0 ; i < excelDTO.getList().size(); i++){
                HSSFRow row10 = sheet.createRow(10+i);

                EthHolderDetail detail = excelDTO.getList().get(i);

                row10.createCell(0).setCellValue(i+1);
                row10.createCell(1).setCellValue(detail.getAddress());
                row10.createCell(2).setCellValue(detail.getQuantity());
                row10.createCell(3).setCellValue(detail.getPercentage());
            }
            String fileName = excelDTO.getEthName()+"-"+dateTime+".xls";
            File file = new File(new File(filePath),fileName);

            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);

            workbook.write(file);
            workbook.close();

            logger.info("保存[{}]文件成功!",fileName);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("生成文件出错!",e);
        }
    }

    private void saveDayStatistic(ExportExcelDTO excelDTO,String filePath,String date,String time) throws Exception{
        String fileName = excelDTO.getEthName()+"-"+date+".xls";
        File file = new File(filePath,fileName);
        if (file.exists()){
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
            HSSFSheet sheetAt = workbook.getSheetAt(0);
            short lastCellNum = sheetAt.getRow(0).getLastCellNum();

            sheetAt.getRow(0).createCell(lastCellNum).setCellValue(time);
            sheetAt.getRow(1).createCell(lastCellNum).setCellValue(excelDTO.getTop10Rate().toString()+"%");
            sheetAt.getRow(2).createCell(lastCellNum).setCellValue(excelDTO.getTop20Rate().toString()+"%");
            sheetAt.getRow(3).createCell(lastCellNum).setCellValue(excelDTO.getTop50Rate().toString()+"%");
            sheetAt.getRow(4).createCell(lastCellNum).setCellValue(excelDTO.getTop100Rate().toString()+"%");
            sheetAt.getRow(5).createCell(lastCellNum).setCellValue(excelDTO.getTop200Rate().toString()+"%");
            sheetAt.getRow(6).createCell(lastCellNum).setCellValue(excelDTO.getTop500Rate().toString()+"%");
            workbook.write(file);
            workbook.close();
        }else {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet(fileName);

            HSSFRow row = sheet.createRow(0);
            row.createCell(0).setCellValue("时间");

            HSSFRow row1 = sheet.createRow(1);
            row1.createCell(0).setCellValue("TOP10持有量");

            HSSFRow row2 = sheet.createRow(2);
            row2.createCell(0).setCellValue("TOP20持有量");

            HSSFRow row3 = sheet.createRow(3);
            row3.createCell(0).setCellValue("TOP50持有量");

            HSSFRow row4 = sheet.createRow(4);
            row4.createCell(0).setCellValue("TOP100持有量");

            HSSFRow row5 = sheet.createRow(5);
            row5.createCell(0).setCellValue("TOP200持有量");

            HSSFRow row6 = sheet.createRow(6);
            row6.createCell(0).setCellValue("TOP500持有量");

            sheet.getRow(0).createCell(1).setCellValue(time);
            sheet.getRow(1).createCell(1).setCellValue(excelDTO.getTop10Rate().toString()+"%");
            sheet.getRow(2).createCell(1).setCellValue(excelDTO.getTop20Rate().toString()+"%");
            sheet.getRow(3).createCell(1).setCellValue(excelDTO.getTop50Rate().toString()+"%");
            sheet.getRow(4).createCell(1).setCellValue(excelDTO.getTop100Rate().toString()+"%");
            sheet.getRow(5).createCell(1).setCellValue(excelDTO.getTop200Rate().toString()+"%");
            sheet.getRow(6).createCell(1).setCellValue(excelDTO.getTop500Rate().toString()+"%");

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);

            workbook.write(file);
            workbook.close();
        }
    }



}
