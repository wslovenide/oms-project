package com.ws.oms.web.controller.register;

import com.ws.oms.model.TbUser;
import com.ws.oms.result.Result;
import com.ws.oms.result.WebResultCode;
import com.ws.oms.service.user.api.IUserService;
import com.ws.oms.util.SCaptchaUtil;
import com.ws.oms.web.controller.register.vo.RegisterReqVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-02-05 16:55
 */
@RestController
@RequestMapping("/register")
public class RegisterController {

    private Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Resource
    private IUserService userService;


    @RequestMapping("/add")
    public Result<Boolean> add(@RequestBody RegisterReqVO reqVO, HttpServletRequest request){
        logger.info("开始注册:{}",reqVO);

        Result<Boolean> result =  validate(reqVO,request);
        if (!result.isSuccess()){
            return result;
        }
        return userService.register(reqVO);
    }

    private Result<Boolean> validate(RegisterReqVO reqVO, HttpServletRequest request){
        Result<Boolean> result = Result.newSuccess();

        String sessionVerifyCode = (String) request.getSession().getAttribute(SCaptchaUtil.RANDOMCODEKEY);
        if (StringUtils.isBlank(sessionVerifyCode) || StringUtils.isBlank(reqVO.getVerifyCode())){
            logger.info("验证码为空! sessionVerifyCode = {}",sessionVerifyCode);
            return result.setErrorCode(WebResultCode.VERIFY_CODE_NULL);
        }
        if (!sessionVerifyCode.equals(reqVO.getVerifyCode().trim())){
            logger.info("验证码为错误! sessionVerifyCode = {}",sessionVerifyCode);
            return result.setErrorCode(WebResultCode.VERIFY_CODE_ERROR);
        }
        if (StringUtils.isAnyBlank(reqVO.getUsername(),reqVO.getPassword(),reqVO.getConfirmPassword())){
            logger.info("注册的用户名或密码为空!");
            return result.setErrorCode(WebResultCode.PARAM_NULL_ERROR);
        }
        if (!reqVO.getPassword().equals(reqVO.getConfirmPassword())){
            logger.info("注册的两次密码不一致");
            return result.setErrorCode(WebResultCode.PASSWORD_CONFIRM_ERROR);
        }
        return result;
    }

}
