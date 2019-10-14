package com.home.liuhao.other.oauth.service;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.subject.Subject;

import com.home.liuhao.other.oauth.po.Client;
import com.home.liuhao.webbussess.po.User;

/**
 * @author ai996
 * info:授权客户端接口
 */
public interface ClientService {
	
	
	/**
	 * @param ClientId
	 * @return
	 * 判断clientId是否存在
	 */
	public boolean checkClientId(String clientId);
	
	public boolean login(Subject subject, HttpServletRequest request);
	
	/**
	 * @param ClientId
	 * @return
	 * 根据id查询客户端信息
	 */
	public Client findByClientId(String clientId);
	
	/**
	 * 验证secret 是否正确
	 * @param Secret
	 * @return
	 */
	boolean checkClientSecret(String Secret);
	/**
	 * info:验证code是否正确
	 * @param code
	 * @return
	 */
	boolean checkAuthCode(String code);
	
	/**
	 * 存code
	 * @param s
	 * @param o
	 */
	void addAuthCode(String s,Object o);
	
	/**
	 * @param code
	 * @return
	 * 根据code获取用户信息
	 */
	Object getAuthCode(String code);
	
	/**
	 * info:添加token
	 * @param token
	 * @param o
	 * 
	 */
	void addAccessToken(String token,String code);
	
	/**
	 * info:验证accessToken
	 * @param token
	 * @return
	 */
	boolean checkAccessToken(String token);
	
	/**
	 * info:根据token获取用户信息
	 * @param token
	 * @return
	 */
	User getUserByToken(String token);

}
