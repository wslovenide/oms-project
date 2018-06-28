package com.ws.service.process;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by gongmei on 2018/6/28.
 */
public class Constants {

    public static final String[] ITEMS = {"A","B","C","D","E","F","G","H"};

    public static final String ITEM_SEPARATOR = "--";

    public static final Map<String,List<String>> ITEM_MAP = new HashMap<>();

    public static final String DATA_FILE_PATH = "/Users/gongmei/Desktop/sss/data";


    static {
        for (int i=0; i < ITEMS.length-1; i++){
            for (int j = i + 1; j < ITEMS.length ; j++){
                String key = ITEMS[i] + ITEM_SEPARATOR + ITEMS[j];
                ITEM_MAP.put(key,null);
            }
        }
    }



}
