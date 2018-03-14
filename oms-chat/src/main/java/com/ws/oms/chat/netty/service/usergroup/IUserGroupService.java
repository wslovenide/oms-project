package com.ws.oms.chat.netty.service.usergroup;

import java.util.Set;

/**
 * Description:
 * sessionId 和 group 管理
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-03-13 13:24
 */
public interface IUserGroupService {

    void save(String groupId, String sessionId);

    Set<String> getSessionList(String groupId);

    Set<String> getGroupList(String sessionId);

    boolean containsSessionId(String sessionId);

    boolean containsGroupId(String groupId);
}
