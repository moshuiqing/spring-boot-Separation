package com.home.lh.system.po;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "SysUser", description = "系统用户类")
public class SysUser implements Serializable {
	
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("主键唯一id")
	private String id;

	@ApiModelProperty("系统用户登录账号")
	private String userName;

	@ApiModelProperty("登录密码")
	private String passWord;

	@ApiModelProperty("盐")
	private String salt;

	@ApiModelProperty("是否单点登录0否1是")
	private String isSingle;

	@ApiModelProperty("是否删除0否1是")
	private String isdel;

	@ApiModelProperty("是否禁用0否1是")
	private String isDisable;

	@ApiModelProperty("用户最后登录时间")
	private String userEndTime;

	@ApiModelProperty("创建时间")
	private String createTime;

	@ApiModelProperty("对应人id")
	private String pid;

	@ApiModelProperty("对应角色id")
	private String rid;
	
	@ApiModelProperty("登录状态")
	private String status;

	@ApiModelProperty("签名")
	private String sign;
	
	@ApiModelProperty("图像")
	private String headImg;
	
	@ApiModelProperty("对应的人信息")
	private Person person;
	
	@ApiModelProperty("对应的角色信息")
	private Role role;
	
	/////非数据库///仅仅标识
	@ApiModelProperty("登录类型")
	private String loginType;
	@ApiModelProperty("消息")
	private String strmsg;
	
	@ApiModelProperty(" 记住我")
	private boolean rememberMe;
	

}
