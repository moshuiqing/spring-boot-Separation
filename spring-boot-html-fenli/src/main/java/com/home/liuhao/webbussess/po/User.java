package com.home.liuhao.webbussess.po;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户实体
 * 
 * @author liuhao
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="User",description="前台用户")
public class User {

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键唯一id")
	private String id;

	/**
	 * 生日
	 */
	@ApiModelProperty(value = "生日")
	private String birthday;

	/**
	 * 头像
	 */
	@ApiModelProperty(value = "头像")
	private String HeadImg;

	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "手机号")
	private String phone;

	/**
	 * 用户名称（邮箱）
	 */
	@ApiModelProperty(value = "用户名称")
	private String userName;
	
	@ApiModelProperty("真实姓名")
	private String realName;

	/**
	 * 用户密码
	 */
	@ApiModelProperty(value = "用户密码")
	private String password;

	/**
	 * 盐
	 */
	@ApiModelProperty(value = "盐")
	private String salt;
	
	
	/**
	 * 最后登录时间
	 */
	@ApiModelProperty("最后登录时间")
	private String userEndTime;
	
	/**
	 * 创建时间
	 */
	@ApiModelProperty("创建时间")
	private String createTime;


	/**
	 * 0 未删除 1 已删除
	 */
	@ApiModelProperty(value = "是否删除 0：未删除 1：已删除")
	private String isdel;

	/**
	 * 0 启用 1 禁用
	 */
	@ApiModelProperty(value = "是否禁用 0：启用 1：禁用")
	private String isdisable;

	/**
	 * 描述
	 */
	@ApiModelProperty(value = "用户描述")
	private String remark;
	
	@ApiModelProperty("微信openid")
	private String openid;

	/**
	 * 唯一会话标识
	 */
	@ApiModelProperty(value = "唯一会话标识")
	private Serializable sessionId;

	/**
	 * 记住我
	 */
	@ApiModelProperty(value = "记住我")
	private boolean rememberMe;

	///// 非数据库///仅仅标识
	@ApiModelProperty(value = "登陆类型")
	private String loginType;

	@ApiModelProperty("存储明文密码")
	private String strmsg;

}
