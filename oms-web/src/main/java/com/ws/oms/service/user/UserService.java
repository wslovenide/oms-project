package com.ws.oms.service.user;

import com.ws.oms.mapper.TbUserMapper;
import com.ws.oms.model.TbUser;
import com.ws.oms.result.Result;
import com.ws.oms.service.user.api.IUserService;
import com.ws.oms.web.controller.auth.vo.LoginReqVO;
import com.ws.oms.web.controller.register.vo.RegisterReqVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.ws.oms.result.WebResultCode.USER_NAME_EXISTS;

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

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @Resource
    private TbUserMapper tbUserMapper;

    @Override
    public Result<Boolean> register(RegisterReqVO reqVO) {
        TbUser user = getByUserName(reqVO.getUsername());
        if (user != null){
            logger.info("用户名已经存在! reqVO = {}",reqVO);
            return Result.newError(USER_NAME_EXISTS);
        }
        user = new TbUser();
        user.setUsername(reqVO.getUsername());
        user.setPassword(reqVO.getPassword());
        tbUserMapper.insertSelective(user);
        logger.info("注册成功! userName = {}" , reqVO.getUsername());
        return Result.newSuccess(true);
    }

    @Override
    public TbUser getByUserName(String userName) {
        return tbUserMapper.selectByUserName(userName);
    }

    @Override
    public TbUser login(LoginReqVO reqVO) {
        TbUser user = getByUserName(reqVO.getUsername());
        if (user == null || !user.getPassword().equals(reqVO.getPassword())){
            return null;
        }
        return user;
    }
}
