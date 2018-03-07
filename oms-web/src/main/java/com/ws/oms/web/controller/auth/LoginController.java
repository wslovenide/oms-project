package com.ws.oms.web.controller.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

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

//    @RequestMapping(value = "/logon",method = RequestMethod.POST)
//    public Result<LoginRespVO> login(@ModelAttribute LoginReqVO loginVO){
//        logger.info("开始登陆, 传入的参数为:{}",loginVO);
//
//        if (StringUtils.isBlank(loginVO.getUsername()) || StringUtils.isBlank(loginVO.getPassword())){
//            logger.error("用户名或密码为空! username = {}, password = {}",loginVO.getUsername(),loginVO.getPassword());
//            return Result.newError(WebResultCode.USERNAME_PASSWORD_PARAM);
//        }
//
//        Subject subject = SecurityUtils.getSubject();
//        try {
//            subject.login(new UsernamePasswordToken(loginVO.getUsername(),loginVO.getPassword()));
//            logger.info("登陆成功 , username = {}" , loginVO.getUsername());
//        }catch (Exception e){
//            logger.info("登陆失败 , username = {}" , loginVO.getUsername());
//            return Result.newError(WebResultCode.USERNAME_PASSWORD_ERROR);
//        }
//        return null;
//    }


//    @RequestMapping(value = "/loginin")
//    public void login(@ModelAttribute LoginReqVO loginVO){
//        if (StringUtils.isBlank(loginVO.getUsername()) || StringUtils.isBlank(loginVO.getPassword())){
//            logger.error("用户名或密码为空! username = {}, password = {}",loginVO.getUsername(),loginVO.getPassword());
//            return;
//        }
//        Subject subject = SecurityUtils.getSubject();
//        try {
//            subject.login(new UsernamePasswordToken(loginVO.getUsername(),loginVO.getPassword()));
//            logger.info("登陆成功 , username = {}" , loginVO.getUsername());
//        }catch (Exception e){
//            logger.info("登陆失败 , username = {}" , loginVO.getUsername());
//        }
//    }

}
