package com.ws.oms.web.controller.auth;

import com.ws.oms.web.controller.auth.vo.LoginReqVO;
import com.ws.oms.web.controller.auth.vo.LoginRespVO;
import com.ws.oms.web.enums.WebResultCode;
import com.ws.service.Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-02-05 11:39
 */

@RestController
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/logon",method = RequestMethod.POST)
    public Result<LoginRespVO> login(@ModelAttribute LoginReqVO loginVO){
        logger.info("开始登陆, 传入的参数为:{}",loginVO);

        if (StringUtils.isBlank(loginVO.getUsername()) || StringUtils.isBlank(loginVO.getPassword())){
            logger.error("用户名或密码为空! username = {}, password = {}",loginVO.getUsername(),loginVO.getPassword());
            return Result.newError(WebResultCode.USERNAME_PASSWORD_PARAM);
        }

        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(new UsernamePasswordToken(loginVO.getUsername(),loginVO.getPassword()));
            logger.info("登陆成功 , username = {}" , loginVO.getUsername());
        }catch (Exception e){
            logger.info("登陆失败 , username = {}" , loginVO.getUsername());
            return Result.newError(WebResultCode.USERNAME_PASSWORD_ERROR);
        }
        return Result.newSuccess(null);
    }

}
