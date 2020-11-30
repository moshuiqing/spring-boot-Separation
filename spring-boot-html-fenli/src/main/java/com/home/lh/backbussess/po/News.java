package com.home.lh.backbussess.po;

import java.util.Date;

import org.springframework.data.elasticsearch.annotations.Document;

import com.home.lh.util.systemutil.DateUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 刘浩 新闻
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "News", description = "新闻实体")
@Document(indexName = "news")
public class News {

	/**
	 * 主键
	 */
	@ApiModelProperty("主键id")
	private Integer id;

	/**
	 * 标题
	 */
	@ApiModelProperty("新闻标题")
	private String title;

	/**
	 * 新闻类型
	 */
	@ApiModelProperty("新闻类型id")
	private String typeid;

	/**
	 * 创建时间
	 */
	@ApiModelProperty("创建时间")
	private Date createTime;

	/**
	 * 作者
	 */
	@ApiModelProperty("作者")
	private String author;

	/**
	 * 内容
	 */
	@ApiModelProperty("内容")
	private String content;

	/**
	 * 
	 */
	@ApiModelProperty("是否删除")
	private String isdel;

	@ApiModelProperty("新闻文本")
	private String wenben;
	@ApiModelProperty("类型名称")
	private String typeName;

	@ApiModelProperty("转换值用")
	private Integer ntId;

	public String getCreateTime() {
		return DateUtils.getStrDateTime(createTime);
	}

}
