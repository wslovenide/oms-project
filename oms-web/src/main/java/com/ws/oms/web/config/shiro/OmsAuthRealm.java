package com.ws.oms.web.config.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-02-05 11:54
 */
public class OmsAuthRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(OmsAuthRealm.class);

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Object next = principalCollection.fromRealm(getName()).iterator().next();
        logger.info("登陆的用户名为:{}",next);

        AuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
        logger.info("登陆信息： {}" , token);

        if (!"admin".equals(token.getUsername())){
            return null;
        }
        String defaultPassword = "123456";

        return new SimpleAuthenticationInfo(token.getPrincipal(),defaultPassword,getName());
    }
}
