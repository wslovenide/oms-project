package com.ws.service.process;

import java.util.List;

/**
 * Created by gongmei on 2018/6/28.
 */
public class Start {


    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        List<String> candidateFileNames = new FileDataService().findCandidateFileNames("A--D");

        MatchService matchService = new MatchService();
        System.out.println(candidateFileNames);

        for (String str : candidateFileNames){
            matchService.prepareData(str);
        }
        matchService.print();

        System.out.println(System.currentTimeMillis() - start);
    }

}
