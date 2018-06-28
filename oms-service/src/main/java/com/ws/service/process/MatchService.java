package com.ws.service.process;


import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by gongmei on 2018/6/28.
 */
public class MatchService {

    private Map<String,Object> map = new HashMap<>(1024);

    private Map<String,List<String>> listMap = new HashMap<>(64);


    public void match(String fileName){
        List<String> list = Constants.ITEM_MAP.get(fileName);
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        String[] split = fileName.split(Constants.ITEM_SEPARATOR);
        listMap.computeIfAbsent(split[0], key -> new ArrayList<>()).add(split[1]);





    }

}
