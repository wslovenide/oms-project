package com.ws.oms.web.controller.register;

import com.ws.oms.model.TbUser;
import com.ws.oms.service.user.api.IUserService;
import com.ws.oms.web.controller.register.vo.RegisterReqVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
    public TbUser add(@RequestBody RegisterReqVO reqVO){
        logger.info("开始注册:{}",reqVO);


        System.out.println(userService);

        return userService.getByUserName("admin");

//        return "/register/add";
    }

}
