package com.ws.oms.service.user.api;

import com.ws.oms.model.TbUser;
import com.ws.oms.result.Result;
import com.ws.oms.web.controller.auth.vo.LoginReqVO;
import com.ws.oms.web.controller.register.vo.RegisterReqVO;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-03-08 11:15
 */
public interface IUserService {

    Result<Boolean> register(RegisterReqVO reqVO);

    TbUser getByUserName(String useName);

    TbUser login(LoginReqVO reqVO);

}
