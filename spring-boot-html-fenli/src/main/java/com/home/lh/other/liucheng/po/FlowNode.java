package com.home.lh.other.liucheng.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 概要说明 : 流程节点 <br>
 * 详细说明 : 流程节点 <br>
 * 创建时间 : 2018年7月19日 上午10:05:03 <br>
 * 
 * @author by liuhao
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Flow", description = "流程类")
public class FlowNode {

	/**
	 * id:节点id
	 */
	private Integer id;

	/**
	 * flowId:流程id
	 */
	private String flowId;

	/**
	 * process_name:节点名称
	 */
	private String processname;

	/**
	 * process_type:节点类型0：状态节点1：前操作2:后操作
	 */
	private String processtype;

	/**
	 * process_to:指向下一个节点
	 */
	private String processto;

	/**
	 * setleft:节点对应左边的位置
	 */
	private String setleft;

	/**
	 * settop:节点对应上边的位置
	 */
	private String settop;

	/**
	 * style:节点样式
	 */
	private String style;

	/**
	 * icon:节点图标
	 */
	private String icon;

	/**
	 * mark:备注
	 */
	private String mark;

	/**
	 * isdel:是否删除 0：否1：是
	 */
	private String isdel;

	/**
	 * updater:更新者id
	 */
	private String updater;

	/**
	 * creater:创建者id
	 */
	private String creater;

	/**
	 * updateTime:更新时间
	 */
	private String updateTime;

	/**
	 * createTime:创建时间
	 */
	private String createTime;

	/**
	 * nodeCode:节点编码
	 */
	private String nodeCode;

	// //////////////////////////////////////////

	/**
	 * pd:判断是自定义还是默认写编码
	 */
	private String pd;

	/**
	 * flag:节点弹窗判断用
	 */
	private String flag;

	public FlowNode(Integer id, String flowId) {
		super();
		this.id = id;
		this.flowId = flowId;
	}

	//////////////////////////////////////////////
	/**
	 * styleWidth:节点长
	 */
	@ApiModelProperty("节点长 ")
	private String styleWidth;

	/**
	 * styleHeight:节点高
	 */
	@ApiModelProperty("节点高")
	private String styleHeight;

	/**
	 * styleColor:字体颜色
	 */
	@ApiModelProperty("字体颜色")
	private String styleColor;

	/**
	 * state:展示选择框判断用
	 */
	@ApiModelProperty("展示选择框判断用")
	private Integer state;

	/**
	 * msg:返回信息用
	 */
	@ApiModelProperty("返回信息用")
	private String msg;

	/**
	 * processtoCopy：判断用
	 */
	@ApiModelProperty("判断用")
	private String processtoCopy;

}
