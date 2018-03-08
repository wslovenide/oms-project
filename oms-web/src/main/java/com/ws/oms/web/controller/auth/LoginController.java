package com.ws.oms.web.controller.auth;

import com.ws.oms.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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


    @RequestMapping(value = "/getLoginInfo",method = RequestMethod.GET)
    public Result<Object> getLoginInfo(HttpServletRequest request){
        HttpSession session = request.getSession();
        Object userInfo = session.getAttribute("userInfo");
        logger.info("从session中取到的用户信息: {}" , userInfo);

        return Result.newSuccess(userInfo);
    }


    public void login(HttpServletRequest request){



    }


}
