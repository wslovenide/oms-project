package com.ws.oms.chat.netty.service.usergroup;

import java.util.List;

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

    List<String> getSessionList(String groupId);

    List<String> getGroupList(String sessionId);

}
