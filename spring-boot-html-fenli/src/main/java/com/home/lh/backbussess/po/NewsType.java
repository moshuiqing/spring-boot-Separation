package com.home.lh.backbussess.po;

import java.util.Date;

import com.home.lh.util.systemutil.DateUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 刘浩 新闻类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "NewsType", description = "新闻类型实体")
public class NewsType {

	@ApiModelProperty("主键id")
	private Integer id;
	@ApiModelProperty("类型名称")
	private String typeName;
	@ApiModelProperty("创建时间")
	private Date createTime;
	@ApiModelProperty("是否删除 0:未删除 1:已删除")
	private String isdel;
	@ApiModelProperty("修改比较参数用")
	private String copytypeName;

	public String getCreateTime() {
		return DateUtils.getStrDateTime(createTime);
	}

}
