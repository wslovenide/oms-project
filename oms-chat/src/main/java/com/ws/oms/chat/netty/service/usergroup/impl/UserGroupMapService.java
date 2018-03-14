package com.ws.oms.chat.netty.service.usergroup.impl;

import com.ws.oms.chat.netty.service.usergroup.IUserGroupService;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-03-13 13:35
 */
public class UserGroupMapService implements IUserGroupService {

    // 每个用户对应的group列表
    private static Map<String,Set<String>> sessionGroupMap = new ConcurrentHashMap<>(512);

    // 每个group里面有多少人
    private static Map<String,Set<String>> groupSessionMap = new ConcurrentHashMap<>(512);

    @Override
    public void save(String groupId, String sessionId) {
        Set<String> groupSet = sessionGroupMap.get(sessionId);
        if (groupSet == null){
            groupSet = new HashSet<>();
            sessionGroupMap.put(sessionId,groupSet);
        }
        groupSet.add(groupId);

        Set<String> sessionSet = groupSessionMap.get(groupId);
        if (sessionSet == null){
            sessionSet = new HashSet<>();
            groupSessionMap.put(groupId,sessionSet);
        }
        sessionSet.add(sessionId);
    }

    @Override
    public Set<String> getSessionList(String groupId) {
        return groupSessionMap.get(groupId);
    }

    @Override
    public Set<String> getGroupList(String sessionId) {
        return sessionGroupMap.get(sessionId);
    }

    @Override
    public boolean containsSessionId(String sessionId) {
        return sessionGroupMap.containsKey(sessionId);
    }

    @Override
    public boolean containsGroupId(String groupId) {
        return groupSessionMap.containsKey(groupId);
    }
}
