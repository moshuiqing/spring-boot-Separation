package com.home.liuhao.util.systemutil;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;

import com.home.liuhao.system.po.SysUser;
import com.home.liuhao.webbussess.po.User;

public class ShiroUtils {
	
    public static Subject getSubjct(){
        return SecurityUtils.getSubject();
    }
    
    public static SysUser getUser(){
        return (SysUser) getSubjct().getPrincipal();
    }
    
    /**
     * 切换身份，登录后，动态更改subject的用户属性
     * @param userInfo
     */
    public static void setUser(SysUser userInfo) {
    	Subject subject = SecurityUtils.getSubject();
    	PrincipalCollection principalCollection = subject.getPrincipals(); 
    	String realmName = principalCollection.getRealmNames().iterator().next();
    	PrincipalCollection newPrincipalCollection = 
    			new SimplePrincipalCollection(userInfo, realmName);
    	subject.runAs(newPrincipalCollection);
    }
    public static void setsmUser(User user,String name) {
    	Subject subject = SecurityUtils.getSubject();
    	PrincipalCollection principalCollection = subject.getPrincipals(); 
    	String realmName = principalCollection.getRealmNames().iterator().next();
    	PrincipalCollection newPrincipalCollection = 
    			new SimplePrincipalCollection(user, realmName);
    	subject.runAs(newPrincipalCollection);
    }
 
}
