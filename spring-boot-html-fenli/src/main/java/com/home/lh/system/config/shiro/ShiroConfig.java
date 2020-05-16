package com.home.lh.system.config.shiro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.io.ResourceUtils;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.home.lh.system.config.shiro.filter.CaptchaValidateFilter;
import com.home.lh.system.config.shiro.filter.CustomFormAuthenticationFilter;
import com.home.lh.system.config.shiro.filter.KickoutSessionControlFilter;
import com.home.lh.system.config.shiro.filter.WebSessionController;
import com.home.lh.system.config.shiro.realm.AppSmRealm;
import com.home.lh.system.config.shiro.realm.AppUserRealm;
import com.home.lh.system.config.shiro.realm.BackUserRealm;
import com.home.lh.system.config.shiro.realm.WebUserRealm;
import com.home.lh.system.config.shiro.realm.WxUserRealm;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.CacheManager;

@Configuration
@Slf4j
public class ShiroConfig {

	/**
	 * 加密方式
	 */
	@Value(value = "${type}")
	private String type;
	/**
	 * 迭代次数
	 */
	@Value(value = "${iterations}")
	private Integer iterations;

	/**
	 * session失效时间
	 */
	@Value(value = "${server.servlet.session.timeout}")
	private Integer timout;

	@Bean
	public FilterRegistrationBean<DelegatingFilterProxy> delegatingFilterProxy() {
		FilterRegistrationBean<DelegatingFilterProxy> filterRegistrationBean = new FilterRegistrationBean<DelegatingFilterProxy>();
		DelegatingFilterProxy proxy = new DelegatingFilterProxy();
		proxy.setTargetFilterLifecycle(true);
		proxy.setTargetBeanName("shiroFilter");
		filterRegistrationBean.setFilter(proxy);
		return filterRegistrationBean;
	}

	@Bean("shiroFilter")
	public ShiroFilterFactoryBean shiroFilterFactoryBean() {
		ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
		return filterFactoryBean;
	}

	/**
	 * 缓存管理器
	 * 
	 * @return cacheManager
	 */
	@Bean
	public EhCacheManager ehCacheManager() {

		CacheManager cacheManager = CacheManager.getCacheManager("es");
		if (cacheManager == null) {
			try {
				cacheManager = CacheManager.create(ResourceUtils.getInputStreamForPath("classpath:config/ehcache.xml"));
			} catch (IOException e) {
				throw new RuntimeException("initialize cacheManager failed");
			}
		}
		EhCacheManager ehCacheManager = new EhCacheManager();
		ehCacheManager.setCacheManager(cacheManager);
		return ehCacheManager;
	}

	/**
	 * @return info:配置加密方式和迭代次数
	 */
	/*@Bean(name = "hashMather")
	public HashedCredentialsMatcher hashedCredentialsMatcher() {

		HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
		credentialsMatcher.setHashAlgorithmName(type);
		//credentialsMatcher.setHashSalted(true);
		credentialsMatcher.setHashIterations(iterations);
		credentialsMatcher.setStoredCredentialsHexEncoded(true);
		return credentialsMatcher;
	}*/
	
	/**
	 * @return info:自定义配置加密方式和迭代次数
	 */
	@Bean(name = "hashMather")
	public MyHashedCredentialsMatcher hashedCredentialsMatcher() {

		MyHashedCredentialsMatcher credentialsMatcher = new MyHashedCredentialsMatcher();
		credentialsMatcher.setHashAlgorithmName(type);
		credentialsMatcher.setHashIterations(iterations);
		credentialsMatcher.setStoredCredentialsHexEncoded(true);
		return credentialsMatcher;
	}
	
	
	

	/**
	 * 后台用户
	 * 
	 * @return
	 * @param hashedCredentialsMatcher 凭证匹配器 info 创建Realm
	 */
	@Bean("backUserRealm")
	public BackUserRealm getRealm(@Qualifier("hashMather") MyHashedCredentialsMatcher hashedCredentialsMatche) {
		BackUserRealm realm = new BackUserRealm();
		realm.setCredentialsMatcher(hashedCredentialsMatche);
		return realm;
	}

