package com.cloud.etherscan.tool;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by gongmei on 2018/5/23.
 */
public class Test {

    public static void main(String[] args) throws Exception {

        String fileName = "EOS-05月23日.xls";
        File file = new File("/Users/gongmei/Desktop/eths/EOS/05月23日",fileName);


        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
        HSSFSheet sheetAt = workbook.getSheetAt(0);
        short lastCellNum = sheetAt.getRow(0).getLastCellNum();
        int physicalNumberOfCells = sheetAt.getRow(0).getPhysicalNumberOfCells();
        System.out.println(lastCellNum);
        System.out.println(physicalNumberOfCells);

        sheetAt.getRow(0).createCell(lastCellNum).setCellValue("admin");

        workbook.write(file);
        workbook.close();

    }

}
