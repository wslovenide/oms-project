package com.ws.oms.web.controller.auth;

import com.ws.oms.web.controller.auth.vo.LoginReqVO;
import com.ws.oms.web.controller.auth.vo.LoginRespVO;
import com.ws.oms.web.enums.WebResultCode;
import com.ws.service.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-02-05 11:39
 */

@Controller
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/logon",method = RequestMethod.POST)
    public Result<LoginRespVO> login(@ModelAttribute LoginReqVO loginVO){
        logger.info("开始登陆...{}",loginVO);

        if (StringUtils.isBlank(loginVO.getUsername()) || StringUtils.isBlank(loginVO.getPassword())){
            logger.error("用户名或密码为空! username = {}, password = {}",loginVO.getUsername(),loginVO.getPassword());
            return Result.newError(WebResultCode.USERNAME_PASSWORD_PARAM);
        }

        return Result.newSuccess(new LoginRespVO());
    }

}