	/**
	 * 前台用户
	 * @param hashedCredentialsMatche
	 * @return
	 */
	@Bean(name = "webUserRealm")
	public WebUserRealm getWebUserRealm(@Qualifier("hashMather") MyHashedCredentialsMatcher hashedCredentialsMatche) {
		WebUserRealm webUserRealm = new WebUserRealm();
		webUserRealm.setCredentialsMatcher(hashedCredentialsMatche);
		return webUserRealm;
	}
	/**
	 * 微信扫码免密登录
	 * @param hashedCredentialsMatche
	 * @return
	 */
	@Bean(name = "wxUserRealm")
	public WxUserRealm getWxUserRealm(@Qualifier("hashMather") MyHashedCredentialsMatcher hashedCredentialsMatche) {
		WxUserRealm wxUserRealm = new WxUserRealm();
		wxUserRealm.setCredentialsMatcher(hashedCredentialsMatche);
		return wxUserRealm;
	}
	
	/**
	 * app扫码登录
	 * @param hashedCredentialsMatche
	 * @return
	 */
	@Bean(name="appSmRealm")
	public AppSmRealm getAppSmRealm(@Qualifier("hashMather") MyHashedCredentialsMatcher hashedCredentialsMatche) {		
		AppSmRealm appSmRealm = new AppSmRealm();
		appSmRealm.setCredentialsMatcher(hashedCredentialsMatche);
		return appSmRealm;
		
	}
	
	/**
	 * app端用户登录
	 * @param hashedCredentialsMatche
	 * @return
	 */
	@Bean(name="appUserRealm")
	public AppUserRealm getAppUserRealm(@Qualifier("hashMather") MyHashedCredentialsMatcher hashedCredentialsMatche) {		
		AppUserRealm appUserRealm = new AppUserRealm();
		appUserRealm.setCredentialsMatcher(hashedCredentialsMatche);
		return appUserRealm;		
	}
	
	


