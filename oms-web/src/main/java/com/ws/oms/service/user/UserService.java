package com.ws.oms.service.user;

import com.ws.oms.mapper.TbUserMapper;
import com.ws.oms.model.TbUser;
import com.ws.oms.service.user.api.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-03-08 11:15
 */
@Service
public class UserService implements IUserService {

    @Resource
    private TbUserMapper tbUserMapper;

    @Override
    public TbUser getByUserName(String userName) {
        return tbUserMapper.selectByUserName(userName);
    }
}
