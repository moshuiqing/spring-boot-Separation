package com.home.liuhao.system.config.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

import com.home.liuhao.system.config.shiro.po.LoginType;

public class MyHashedCredentialsMatcher extends HashedCredentialsMatcher {
	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		UserToken mupt = (UserToken) token;
		if (mupt.getLoginType().equals(LoginType.NOPASSWD)) {
			return true;
		}else {
			return super.doCredentialsMatch(token, info);
		}
		
	}
}
