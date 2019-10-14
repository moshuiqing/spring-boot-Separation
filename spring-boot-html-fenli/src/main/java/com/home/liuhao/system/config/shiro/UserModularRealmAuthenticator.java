package com.home.liuhao.system.config.shiro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import lombok.extern.slf4j.Slf4j;

/**
 * 当配置了多个Realm时，我们通常使用的认证器是shiro自带的org.apache.shiro.authc.pam.ModularRealmAuthenticator，其中决定使用的Realm的是doAuthenticate()方法
 *
 * 自定义Authenticator
 * 注意，当需要分别定义处理学生和教师和管理员验证的Realm时，对应Realm的全类名应该包含字符串“Student”“Teacher”，或者“Admin”。
 * 并且，他们不能相互包含，例如，处理学生验证的Realm的全类名中不应该包含字符串"Admin"。
 */
@Slf4j
public class UserModularRealmAuthenticator extends ModularRealmAuthenticator {


	@Override
	protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		// 判断getRealms() 是否返回为空
		assertRealmsConfigured();
		// 强制转换回自定义的CustomizedToken
		UserToken userToken = (UserToken) authenticationToken;

		// 登录类型
		String loginType = userToken.getLoginType();
		// 所有realms
		Collection<Realm> realms = getRealms();
		// 登录类型对应的所有realm
		List<Realm> list = new ArrayList<>();
		for (Realm realm : realms) {
			// System.out.println(realm.getName());
			if (realm.getName().contains(loginType)) {
				list.add(realm);
			}
		}
		// 判断是单realm还是多realm
		if (list.size() == 1) {
			log.info("单realm");
			return doSingleRealmAuthentication(list.get(0), userToken);
		} else {
			log.info("多realm");
			return doMultiRealmAuthentication(list, userToken);
		}
	}

}
