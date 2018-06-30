package com.ws.service.process;


import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 *
 * Created by gongmei on 2018/6/28.
 */
public class MatchService {

    // A -> [B,C,D]
    private Map<String,List<String>> fileMappingMap = new HashMap<>(64);

    // A--B -> {1,[10,11,13]}
    // A--C -> {2,[1,2,3,4]}
    private Map<String,Map<Integer,List<Integer>>> fileItemMappingMap = new HashMap<>(64);


    public void prepareData(String fileName){
        // A--B -> [1-2,3-4,5-6]
        List<String> list = Constants.ITEM_MAP.get(fileName);
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        String[] split = fileName.split(Constants.ITEM_SEPARATOR);
        fileMappingMap.computeIfAbsent(split[0], key -> new ArrayList<>()).add(split[1]);

        for (String item : list){
            String[] arr = item.split("-");
            Integer first = Integer.valueOf(arr[0]);
            Integer second = Integer.valueOf(arr[1]);

            Map<Integer, List<Integer>> integerListMap = fileItemMappingMap.computeIfAbsent(fileName,x -> new HashMap<>());
            integerListMap.computeIfAbsent(first,key -> new ArrayList<>()).add(second);
        }
    }

    public void match(String start){
        List<String> list = fileMappingMap.get(start);
        for (String item : list){
            String key = start + Constants.ITEM_SEPARATOR + item;
            Map<Integer, List<Integer>> integerListMap = fileItemMappingMap.get(key);
            if (CollectionUtils.isEmpty(integerListMap)){
                continue;
            }
            Iterator<Integer> iterator = integerListMap.keySet().iterator();
            while (iterator.hasNext()){
                // A
                Integer next = iterator.next();
                //
                List<Integer> integers = integerListMap.get(next);

                for (Integer integer : integers){

                    match(item);

                }

            }
        }
    }

    public void print(){
        System.out.println(fileMappingMap);
        System.out.println(fileItemMappingMap);
    }

}
