package com.ws.oms.service.user.api;

import com.ws.oms.model.TbUser;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-03-08 11:15
 */
public interface IUserService {

    TbUser getByUserName(String usename);

}
