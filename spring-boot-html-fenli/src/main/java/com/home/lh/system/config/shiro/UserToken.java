package com.home.lh.system.config.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author liuhao
 * 自定义tonken
 */
public class UserToken extends UsernamePasswordToken  {
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//登录类型，判断是什么登录类型
    private String loginType;

    public UserToken(final String username, final String password,String loginType,boolean rememberMe) {
        super(username,password,rememberMe);
        this.loginType = loginType;
    }

    public String getLoginType() {
        return loginType;
    }
    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }


}
