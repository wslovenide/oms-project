package com.cloud.etherscan.service;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-05-25 9:50
 */
@Service
public class EthChartService {

//    public static void main(String[] args) {
//
//        try {
//            File file = new File("G:\\logs\\EOS\\05月25日\\EOS-05月25日-日汇总.xls");
//
//            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
//
//            new EthChartService().createChartToExcel(workbook);
//
//            workbook.write(new FileOutputStream(file));
//            workbook.close();
//            System.out.println("finished....");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    public void createChartToExcel(HSSFWorkbook workbook){
        HSSFSheet sheet = workbook.getSheetAt(0);
        for (int i = 1; i <= 6; i++){
            HSSFRow row = sheet.getRow(i);
            short lastCellNum = row.getLastCellNum();
            double[] data = new double[lastCellNum-1];
            String[] colKeys = new String[lastCellNum-1];
            for (int j = 1; j < lastCellNum ; j++){
                String cellValue = row.getCell(j).getStringCellValue();
                double dValue = Double.parseDouble(cellValue.replace("%",""));
                data[j-1] = dValue;
                colKeys[j-1] = String.valueOf(j);
            }
            String cellValue = row.getCell(0).getStringCellValue();
            byte[] image = readDataFromExcel(cellValue,data,colKeys);
            insertImageToExcel(workbook,image,i);
        }
    }

    public byte[] readDataFromExcel(String title,double[] data,String[] rowKeys){
        CategoryDataset dataset = DatasetUtilities.createCategoryDataset(new String[]{title}, rowKeys, new double[][]{data});

        StandardChartTheme standardChartTheme=new StandardChartTheme("CN");
        standardChartTheme.setExtraLargeFont(new Font("隶书",Font.PLAIN,12));
        standardChartTheme.setRegularFont(new Font("宋书",Font.PLAIN,12));
        standardChartTheme.setLargeFont(new Font("宋书",Font.PLAIN,12));

        ChartFactory.setChartTheme(standardChartTheme);
        JFreeChart jfreechart = ChartFactory.createLineChart("",title,"",dataset,
                PlotOrientation.VERTICAL,false,false,false);

        CategoryPlot plot = (CategoryPlot)jfreechart.getPlot();
        plot.setBackgroundPaint(SystemColor.darkGray);
        LineAndShapeRenderer renderer = (LineAndShapeRenderer)plot.getRenderer();
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setSeriesStroke(0,new BasicStroke(1.5F));
        renderer.setSeriesOutlineStroke(0, new BasicStroke(2F));//设置折点的大小

        double min = 0.0 , max = 0.0;
        for (double d : data){
            if (min == 0.0){
                min = d;
            }
            min = Double.min(min,d);
            max = Double.max(max,d);
        }
        NumberAxis nrangeAxis = (NumberAxis)plot.getRangeAxis(0);
        nrangeAxis.setAutoTickUnitSelection(false);
        nrangeAxis.setRange(min-0.05, max+0.05);//自定义范围
        nrangeAxis.setTickUnit(new NumberTickUnit(0.03));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
        try {
            ChartUtilities.writeChartAsPNG(outputStream,jfreechart,400,200);
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertImageToExcel(HSSFWorkbook workbook,byte[] image , int i){
        int j = i;
        if (j > 3){
            j = j - 3;
        }
        int firstX = (j - 1) * 6;
        int secondX = firstX + 6;
        firstX++;

        int y1 = 10, y2 = 20;
        if (i > 3){
            y1 = 23;
            y2 = 33;
        }
        // TODO: 2018-05-25   图片会重复叠加上去
        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 255, 255,(short) firstX, y1, (short) secondX, y2);
        anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
        patriarch.createPicture(anchor, workbook.addPicture(image, HSSFWorkbook.PICTURE_TYPE_JPEG));
    }


}
