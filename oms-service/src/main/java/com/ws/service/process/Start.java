package com.ws.service.process;

import java.util.List;

/**
 * Created by gongmei on 2018/6/28.
 */
public class Start {


    public static void main(String[] args) {

        List<String> candidateFileNames = new FileDataService().findCandidateFileNames("A--D");
        System.out.println(candidateFileNames);


    }

}
