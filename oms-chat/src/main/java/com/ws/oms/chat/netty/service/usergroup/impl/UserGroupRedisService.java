package com.ws.oms.chat.netty.service.usergroup.impl;

import com.ws.oms.chat.netty.service.usergroup.IUserGroupService;

import java.util.List;
import java.util.Set;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-03-13 16:37
 */
public class UserGroupRedisService implements IUserGroupService {

    @Override
    public void save(String groupId, String sessionId) {

    }

    @Override
    public Set<String> getSessionList(String groupId) {
        return null;
    }

    @Override
    public Set<String> getGroupList(String sessionId) {
        return null;
    }

    @Override
    public boolean containsSessionId(String sessionId) {
        return false;
    }
}
