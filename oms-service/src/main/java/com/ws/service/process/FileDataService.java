package com.ws.service.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by gongmei on 2018/6/28.
 */
public class FileDataService {


    public FileDataService(){
        initItemFileData();
    }


    private void initItemFileData(){
        Constants.ITEM_MAP.keySet().forEach(key -> {
            String filePath = Constants.DATA_FILE_PATH + "/" + key + ".txt";
            List<String> items = getAllItemsByFile(filePath);
            Constants.ITEM_MAP.put(key,items);
        });
    }

    private List<String> getAllItemsByFile(String filePath){
        List<String> itemList = new LinkedList<>();
        File file = new File(filePath);
        if (file.exists() && file.canRead()){
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(file));
                String line = null;
                while ((line = br.readLine()) != null){
                    if (line.trim().length() > 0 && line.contains("-") && !line.contains("=")){
                        String[] split = line.split(",");
                        for (String str : split){
                            if (str != null && str.contains("-")){
                                itemList.add(str);
                            }
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if (br != null){
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return itemList;
    }


    public List<String> findCandidateFileNames(String fileName){
        if (!Constants.ITEM_MAP.containsKey(fileName)){
            System.out.println("输入的文件有误!");
            return null;
        }
        List<String> list = Arrays.asList(Constants.ITEMS);
        String[] split = fileName.split(Constants.ITEM_SEPARATOR);
        int startIndex = list.indexOf(split[0]);
        int endIndex  = list.indexOf(split[1]);
        if (endIndex - startIndex < 2){
            System.out.println("输入的间隔太小，最少要匹配3个！");
            return null;
        }
        List<String> candidateList = new LinkedList<>();
        for (int i = startIndex; i < endIndex; i++){
            String item = Constants.ITEMS[startIndex] + Constants.ITEM_SEPARATOR + Constants.ITEMS[i+1];
            candidateList.add(item);
        }
        return candidateList;
    }


}
