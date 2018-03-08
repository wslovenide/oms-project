package com.ws.oms.web.controller.auth;

import com.ws.oms.util.SCaptchaUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description:
 * 验证码
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-03-08 13:11
 */
@Controller
@RequestMapping("/verifyCode")
public class VerifyCodeController {

    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public void getCode(HttpServletRequest request, HttpServletResponse response){
       // 禁止图像缓存。
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        SCaptchaUtil captchaUtil = new SCaptchaUtil();
        captchaUtil.getRandcode(request,response);
    }

}
