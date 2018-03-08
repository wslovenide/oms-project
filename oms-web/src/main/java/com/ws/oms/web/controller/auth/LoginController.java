package com.ws.oms.web.controller.auth;

import com.ws.oms.model.TbUser;
import com.ws.oms.result.Result;
import com.ws.oms.service.user.api.IUserService;
import com.ws.oms.util.SCaptchaUtil;
import com.ws.oms.web.controller.auth.vo.LoginReqVO;
import com.ws.oms.web.controller.auth.vo.LoginRespVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.ws.oms.result.WebResultCode.*;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-02-05 11:39
 */

@RestController(value = "/login")
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private IUserService userService;

    private static final String USER_INFO_SESSION_KEY = "userInfo";


    @RequestMapping(value = "/getLoginInfo",method = RequestMethod.GET)
    public Result<LoginRespVO> getLoginInfo(HttpServletRequest request){
        HttpSession session = request.getSession();
        LoginRespVO userInfo = (LoginRespVO)session.getAttribute(USER_INFO_SESSION_KEY);
        logger.info("从session中取到的用户信息: {}" , userInfo);

        return Result.newSuccess(userInfo);
    }


    @RequestMapping(value = "/doLogin",method = RequestMethod.POST)
    public Result<LoginRespVO> login(@RequestBody LoginReqVO reqVO, HttpServletRequest request){
        logger.info("登陆信息为: {}" , reqVO);

        Result<LoginRespVO> validateLogin = validateLogin(reqVO,request);
        if (!validateLogin.isSuccess()){
            return validateLogin;
        }
        TbUser login = userService.login(reqVO);
        if (login == null){
            return Result.newError(USERNAME_PASSWORD_ERROR);
        }

        LoginRespVO respVO = new LoginRespVO();
        respVO.setUserName(login.getUsername());
        request.getSession().setAttribute(USER_INFO_SESSION_KEY,respVO);

        return Result.newSuccess(respVO);

    }

    private Result<LoginRespVO> validateLogin(LoginReqVO reqVO,HttpServletRequest request){
        if (StringUtils.isBlank(reqVO.getVerifyCode())){
            return Result.newError(VERIFY_CODE_NULL);
        }
        if (StringUtils.isAnyBlank(reqVO.getUsername(),reqVO.getPassword())){
            logger.info(" 登陆用户名和密码为空! ");
            return Result.newError(LOGIN_PARAM_NULL);
        }
        String verifyCode = (String) request.getSession().getAttribute(SCaptchaUtil.RANDOMCODEKEY);
        if (StringUtils.isBlank(verifyCode) ||  verifyCode.equals(reqVO.getVerifyCode())){
            logger.info("验证码错误! ");
            return Result.newError(VERIFY_CODE_ERROR);
        }
        return Result.newSuccess(null);
    }


}
