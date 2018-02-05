package com.ws.oms.web.controller.register;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/add")
    public String add(){
        return "/register/add";
    }

}