	/**
	 * @param userRealm
	 * @return
	 * @Qualifier //指定名称的类导入 info 会话管理
	 */
	@Bean(name = "securityManager")
	public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("backUserRealm") BackUserRealm userRealm,
			@Qualifier("webUserRealm") WebUserRealm webUserRealm,@Qualifier("wxUserRealm")WxUserRealm wxUserRealm,
			@Qualifier("appUserRealm")AppUserRealm appUserRealm,@Qualifier("appSmRealm")AppSmRealm appSmRealm) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置realm.
		securityManager.setAuthenticator(customizedModularRealmAuthenticator());
		// 设置realm.
		// securityManager.setRealm(userRealm);
		List<Realm> realms = new ArrayList<>();
		realms.add(userRealm);
		realms.add(webUserRealm);
		realms.add(wxUserRealm);
		realms.add(appUserRealm);
		realms.add(appSmRealm);
		securityManager.setRealms(realms);// 多realm
		// 注入Cookie记住我管理器
		securityManager.setRememberMeManager(rememberMeManager());
		// 注入ehcache缓存管理器;
		securityManager.setCacheManager(ehCacheManager());
		// //注入session管理器;
		securityManager.setSessionManager(configWebSessionManager());
		 //====================多realm授权核心代码===================
        CustomizedModularRealmAuthorizer authorizer = new CustomizedModularRealmAuthorizer();
        authorizer.setRealms(realms);
        securityManager.setAuthorizer(authorizer);
        //====================多realm授权核心代码===================

		return securityManager;
	}

	/**
	 * 自定义的Realm管理，主要针对多realm
	 */
	@Bean
	public UserModularRealmAuthenticator customizedModularRealmAuthenticator() {
		UserModularRealmAuthenticator customizedModularRealmAuthenticator = new UserModularRealmAuthenticator();
		// 设置realm判断条件
		customizedModularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());

		return customizedModularRealmAuthenticator;
	}

	/**
	 * 记住我管理器 cookie管理对象;
	 * 
	 * @return
	 */
	@Bean(name = "cookieRememberMeManager")
	public CookieRememberMeManager rememberMeManager() {
		System.out.println("rememberMeManager()");
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(rememberMeCookie());
		cookieRememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));
		return cookieRememberMeManager;
	}

	/**
	 * cookie对象;
	 * 
	 * @return
	 */
	@Bean
	public SimpleCookie rememberMeCookie() {
		log.info("rememberMeCookie()");
		// 这个参数是cookie的名称，对应前端的checkbox 的name = rememberMe
		SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
		// <!-- 记住我cookie生效时间30天（259200） ,单位秒;-->
		simpleCookie.setMaxAge(259200);
		return simpleCookie;
	}

	/**
	 * 会话管理器
	 * 
	 * @return sessionManager
	 */
	@Bean
	public DefaultWebSessionManager configWebSessionManager() {
		DefaultWebSessionManager manager = new DefaultWebSessionManager();
		// 加入缓存管理器
		// manager.setCacheManager(ehCacheManager());

		manager.setSessionDAO(enterCacheSessionDAO());

		// 删除过期的session
		manager.setDeleteInvalidSessions(true);

		// 设置全局session超时时间
		manager.setGlobalSessionTimeout(timout * 1000);

		// 是否定时检查session
		manager.setSessionValidationSchedulerEnabled(true);
		manager.setSessionValidationScheduler(configSessionValidationScheduler());
		manager.setSessionIdUrlRewritingEnabled(false);
		manager.setSessionIdCookieEnabled(true);
		return manager;
	}

	/**
	 * EnterpriseCacheSessionDAO shiro sessionDao层的实现；
	 * 提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
	 */
	@Bean
	public EnterpriseCacheSessionDAO enterCacheSessionDAO() {
		EnterpriseCacheSessionDAO enterCacheSessionDAO = new EnterpriseCacheSessionDAO();
		// 添加缓存管理器
		// enterCacheSessionDAO.setCacheManager(ehCacheManager());
		// 添加ehcache活跃缓存名称（必须和ehcache缓存名称一致）
		enterCacheSessionDAO.setActiveSessionsCacheName("shiro-kickout-session");
		return enterCacheSessionDAO;
	}

	/**
	 * session会话验证调度器
	 * 
	 * @return session会话验证调度器
	 */
	@Bean
	public ExecutorServiceSessionValidationScheduler configSessionValidationScheduler() {
		ExecutorServiceSessionValidationScheduler sessionValidationScheduler = new ExecutorServiceSessionValidationScheduler();
		// 设置session的失效扫描间隔，单位为毫秒
		sessionValidationScheduler.setInterval(1800 * 1000);
		return sessionValidationScheduler;
	}

	/**
	 * 限制同一账号登录同时登录人数控制
	 * 
	 * @return 过滤器
	 */
	@Bean
	public KickoutSessionControlFilter kickoutSessionControlFilter() {
		KickoutSessionControlFilter kickoutSessionControlFilter = new KickoutSessionControlFilter();
		// 使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
		// 这里我们还是用之前shiro使用的redisManager()实现的cacheManager()缓存管理
		// 也可以重新另写一个，重新配置缓存时间之类的自定义缓存属性
		kickoutSessionControlFilter.setCacheManager(ehCacheManager());
		// 用于根据会话ID，获取会话进行踢出操作的；
		kickoutSessionControlFilter.setSessionManager(configWebSessionManager());
		// 是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；踢出顺序。
		kickoutSessionControlFilter.setKickoutAfter(false);
		// 同一个用户最大的会话数，默认1；比如2的意思是同一个用户允许最多同时两个人登录；
		kickoutSessionControlFilter.setMaxSession(1);

		// 被踢出后重定向到的地址；
		kickoutSessionControlFilter.setKickoutUrl("/backsystem/sysuser/tologin");
		return kickoutSessionControlFilter;
	}

	@Bean
	public ShiroFilterFactoryBean getshiroFilterFactoryBean(
			@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {

		ShiroFilterFactoryBean shiroFilterFactoryBean = shiroFilterFactoryBean();
		// 设置安全管理器
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// 添加shiro内置过滤器
		/*
		 * shiro 内置过滤器，可以实现权限相关的拦截器 info 常用的过滤器: anon:无需认证(登陆可以访问) authc:必须认证才可以访问
		 * user:如果使用rememberme的功能可以直接访问 perms:该资源必须得到资源权限才可以访问 role:该资源必须得到角色权限才 可以访问
		 */
		Map<String, String> filterMap = new LinkedHashMap<>();

		// 授权过滤器
		// 注意：当 前授权方 拦截后，shiro会自动跳到 未授权页面
		// <!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
		// 模糊全局匹配
		filterMap.put("/*/*/add*", "perms[新增]");
		filterMap.put("/*/*/update*", "perms[修改]");
		filterMap.put("/*/*/delete*", "perms[删除]");
		filterMap.put("/*/*/found*", "perms[查找]");
		filterMap.put("/*/*/send*", "perms[发送]");

		// 自定义拦截器
		Map<String, Filter> filtersMap = shiroFilterFactoryBean.getFilters();
		// 将自定义的FormAuthenticationFilter注入shiroFilter中
		filtersMap.put("myauthc", new CustomFormAuthenticationFilter());
		filtersMap.put("captchaVaildate", new CaptchaValidateFilter());// 验证码
		filtersMap.put("kickout", kickoutSessionControlFilter());// 后台用户过滤
		filtersMap.put("web", new WebSessionController());// 前台用户过滤
		shiroFilterFactoryBean.setFilters(filtersMap);

		/////////////////////////////////////////////////////////////////////////////////////////////////////////

		filterMap.put("/backsystem/sysuser/tologin", "anon");// 后台进入登录页
		filterMap.put("/backsystem/sysuser/sysLogin", "anon");// 后台登录放行
		filterMap.put("/captcha/**", "anon");// 验证码放行
		filterMap.put("/backsystem/sysuser/noLogin", "anon");// 后台登录提示未登录
		filterMap.put("/webbussess/weblogin/toWebLogin", "anon");// 前台登录首页放行
		filterMap.put("/webbussess/weblogin/webLogin", "anon");// 前台登录放行
		filterMap.put("/webbussess/weblogin/retrievePwd", "anon");//前台发送找回密码邮件放行
		filterMap.put("/webbussess/weblogin/resetPwd", "anon");//邮件重置密码放行
		filterMap.put("/webbussess/weblogin/toRegister", "anon");//前台跳转注册放行
		filterMap.put("/webbussess/weblogin/register", "anon");//前台注册放行
		filterMap.put("/baidu/eduit/config", "anon");// 百度编辑器配置放行
		filterMap.put("/appdesk/appUser/**", "anon");
		filterMap.put("/appdesk/appSys/getAppSys","anon");//获取初始化信息放行
		//filterMap.put("/appdesk/appUser/backPrompt", "anon");//app跳转未登录提示放行
		//filterMap.put("/appdesk/appUser/applogin","anon");//app登录跳转
		//filterMap.put("/appdesk/appUser/appVersion", "anon");//app版本验证是否需要更新		
	
		//filterMap.put("/appdesk/appUser/regist", "anon");//app注册放行
		
		
		// 放行swagger
		filterMap.put("/swagger-ui.html", "anon");
		filterMap.put("/swagger/**", "anon");
		filterMap.put("/webjars/**", "anon");
		filterMap.put("/swagger-resources/**", "anon");
		filterMap.put("/v2/**", "anon");
		filterMap.put("/druid/**", "anon");
		filterMap.put("/", "anon");
		

		filterMap.put("/backsystem/sysuser/sysLogin", "captchaVaildate");// 后台登录验证码过滤器
		filterMap.put("/backsystem/**", "kickout,myauthc,user");// 后台方法验证过滤器
		filterMap.put("/webbussess/weblogin/webLogin", "captchaVaildate");// 前台登录验证码过滤器
		filterMap.put("/webbussess/**", "myauthc,web,user");// 前台验证过滤器
		filterMap.put("/appdesk/**", "myauthc,user");//app登录验证器

		// 重定向的方法
		shiroFilterFactoryBean.setLoginUrl("myauthc");// 登录页面
		shiroFilterFactoryBean.setUnauthorizedUrl("/backsystem/sysuser/toother");// 没有权限提示

		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
		return shiroFilterFactoryBean;

	}

	public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		advisorAutoProxyCreator.setProxyTargetClass(true);
		return advisorAutoProxyCreator;
	}

	/**
	 * 开启shiro aop注解支持 使用代理方式所以需要开启代码支持
	 * 一定要写入上面advisorAutoProxyCreator（）自动代理。不然AOP注解不会生效
	 * 
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
			@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	/**
	 * @return 配置ShiroDialect ,用于thymeleaf和shiro标签配合使用
	 */
	@Bean
	public ShiroDialect getShiroDialect() {
		return new ShiroDialect();
	}

}
