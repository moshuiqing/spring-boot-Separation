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
@ApiModel(value = "Person", description = "人员类")
public class Person implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("主键id")
	private String id;
	
	@ApiModelProperty("姓名")
	private String name;
	
	@ApiModelProperty("手机号")
	private String phone;
	
	@ApiModelProperty("性别")
	private String gender;
	
	@ApiModelProperty("所在城市")
	private String city;
	
	@ApiModelProperty("邮箱")
	private String email;
	
	@ApiModelProperty("自我评价")
	private String myself;
	
	@ApiModelProperty("头像")
	private String headimg;
	
	@ApiModelProperty("生日")
	private String birthday;
	@ApiModelProperty("是否刪除")
	private String isdel;
	
	@ApiModelProperty("展示签名")
	private String sign;


}
