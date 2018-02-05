package com.ws.oms.web.config.shiro;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-02-05 11:33
 */
@Configuration
public class ShiroConfiguration {

    @Bean
    public DefaultWebSecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(new OmsAuthRealm());
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(){
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSuccessUrl("/home/index");
        factoryBean.setUnauthorizedUrl("/home/login");
        factoryBean.setLoginUrl("/logon");

        Map<String,String> beanDefinitionMap = new HashMap<>();
        beanDefinitionMap.put("/logon","anon");
        beanDefinitionMap.put("/**","authc");
        factoryBean.setFilterChainDefinitionMap(beanDefinitionMap);

        factoryBean.setSecurityManager(securityManager());
        return factoryBean;
    }



}
