package com.ws.oms.chat.netty.service.usergroup.impl;

import com.ws.oms.chat.netty.service.usergroup.IUserGroupService;
import java.util.Set;
import static com.ws.oms.chat.netty.util.RedisUtil.doInJedis;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-03-13 16:37
 */
public class UserGroupRedisService implements IUserGroupService {

    private static final String GROUP_SESSION_PREFIX = "GROUP_SESSION_PREFIX:";

    private static final String SESSION_GROUP_PREFIX = "SESSION_GROUP_PREFIX:";

    @Override
    public void save(String groupId, String sessionId) {
        doInJedis(redis -> {
            String groupKey = GROUP_SESSION_PREFIX + groupId;
            redis.sadd(groupKey, sessionId);

            String sessionKey = SESSION_GROUP_PREFIX + sessionId;
            redis.sadd(sessionKey, groupId);
            return null;
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<String> getSessionList(String groupId) {
        return (Set<String>)doInJedis(redis -> redis.smembers(GROUP_SESSION_PREFIX + groupId));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<String> getGroupList(String sessionId) {
        return (Set<String>)doInJedis(redis -> redis.smembers(SESSION_GROUP_PREFIX + sessionId));
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean containsSessionId(String sessionId) {
        return (Boolean) doInJedis(redis -> redis.exists(SESSION_GROUP_PREFIX + sessionId));
    }
}